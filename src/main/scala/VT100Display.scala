class VT100Display(x : Int, y : Int, screenContents : String):
  private var screen : Array[Array[Char]] = 
    screenContents
      .split("\n")
      .padTo(24,"")
      .map(_.padTo(80,' ').toCharArray)

  def getScreen() : Seq[String] = screen.map(_.mkString)

  def printChar(char : Char, cursor : VT100.Cursor) : Unit = screen(cursor.y - 1)(cursor.x - 1) = char

  def scroll() : Unit =
    for y <- 0 to 22 do screen(y) = screen(y+1)
    screen(23) = Array.fill(80)(' ')

