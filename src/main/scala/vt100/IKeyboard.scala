package vt100

trait IKeyboard {
  /** If char is a lowercase character, this represents the user pressing a letter key.
   * If char is an uppercase character, this represents the user pressing SHIFT with a letter key. */
  def press(char : Char) : Unit
}
