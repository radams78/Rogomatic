class VT100 {
  var firstChar = ' '
  def getScreen() : Seq[String] = Seq(firstChar + " " * 79) ++ Seq.fill(23)(" " * 80)

  def sendChar(char : Char) : Unit = firstChar = char
}
