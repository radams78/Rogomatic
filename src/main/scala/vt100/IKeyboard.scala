package vt100

trait IKeyboard {
  def press(char : Char) : Unit
}
