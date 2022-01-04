package vt100

import javax.smartcardio.CommandAPDU

class Keyboard(
    transmitter: ITransmitter,
    click: IClick,
    private var _capsLock: Boolean = false
) extends IKeyboard {
  override def capsLock : Boolean = _capsLock

  override def press(key : Key): Unit = {
    click.click()
    key match {
        case Key.Letter(l) => transmitter.transmit(if capsLock then l.toUpper else l)
        case Key.CapsLock => _capsLock = ! _capsLock
        case key => ()
    }
  }

  override def pressWithShift(key : Key) : Unit = {
    click.click()
    key match {
        case Key.Letter(l) => transmitter.transmit(l.toUpper)
        case _ => ()
    }
  }
}

enum Key {
    case Setup
    case Up
    case Down
    case Left
    case Right

    case Esc
    case One
    case Two
    case Three
    case Four
    case Five
    case Six
    case Seven
    case Eight
    case Nine
    case Zero
    case Minus
    case Equals
    case BackslashOne
    case Backspace
    case Break

    case Tab
    case Letter(letter : Char) // TODO Must be lower-case letter
    case LeftBracket
    case RightBracket
    case Return
    case Delete

    case CapsLock
    case Semicolon
    case Apostrophe
    case BackslashTwo
    
    case NoScroll
    case Comma
    case Period
    case Slash
    case Linefeed

    case Space

    case PF1
    case PF2
    case PF3
    case PF4
    case Num0
    case Num1
    case Num2
    case Num3
    case Num4
    case Num5
    case Num6
    case Num7
    case Num8
    case Num9
    case NumMinus
    case NumComma
    case NumEnter
    case NumPeriod
}