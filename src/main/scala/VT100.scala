class VT100(x : Int = 1, y : Int = 1, screenContents : String = ""):
  private var screen : Array[Array[Char]] = 
    screenContents
      .split("\n")
      .padTo(24,"")
      .map(_.padTo(80,' ').toCharArray)
  private var cursor : Cursor = Cursor(x, y)
  private var controlSeq = 0
  
  def getScreen() : Seq[String] = screen.map(_.mkString)

  def getCursorX() : Int = cursor.x

  def getCursorY() : Int = cursor.y

  def sendChar(char : Char) : Unit = 
    char match
      case VT100.NUL | VT100.DEL => ()
      case VT100.BS => backspace()
      case VT100.LF | VT100.VT | VT100.FF => lineFeed()
      case VT100.CR => cursor = Cursor(1, cursor.y)
      case VT100.ESC => controlSeq += 1
      case '[' => if (controlSeq > 0) controlSeq += 1 else printChar('[')
      case 'D' => if (controlSeq == 2) backspace() else printChar('D')
      case c => printChar(c)

  private def printChar(char : Char) =
    screen(cursor.y - 1)(cursor.x - 1) = char
    advanceCursor()

  private def backspace() : Unit =
    if (cursor.x > 1) cursor = Cursor(cursor.x - 1, cursor.y)

  private def advanceCursor() : Unit =
    if cursor.x < 80 then cursor = Cursor(cursor.x + 1, cursor.y)

  private def lineFeed() : Unit =
    if cursor.y == 24
    then scroll()
    else cursor = Cursor(cursor.x, cursor.y + 1)

  private def scroll() : Unit =
    for y <- 0 to 22 do screen(y) = screen(y+1)
    screen(23) = Array.fill(80)(' ')

  private case class Cursor(x : Int, y : Int):
    assert(x > 0, s"Cursor moved off left edge of screen: ($x,$y)")
    assert(x <= 80, s"Cursor moved off right edge of screen: ($x,$y)")
    assert(y > 0, s"Cursor moved off top of screen: ($x,$y)")
    assert(y <= 24, s"Cursor moved off bottom of screen: ($x,$y)")

object VT100:
  val NUL = '\u0000'
  val BS = '\u0008'
  val LF = '\u000a'
  val VT = '\u000b'
  val FF = '\u000c'
  val CR = '\u000d'
  val ESC = '\u001b'
  val DEL = '\u007f'