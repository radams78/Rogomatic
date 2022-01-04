package terminal

class Terminal {
  private var screenContents : Array[Array[Char]] = Array.fill(Terminal.HEIGHT, Terminal.WIDTH)(' ')
  private var cursorX = 1
  private var cursorY = 1

  def getScreen() : Seq[String] = screenContents.map(_.mkString).toSeq
  def getCursorX() : Int = cursorX
  def getCursorY() : Int = cursorY

  def sendChar(char : Char) : Unit = char match {
      case Terminal.NUL => ()
      case c if ! c.isControl => {
        screenContents(cursorY - 1)(cursorX - 1) = char
        if (cursorX < Terminal.WIDTH) then cursorX += 1
      }
      case c => throw new Error("Unrecognized character: " + c)
  }
}

object Terminal {
  private val HEIGHT = 24
  private val WIDTH = 80
  private val NUL = '\u0000'
}
