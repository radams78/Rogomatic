package terminal

class Terminal(x : Int = 1, y : Int = 1) {
  private var screenContents : Array[Array[Char]] = Array.fill(Terminal.HEIGHT, Terminal.WIDTH)(' ')
  private var cursorX = x
  private var cursorY = y

  def getScreen() : Seq[String] = screenContents.map(_.mkString).toSeq
  def getCursorX() : Int = cursorX
  def getCursorY() : Int = cursorY

  def sendChar(char : Char) : Unit = char match {
      case Terminal.NUL => ()
      case '\u0008' => if cursorX > 1 then cursorX -= 1
      case '\u007f' => ()
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
}
