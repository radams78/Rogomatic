package terminal

class Screen(initialContents : String) {
  var screenContents : Array[Array[Char]] = initialContents
    .split('\n')
    .padTo(24, "")
    .map(_.padTo(80,' ').toCharArray)
    .toArray

  def getContents() : Seq[String] = screenContents.map(_.mkString).toSeq

  def printChar(x : Int, y : Int, char : Char) : Unit = {
      assert(1 <= x)
      assert(x <= Terminal.WIDTH)
      assert(1 <= y)
      assert(y <= Terminal.HEIGHT)
      screenContents(y - 1)(x - 1) = char
    }
}
