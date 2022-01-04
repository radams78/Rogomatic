package terminal

class Terminal {
  def getScreen() : Seq[String] = Seq.fill(24)(
      " " * 80
    )
}
