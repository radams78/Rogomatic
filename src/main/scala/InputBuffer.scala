import scala.collection.immutable.Queue
class InputBuffer(display : VT100Display) {
    private var contents : Queue[Char] = Queue()
    def add(char: Char) : Unit = char match {
      case VT100.NUL | VT100.DEL => ()
      case char => {// TODO Keep consuming characters until we cannot any more
        contents = contents :+ char
        for (cs, rest) <- InputBuffer.interpretBuffer(contents) do {
          performAction(cs)
          contents = rest
        }
      }
    }

    private def performAction(charSeq : InputBuffer.CharSeq) : Unit = charSeq match {
      case InputBuffer.CharSeq.Backspace => display.backspace()
      case InputBuffer.CharSeq.Linefeed => display.lineFeed()
      case InputBuffer.CharSeq.CarriageReturn => display.carriageReturn()
      case InputBuffer.CharSeq.CursorBackwards(n) => for i <- 1 to n do display.backspace()
      case InputBuffer.CharSeq.NormalChar(c) => display.printChar(c)
    }

}

object InputBuffer {
  private def interpretBuffer(contents : Queue[Char]) : Option[(InputBuffer.CharSeq, Queue[Char])] = {
    contents.dequeueOption match {
      case Some(VT100.BS, tail) =>
        Some(InputBuffer.CharSeq.Backspace, tail)
      case Some(c, tail) if c == VT100.LF || c == VT100.VT || c == VT100.FF => 
        Some(InputBuffer.CharSeq.Linefeed, tail)
      case Some(VT100.CR, tail) =>
        Some(InputBuffer.CharSeq.CarriageReturn, tail)
      case Some(VT100.ESC, tail) =>
        if contents.lift(1) == Some('[') && contents.lift(2) == Some('D')
        then Some(InputBuffer.CharSeq.CursorBackwards(1), contents.drop(3))
        else None
      case Some(c, tail) => Some(InputBuffer.CharSeq.NormalChar(c), tail)
      case None => None
    }
  }

  private enum CharSeq {
    case Backspace
    case Linefeed
    case CarriageReturn
    case CursorBackwards(n : Int)
    case NormalChar(c : Char)
  }
}
