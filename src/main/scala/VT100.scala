import scala.collection.immutable.Queue
import scala.collection.mutable.Stack
import java.{util => ju}

class VT100(x : Int = 1, y : Int = 1, screenContents : String = ""):
  private var display : VT100Display = VT100Display(x, y, screenContents)
  private var inputBuffer : InputBuffer = InputBuffer()
  private var cursor : VT100.Cursor = VT100.Cursor(x, y)
  private var controlSeq = 0
  
  def getScreen() : Seq[String] = display.getScreen()

  def getCursorX() : Int = cursor.x

  def getCursorY() : Int = cursor.y

  def sendChar(char : Char) : Unit = 
    char match 
      case VT100.NUL | VT100.DEL => ()
      case char => // TODO Keep consuming characters until we cannot any more
        inputBuffer = inputBuffer.add(char)
        for (cs, rest) <- interpretBuffer(inputBuffer) do
          performAction(cs)
          inputBuffer = rest

  private def interpretBuffer(inputBuffer : InputBuffer) : Option[(CharSeq, InputBuffer)] =
    inputBuffer.dequeueOption match
      case Some(VT100.BS, tail) =>
        Some(CharSeq.Backspace, tail)
      case Some(c, tail) if c == VT100.LF || c == VT100.VT || c == VT100.FF => 
        Some(CharSeq.Linefeed, tail)
      case Some(VT100.CR, tail) =>
        Some(CharSeq.CarriageReturn, tail)
      case Some(VT100.ESC, tail) =>
        if inputBuffer.lift(1) == Some('[') && inputBuffer.lift(2) == Some('D')
        then Some(CharSeq.CursorBackwards(1), inputBuffer.drop(3))
        else None
      case Some(c, tail) => Some(CharSeq.NormalChar(c), tail)
      case None => None

  private def performAction(charSeq : CharSeq) : Unit = charSeq match
    case CharSeq.Backspace => backspace()
    case CharSeq.Linefeed => lineFeed()
    case CharSeq.CarriageReturn => cursor = VT100.Cursor(1, cursor.y)
    case CharSeq.CursorBackwards(n) => for i <- 1 to n do backspace()
    case CharSeq.NormalChar(c) => printChar(c)

  private enum CharSeq:
    case Backspace
    case Linefeed
    case CarriageReturn
    case CursorBackwards(n : Int)
    case NormalChar(c : Char)

  private def printChar(char : Char) = 
    display.printChar(char, cursor)
    advanceCursor()

  private def backspace() : Unit =
    if (cursor.x > 1) cursor = VT100.Cursor(cursor.x - 1, cursor.y)

  private def advanceCursor() : Unit =
    if cursor.x < 80 then cursor = VT100.Cursor(cursor.x + 1, cursor.y)

  private def lineFeed() : Unit =
    if cursor.y == 24
    then scroll()
    else cursor = VT100.Cursor(cursor.x, cursor.y + 1)

  private def scroll() : Unit = display.scroll()

object VT100:
  val NUL = '\u0000'
  val BS = '\u0008'
  val LF = '\u000a'
  val VT = '\u000b'
  val FF = '\u000c'
  val CR = '\u000d'
  val ESC = '\u001b'
  val DEL = '\u007f'

  case class Cursor(x : Int, y : Int):
    assert(x > 0, s"Cursor moved off left edge of screen: ($x,$y)")
    assert(x <= 80, s"Cursor moved off right edge of screen: ($x,$y)")
    assert(y > 0, s"Cursor moved off top of screen: ($x,$y)")
    assert(y <= 24, s"Cursor moved off bottom of screen: ($x,$y)")
