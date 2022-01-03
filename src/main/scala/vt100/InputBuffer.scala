package vt100

import scala.collection.immutable.Queue
import scala.compiletime.ops.int

private class InputBuffer(display : VT100Display, interpreter : Interpreter) {
    private var contents : Queue[Char] = Queue()
    
    def add(char: Char) : Unit = char match {
      case VT100.NUL | VT100.DEL => ()
      case char => {
        contents = contents :+ char
        for (cs, rest) <- InputBuffer.interpretBuffer(contents) do {
          performAction(cs)
          contents = rest
        }
      }
    }

    private def performAction(charSeq : InputBuffer.CharSeq) : Unit = charSeq match {
      case InputBuffer.CharSeq.Backspace => interpreter.backspace()
      case InputBuffer.CharSeq.Linefeed => interpreter.lineFeed()
      case InputBuffer.CharSeq.CarriageReturn => interpreter.carriageReturn()
      case InputBuffer.CharSeq.CursorBackwards(n) => interpreter.cursorBackwards(n)
      case InputBuffer.CharSeq.CursorDown(n) => interpreter.cursorDown(n)
      case InputBuffer.CharSeq.CursorForwards(n) => interpreter.cursorForwards(n)
      case InputBuffer.CharSeq.CursorPosition(y, x) => interpreter.cursorPosition(y, x)
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
      case Some(VT100.ESC, tail) => interpretSequenceAfterEscape(tail)
      case Some(c, tail) => Some(InputBuffer.CharSeq.NormalChar(c), tail)
      case None => None
    }
  }

  private def interpretSequenceAfterEscape(contents : Queue[Char]) : Option[(CharSeq, Queue[Char])] =
    contents.dequeueOption match {
      case Some('[', tail) => interpretSequenceAfterCSI(tail, Seq(), 0)
      case _ => None
    }

  private def interpretSequenceAfterCSI(contents : Queue[Char], parameters : Seq[Int], currentParameter : Int) : Option[(CharSeq, Queue[Char])] =
    contents.dequeueOption match {
      case Some('B', tail) => 
        Some(CharSeq.CursorDown(currentParameter), tail)
      case Some('C', tail) => 
        Some(CharSeq.CursorForwards(if currentParameter == 0 then 1 else currentParameter), tail)
      case Some('D', tail) => 
        Some(CharSeq.CursorBackwards(if currentParameter == 0 then 1 else currentParameter), tail)
      case Some('H', tail) => parameters match {
        case Nil => if currentParameter == 0 then Some(CharSeq.CursorPosition(1, 1), tail) else None
        case y :: Nil => 
          Some(CharSeq.CursorPosition(y, currentParameter), tail)
        case _ => None
      }
      case Some(n, tail) if '0' <= n && n <= '9' => 
        interpretSequenceAfterCSI(tail, parameters, currentParameter * 10 + n - '0')
      case Some(';', tail) =>
        interpretSequenceAfterCSI(tail, parameters :+ currentParameter, 0)
      case _ => None
    }

  private enum CharSeq {
    case Backspace
    case Linefeed
    case CarriageReturn
    case CursorBackwards(n : Int)
    case CursorDown(n : Int)
    case CursorForwards(n : Int)
    case CursorPosition(x : Int, y : Int)
    case NormalChar(c : Char)
  }
}
