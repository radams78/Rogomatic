package terminal

class Terminal {
  def getScreen() : Seq[String] = Seq.fill(24)(
      " " * 80
    )

  def getCursorX() : Int = 1
  def getCursorY() : Int = 1
}
