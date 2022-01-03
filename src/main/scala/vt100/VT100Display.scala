package vt100

private class VT100Display(private val x : Int, 
                   private val y : Int, 
                   private val screenContents : String) extends IDisplay {
  private var cursor : VT100Display.Cursor = VT100Display.Cursor(x, y)

  private var screen : Array[Array[Char]] = 
    screenContents
      .split("\n")
      .padTo(VT100Display.HEIGHT,"")
      .map(_.padTo(VT100Display.WIDTH,' ').toCharArray)

  def getScreen() : Seq[String] = screen.map(_.mkString)

  def getCursorX() : Int = cursor.x

  def getCursorY() : Int = cursor.y

  def putChar(c : Char) = screen(cursor.y - 1)(cursor.x - 1) = c

  def setCursorX(x : Int) : Unit = cursor = VT100Display.Cursor(x, cursor.y)

  def setCursorY(y : Int) : Unit = cursor = VT100Display.Cursor(cursor.x, y)

  private def advanceCursor() : Unit =
    if cursor.x < VT100Display.WIDTH then cursor = VT100Display.Cursor(cursor.x + 1, cursor.y)

  def scroll() : Unit = {
    for y <- 0 until VT100Display.HEIGHT - 1 do screen(y) = screen(y+1)
    screen(VT100Display.HEIGHT - 1) = Array.fill(VT100Display.WIDTH)(' ')
  }
}

object VT100Display {
  val WIDTH = 80
  val HEIGHT = 24

  private case class Cursor(x : Int, y : Int):
    assert(x > 0, s"Cursor moved off left edge of screen: ($x,$y)")
    assert(x <= WIDTH, s"Cursor moved off right edge of screen: ($x,$y)")
    assert(y > 0, s"Cursor moved off top of screen: ($x,$y)")
    assert(y <= HEIGHT, s"Cursor moved off bottom of screen: ($x,$y)")
}