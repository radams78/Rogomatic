package vt100

class Keyboard(transmitter: ITransmitter, click: IClick, capsLock : Boolean = false) extends IKeyboard {
  override def press(char: Char): Unit = {
    click.click()
    char match {
        case c if c.isLower => transmitter.transmit(if capsLock then c.toUpper else c)
        case c if c.isUpper => transmitter.transmit(c)
    }
  }
}
