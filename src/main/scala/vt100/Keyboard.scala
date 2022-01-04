package vt100

class Keyboard(transmitter: ITransmitter, click: IClick) extends IKeyboard {
  override def press(char: Char): Unit = {
    click.click()
    transmitter.transmit(char)
  }
}
