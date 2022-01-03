package vt100

class VT100(display : VT100Display, inputBuffer : InputBuffer, host : Option[IHost]) {
  def getScreen() : Seq[String] = display.getScreen()

  def getCursorX() : Int = display.getCursorX()

  def getCursorY() : Int = display.getCursorY()

  def sendChar(char : Char) : Unit = inputBuffer.add(char)

  def press(char : Char) : Unit =
    for h <- host do h.sendChar(char)
}

object VT100 {
  val NUL = '\u0000'
  val BS = '\u0008'
  val LF = '\u000a'
  val VT = '\u000b'
  val FF = '\u000c'
  val CR = '\u000d'
  val ESC = '\u001b'
  val DEL = '\u007f'

  def apply(x : Int = 1, y : Int = 1, screenContents : String = "") : VT100= {
    val display = VT100Display(x, y, screenContents)
    new VT100(display, InputBuffer(Interpreter(display)), None)
  }

  def apply(host : IHost) : VT100 = {
    val display = VT100Display(1, 1, "")
    new VT100(display, InputBuffer(Interpreter(display)), Some(host))
  }
}