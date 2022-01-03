package vt100

class Transmitter(host : Option[IHost]) {
  def transmit(char : Char) = for h <- host do h.sendChar(char)
}
