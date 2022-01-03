package vt100

class Keyboard(transmitter : Transmitter) extends IKeyboard {
    override def press(char : Char) : Unit = transmitter.transmit(char)
}
