import scala.collection.immutable.Queue
import scala.collection.mutable.Stack
import java.{util => ju}

class VT100(x : Int = 1, y : Int = 1, screenContents : String = ""):
  private var screen : Array[Array[Char]] = 
    screenContents
      .split("\n")
      .padTo(24,"")
      .map(_.padTo(80,' ').toCharArray)
  private var cursor : Cursor = Cursor(x, y)
  private var inputBuffer : Queue[Char] = Queue()
  private var controlSeq = 0
  
  def getScreen() : Seq[String] = screen.map(_.mkString)

  def getCursorX() : Int = cursor.x

  def getCursorY() : Int = cursor.y

  def sendChar(char : Char) : Unit = 
    char match 
      case VT100.NUL | VT100.DEL => ()
      case char => // TODO Keep consuming characters until we cannot any more
        inputBuffer = inputBuffer :+ char
        inputBuffer.dequeueOption match
          case Some(VT100.BS, tail) =>
            inputBuffer = tail
            performAction(CharSeq.Backspace)
          case Some(c, tail) if c == VT100.LF || c == VT100.VT || c == VT100.FF => 
            inputBuffer = tail
            performAction(CharSeq.Linefeed)
          case Some(VT100.CR, tail) =>
            inputBuffer = tail
            performAction(CharSeq.CarriageReturn)
          case Some(VT100.ESC, tail) =>
            if inputBuffer.lift(1) == Some('[') && inputBuffer.lift(2) == Some('D')
            then
              inputBuffer = inputBuffer.drop(3)
              backspace()
          case Some(c, tail) =>
            inputBuffer = tail
            printChar(c)
          case None => ()

  private def performAction(charSeq : CharSeq) : Unit = charSeq match
    case CharSeq.Backspace => backspace()
    case CharSeq.Linefeed => lineFeed()
    case CharSeq.CarriageReturn => cursor = Cursor(1, cursor.y)
    case CharSeq.CursorBackwards(n) => for i <- 1 to n do backspace()
    case CharSeq.NormalChar(c) => printChar(c)

  private enum CharSeq:
    case Backspace
    case Linefeed
    case CarriageReturn
    case CursorBackwards(n : Int)
    case NormalChar(c : Char)

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