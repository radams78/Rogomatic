class VT100Display(x : Int, y : Int, screenContents : String):
  private var cursor : VT100Display.Cursor = VT100Display.Cursor(x, y)

  private var screen : Array[Array[Char]] = 
    screenContents
      .split("\n")
      .padTo(VT100Display.HEIGHT,"")
      .map(_.padTo(VT100Display.WIDTH,' ').toCharArray)

  def getScreen() : Seq[String] = screen.map(_.mkString)

  def getCursorX() : Int = cursor.x

  def getCursorY() : Int = cursor.y

  def printChar(char : Char) : Unit = 
      screen(cursor.y - 1)(cursor.x - 1) = char
      advanceCursor()

  def backspace() : Unit = if (cursor.x > 1) cursor = VT100Display.Cursor(cursor.x - 1, cursor.y)

  def carriageReturn() : Unit = cursor = VT100Display.Cursor(1, cursor.y)

  def lineFeed() : Unit =
    if cursor.y == VT100Display.HEIGHT
    then scroll()
    else cursor = VT100Display.Cursor(cursor.x, cursor.y + 1)

  private def advanceCursor() : Unit =
    if cursor.x < VT100Display.WIDTH then cursor = VT100Display.Cursor(cursor.x + 1, cursor.y)

  private def scroll() : Unit =
    for y <- 0 until VT100Display.HEIGHT - 1 do screen(y) = screen(y+1)
    screen(VT100Display.HEIGHT - 1) = Array.fill(VT100Display.WIDTH)(' ')

object VT100Display:
  private val WIDTH = 80
  private val HEIGHT = 24

  private case class Cursor(x : Int, y : Int):
    assert(x > 0, s"Cursor moved off left edge of screen: ($x,$y)")
    assert(x <= WIDTH, s"Cursor moved off right edge of screen: ($x,$y)")
    assert(y > 0, s"Cursor moved off top of screen: ($x,$y)")
    assert(y <= HEIGHT, s"Cursor moved off bottom of screen: ($x,$y)")
