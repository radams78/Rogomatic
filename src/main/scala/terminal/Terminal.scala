package terminal

import scala.collection.immutable.Queue
import scala.annotation.meta.param

/** This terminal: * ignores tab characters * does not scroll - ignores line
  * feeds and cursor down commands when at the bottom of the screen * ignores SO
  * and SI codes * ignores XON and XOFF codes, and does not transmit them *
  * ignores CPR sequences
  */
class Terminal(x: Int = 1, y: Int = 1) {
  private var screenContents: Array[Array[Char]] =
    Array.fill(Terminal.HEIGHT, Terminal.WIDTH)(' ')
  private var cursorX = x
  private var cursorY = y
  private var inputBuffer: Queue[Char] = Queue()
  private var escape = 0

  def getScreen(): Seq[String] = screenContents.map(_.mkString).toSeq
  def getCursorX(): Int = cursorX
  def getCursorY(): Int = cursorY

  def sendChar(char: Char): Unit = char match {
    case Terminal.NUL | Terminal.DEL => ()
    case c =>
      inputBuffer = inputBuffer :+ c
      inputBuffer.dequeue match
        case (Terminal.BS, tail) =>
          if cursorX > 1 then cursorX -= 1
          inputBuffer = tail
        case (Terminal.LF | Terminal.VT | Terminal.FF, tail) =>
          if cursorY < Terminal.HEIGHT then cursorY += 1
          inputBuffer = tail
        case (Terminal.CR, tail) =>
          cursorX = 1
          inputBuffer = tail
        case (Terminal.ESC, tail) => parseSequenceAfterEscape(tail)
        case (c, tail) if !c.isControl => {
          screenContents(cursorY - 1)(cursorX - 1) = char
          if (cursorX < Terminal.WIDTH) then cursorX += 1
          inputBuffer = tail
        }
        case (c, tail) => throw new Error("Unrecognized character " + c)
  }

  private def parseSequenceAfterEscape(tail: Queue[Char]): Unit =
    tail.dequeueOption match
      case Some('[', tail) => parseSequenceAfterCSI(tail, Seq('['), Seq(), 0)
      case Some(c, tail) =>
        throw new Error("Unrecognized escape sequence: ESC + " + c)
      case None => ()

  private def parseSequenceAfterCSI(
      tail: Queue[Char],
      sequence: Seq[Char],
      parameters: Seq[Int],
      currentParameter: Int
  ): Unit = tail.dequeueOption match
    case Some('B', tail) =>
      cursorY = (cursorY + currentParameter.max(1)).min(Terminal.HEIGHT)
      inputBuffer = tail
    case Some('C', tail) =>
      cursorX = (cursorX + currentParameter.max(1)).min(Terminal.WIDTH)
      inputBuffer = tail
    case Some('D', tail) =>
      cursorX = (cursorX - currentParameter.max(1)).max(1)
      inputBuffer = tail
    case Some('H', tail) => {
      parameters.length match {
        case 0 =>
          if currentParameter == 0 then {
            cursorX = 1
            cursorY = 1
          }
        case 1 => {
          val x = currentParameter.max(1)
          val y = parameters(0).max(1)
          if (1 <= x && x <= Terminal.WIDTH && 1 <= y && y <= Terminal.HEIGHT) then {
            cursorX = x
            cursorY = y
          }
        }
        case n => ()
      }
      inputBuffer = tail
    }
    case Some(';', tail) =>
      parseSequenceAfterCSI(
        tail,
        sequence :+ ';',
        parameters :+ currentParameter,
        0
      )
    case Some(n, tail) if n.isDigit =>
      parseSequenceAfterCSI(
        tail,
        sequence :+ n,
        parameters,
        10 * currentParameter + n.asDigit
      )
    case Some(c, tail) =>
      throw new Error(
        "Unrecognized escape sequence: ESC + " + sequence.mkString + c
      )
    case None => ()
}

object Terminal {
  private val HEIGHT = 24
  private val WIDTH = 80
  private val NUL = '\u0000'
  private val BS = '\u0008'
  private val DEL = '\u007f'
  private val LF = '\n' // same as

  private val VT = '\u000b'
  private val FF = '\u000c'
  private val CR = '\u000d'
  private val ESC = '\u001b'
}
