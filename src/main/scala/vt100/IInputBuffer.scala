package vt100

private trait IInputBuffer {
  def add(char : Char) : Unit
}
