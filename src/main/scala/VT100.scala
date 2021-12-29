class VT100 {
  var screen : Array[Array[Char]] = Array.fill(24)(Array.fill(80)(' '))
  
  def getScreen() : Seq[String] = screen.map(_.mkString)

  def sendChar(char : Char) : Unit = screen(0)(0) = char
}