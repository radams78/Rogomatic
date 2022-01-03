package vt100

trait IDisplay {
  def getCursorX() : Int
  def getCursorY() : Int
  def getScreen() : Seq[String]
}
