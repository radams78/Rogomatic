package vt100

class VT100 private (display : IDisplay, inputBuffer : IInputBuffer, keyboard : IKeyboard) extends IVT100 {
  override def getScreen() : Seq[String] = display.getScreen()

  override def getCursorX() : Int = display.getCursorX()

  override def getCursorY() : Int = display.getCursorY()

  override def press(key : Key) : Unit = keyboard.press(key)

  override def pressWithShift(key : Key) : Unit = keyboard.pressWithShift(key)

  private[vt100] def sendChar(char : Char) : Unit = inputBuffer.add(char)
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

  def apply(x : Int = 1, y : Int = 1, screenContents : String = "") : VT100 = {
    val display = VT100Display(x, y, screenContents)
    new VT100(display, InputBuffer(Interpreter(display)), Keyboard(Transmitter(None),Click()))
  }

  def apply(host : IHost) : VT100 = {
    val display = VT100Display(1, 1, "")
    new VT100(display, InputBuffer(Interpreter(display)), Keyboard(Transmitter(Some(host)),Click()))
  }
}