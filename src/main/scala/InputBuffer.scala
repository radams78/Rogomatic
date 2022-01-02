import scala.collection.immutable.Queue
class InputBuffer(contents : Queue[Char] = Queue(), display : VT100Display) {
    def add(char: Char) : InputBuffer = InputBuffer(contents :+ char, display)

    def dequeueOption : Option[(Char, InputBuffer)] = 
        for (c, q) <- contents.dequeueOption yield (c, InputBuffer(q, display))

    def lift(n : Int) : Option[Char] = contents.lift(n)

    def drop(n : Int) : InputBuffer = InputBuffer(contents.drop(n), display)

    private def performAction(charSeq : InputBuffer.CharSeq) : Unit = charSeq match {
      case InputBuffer.CharSeq.Backspace => display.backspace()
      case InputBuffer.CharSeq.Linefeed => display.lineFeed()
      case InputBuffer.CharSeq.CarriageReturn => display.carriageReturn()
      case InputBuffer.CharSeq.CursorBackwards(n) => for i <- 1 to n do display.backspace()
      case InputBuffer.CharSeq.NormalChar(c) => display.printChar(c)
    }

}

object InputBuffer {
  enum CharSeq {
    case Backspace
    case Linefeed
    case CarriageReturn
    case CursorBackwards(n : Int)
    case NormalChar(c : Char)
  }
}
