package vt100

class Interpreter(display : VT100Display) {
    def printChar(c : Char) : Unit = {
      display.putChar(c)
      display.setCursorX((display.getCursorX() + 1).min(VT100Display.WIDTH))
    }
    
    def backspace() : Unit = 
        display.setCursorX((display.getCursorX() - 1).max(1))

    def lineFeed() : Unit = 
        if display.getCursorY() == VT100Display.HEIGHT
        then display.scroll()
        else display.setCursorY(display.getCursorY() + 1)
  
    def carriageReturn() : Unit = display.setCursorX(1)

    def cursorBackwards(n : Int) : Unit = {
        assert(n >= 0)
        val distance = n.max(1)
        display.setCursorX((display.getCursorX() - distance).max(1))
    }

    def cursorDown(n : Int) : Unit = {
        assert(n >= 0)
        val distance = n.max(1)
        display.setCursorY((display.getCursorY() + distance).min(VT100Display.HEIGHT))
    }

    def cursorForwards(n : Int) : Unit = {
        assert(n >= 0)
        val distance = n.max(1)
        display.setCursorX((display.getCursorX() + distance).min(VT100Display.WIDTH))
    }

    def cursorPosition(y : Int, x : Int) : Unit = {
        assert(y >= 0)
        assert(x >= 0)
        if (y > VT100Display.HEIGHT) then return
        if (x > VT100Display.WIDTH) then return
        val line = y.max(1)
        val column = x.max(1)
        display.setCursorX(column)
        display.setCursorY(line)
    }
}
