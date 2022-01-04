package terminal

class Terminal {
  private var screenContents : Array[Array[Char]] = Array.fill(24, 80)(' ')
  var cursorX = 1

  def getScreen() : Seq[String] = screenContents.map(_.mkString).toSeq
  def getCursorX() : Int = cursorX
  def getCursorY() : Int = 1
  def sendChar(char : Char) : Unit = {
    screenContents(0)(0) = char
    cursorX = 2
  }
}
