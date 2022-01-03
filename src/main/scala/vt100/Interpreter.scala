package vt100

class Interpreter(display : VT100Display) {
    def backspace() : Unit = 
        display.setCursorX((display.getCursorX() - 1).max(1))

    def lineFeed() : Unit = 
        if display.getCursorY() == VT100Display.HEIGHT
        then display.scroll()
        else display.setCursorY(display.getCursorY() + 1)
  
    def carriageReturn() : Unit = display.setCursorX(1)

    def cursorBackwards(n : Int) : Unit = {
        assert(n >= 0)
        val distance = if n == 0 then 1 else n
        display.setCursorX((display.getCursorX() - distance).max(1))
    }

    def cursorDown(n : Int) : Unit = {
        assert(n >= 0)
        val distance = if n == 0 then 1 else n
        display.setCursorY((display.getCursorY() + distance).min(VT100Display.HEIGHT))
    }

    def cursorForwards(n : Int) : Unit = {
        assert(n >= 0)
        val distance = if n == 0 then 1 else n
        display.setCursorX((display.getCursorX() + distance).min(VT100Display.WIDTH))
    }
}
