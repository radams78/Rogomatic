package vt100

trait IInputBuffer {
  def add(char : Char) : Unit
}
