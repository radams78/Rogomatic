package vt100

trait IKeyboard {
  def press(key : Key) : Unit

  def pressWithShift(key : Key) : Unit
}
