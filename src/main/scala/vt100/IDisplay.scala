package vt100

private trait IDisplay {
  def getCursorX() : Int
  def getCursorY() : Int
  def getScreen() : Seq[String]
}
