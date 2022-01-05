package terminal

import scala.collection.immutable.Queue

/** This terminal:
  * * ignores tab characters
  * * does not scroll - ignores line feeds and cursor down commands when at the bottom of the screen
  * * ignores SO and SI codes
  * * ignores XON and XOFF codes, and does not transmit them
  * * ignores CPR sequences
  */
class Terminal(x : Int = 1, y : Int = 1) {
  private var screenContents : Array[Array[Char]] = Array.fill(Terminal.HEIGHT, Terminal.WIDTH)(' ')
  private var cursorX = x
  private var cursorY = y
  private var inputBuffer : Queue[Char] = Queue()
  private var escape = 0

  def getScreen() : Seq[String] = screenContents.map(_.mkString).toSeq
  def getCursorX() : Int = cursorX
  def getCursorY() : Int = cursorY

  def sendChar(char : Char) : Unit = char match {
      case Terminal.NUL | Terminal.DEL => ()
      case c =>
        inputBuffer = inputBuffer :+ c
        inputBuffer.dequeue match
          case (Terminal.BS, tail) =>
            if cursorX > 1 then cursorX -= 1
            inputBuffer = tail
          case (Terminal.LF | Terminal.VT | Terminal.FF , tail) => 
            if cursorY < Terminal.HEIGHT then cursorY += 1
            inputBuffer = tail
          case (Terminal.CR, tail) => 
            cursorX = 1
            inputBuffer = tail
          case ('\u001b', tail) => tail.dequeueOption match
            case Some('[', tail) => tail.dequeueOption match
              case Some('D', tail) =>
                if cursorX > 1 then cursorX -= 1
                inputBuffer = tail
              case Some(c, tail) => throw new Error("Unrecognized escape sequence: ESC + [ + " + c)
              case None => ()
            case Some(c, tail) => throw new Error("Unrecognized escape sequence: ESC + " + c)
            case None => ()
          case (c, tail) if ! c.isControl => {
            screenContents(cursorY - 1)(cursorX - 1) = char
            if (cursorX < Terminal.WIDTH) then cursorX += 1
            inputBuffer = tail
          }
          case (c, tail) => throw new Error("Unrecognized character " + c)
  }
}

object Terminal {
  private val HEIGHT = 24
  private val WIDTH = 80
  private val NUL = '\u0000'
  private val BS = '\u0008'
  private val DEL = '\u007f'
  private val LF = '\n' // same as \u000a
  private val VT = '\u000b'
  private val FF = '\u000c'
  private val CR = '\u000d'
}
