class VT100(x : Int = 1, y : Int = 1) {
  private var screen : Array[Array[Char]] = Array.fill(24)(Array.fill(80)(' '))
  private var cursor : Cursor = Cursor(x, y)
  
  def getScreen() : Seq[String] = screen.map(_.mkString)

  def getCursorX() : Int = cursor.x

  def getCursorY() : Int = cursor.y

  def sendChar(char : Char) : Unit = char match {
    case VT100.NUL => ()
    case VT100.BS => cursor = Cursor(cursor.x - 1, cursor.y) // todo left edge
    case VT100.LF => cursor = Cursor(cursor.x, cursor.y + 1) // todo bottom margin
    case c => printChar(c)
  }

  private def printChar(char : Char) = {
    screen(cursor.y - 1)(cursor.x - 1) = char
    advanceCursor()  
  }

  private def advanceCursor() : Unit = {
    if (cursor.x < 80) cursor = Cursor(cursor.x + 1, cursor.y)
  }

  private class Cursor(val x : Int, val y : Int)
}

object VT100 {
  val NUL = '\u0000'
  val BS = '\u0008'
  val LF = '\n'
}