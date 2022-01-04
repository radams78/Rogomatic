package vt100

trait ITransmitter {
  def transmit(char : Char) : Unit
}
