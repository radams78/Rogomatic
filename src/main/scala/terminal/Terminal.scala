package terminal

class Terminal {
  var firstChar = ' '
  var cursorX = 1

  def getScreen() : Seq[String] = Seq(
      firstChar + " " * 79
    ) ++ Seq.fill(23)(" " * 80)

  def getCursorX() : Int = cursorX
  def getCursorY() : Int = 1
  def sendChar(char : Char) : Unit = {
    firstChar = char
    cursorX = 2
  }
}
