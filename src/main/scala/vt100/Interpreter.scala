package vt100

class Interpreter(display : VT100Display) {
    def backspace() : Unit = if (display.getCursorX() != 1) display.moveCursorLeft()
  
    def cursorBackwards(n : Int) : Unit = {
        assert(n >= 0)
        val distance = if n == 0 then 1 else n
        if (display.getCursorX() - n < 0) display.setCursorX(1) else display.moveCursorLeft(distance)
    }
}
