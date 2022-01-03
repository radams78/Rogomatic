package vt100

private class Transmitter(host : Option[IHost]) {
  def transmit(char : Char) = for h <- host do h.sendChar(char)
}
