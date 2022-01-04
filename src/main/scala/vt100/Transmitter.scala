package vt100

private class Transmitter(host : Option[IHost]) extends vt100.ITransmitter {
  def transmit(char : Char) = for h <- host do h.sendChar(char)
}
