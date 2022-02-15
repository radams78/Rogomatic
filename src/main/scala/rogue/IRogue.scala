package rogue

trait IRogue {
    // Must return an array of 24 strings of length 80
  def getScreen() : Seq[String]

  def sendKeypress(keypress : Char) : Unit
}
