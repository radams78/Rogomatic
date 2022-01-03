package vt100

trait IVT100 {
  def getScreen() : Seq[String]
  def getCursorX() : Int
  def getCursorY() : Int
  def press(char : Char) : Unit
}
