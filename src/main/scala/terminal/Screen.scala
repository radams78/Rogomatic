package terminal

class Screen(initialContents : String) {
  var screenContents : Array[Array[Char]] = initialContents
    .split('\n')
    .padTo(24, "")
    .map(_.padTo(80,' ').toCharArray)
    .toArray

  def getContents() : Seq[String] = screenContents.map(_.mkString).toSeq
}
