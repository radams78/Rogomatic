package terminal

/** This terminal:
  * * ignores tab characters
  * * does not scroll - ignores line feeds and cursor down commands when at the bottom of the screen
  * * ignores SO and SI codes
  * * ignores XON and XOFF codes, and does not transmit them
  */
class Terminal(x : Int = 1, y : Int = 1) {
  private var screenContents : Array[Array[Char]] = Array.fill(Terminal.HEIGHT, Terminal.WIDTH)(' ')
  private var cursorX = x
  private var cursorY = y

  def getScreen() : Seq[String] = screenContents.map(_.mkString).toSeq
  def getCursorX() : Int = cursorX
  def getCursorY() : Int = cursorY

  def sendChar(char : Char) : Unit = char match {
      case Terminal.NUL => ()
      case Terminal.BS => if cursorX > 1 then cursorX -= 1
      case Terminal.DEL => ()
      case '\n' | Terminal.VT | Terminal.FF => if cursorY < Terminal.HEIGHT then cursorY += 1
      case '\u000d' => cursorX = 1
      case c if ! c.isControl => {
        screenContents(cursorY - 1)(cursorX - 1) = char
        if (cursorX < Terminal.WIDTH) then cursorX += 1
      }
      case c => throw new Error("Unrecognized character: " + c.toInt)
  }
}

object Terminal {
  private val HEIGHT = 24
  private val WIDTH = 80
  private val NUL = '\u0000'
  private val BS = '\u0008'
  private val DEL = '\u007f'
  private val VT = '\u000b'
  private val FF = '\u000c'
}
