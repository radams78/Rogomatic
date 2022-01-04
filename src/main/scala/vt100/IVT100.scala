package vt100

trait IVT100 {
  def getScreen() : Seq[String]
  def getCursorX() : Int
  def getCursorY() : Int
  def press(key : Key) : Unit
  def pressWithShift(key : Key) : Unit
}
