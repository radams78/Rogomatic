package vt100

trait IKeyboard {
  def capsLock : Boolean
  
  def press(key : Key) : Unit

  def pressWithShift(key : Key) : Unit
}
