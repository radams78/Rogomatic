package vt100

trait IHost {
  def sendChar(char : Char) : Unit
}
