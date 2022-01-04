package terminal

class Terminal {
  private var screenContents : Array[Array[Char]] = Array.fill(24, 80)(' ')
  private var cursorX = 1
  private var cursorY = 1

  def getScreen() : Seq[String] = screenContents.map(_.mkString).toSeq
  def getCursorX() : Int = cursorX
  def getCursorY() : Int = cursorY
  def sendChar(char : Char) : Unit = {
    screenContents(cursorY - 1)(cursorX - 1) = char
    cursorX += 1
  }
}
