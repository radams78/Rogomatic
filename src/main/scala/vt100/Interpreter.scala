package vt100

class Interpreter(display : VT100Display) {
    def backspace() : Unit = if (display.getCursorX() != 1) display.moveCursorLeft()
  
}
