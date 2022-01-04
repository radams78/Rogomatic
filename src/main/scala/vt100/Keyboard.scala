package vt100

import javax.smartcardio.CommandAPDU

class Keyboard(
    transmitter: ITransmitter,
    click: IClick,
    private var _capsLock: Boolean = false,
    private var _newLine : NewLine = NewLine.LineFeed
) extends IKeyboard {
  override def capsLock: Boolean = _capsLock

  override def press(key: Key): Unit = {
    click.click()
    key match {
      case Key.One    => transmitter.transmit('\u0031')
      case Key.Two    => transmitter.transmit('\u0032')
      case Key.Three  => transmitter.transmit('\u0033')
      case Key.Four   => transmitter.transmit('\u0034')
      case Key.Five   => transmitter.transmit('\u0035')
      case Key.Six    => transmitter.transmit('\u0036')
      case Key.Seven  => transmitter.transmit('\u0037')
      case Key.Eight  => transmitter.transmit('\u0038')
      case Key.Nine   => transmitter.transmit('\u0039')
      case Key.Zero   => transmitter.transmit('\u0030')
      case Key.Minus  => transmitter.transmit('\u002d')
      case Key.Equals => transmitter.transmit('\u003d')
      case Key.LeftBracket =>
        transmitter.transmit(
          '['
        ) // \u005b gives an "unclosed character literal" error for some reason
      case Key.Semicolon    => transmitter.transmit('\u003b')
      case Key.Apostrophe   => transmitter.transmit('\u0027')
      case Key.Comma        => transmitter.transmit('\u002c')
      case Key.Period       => transmitter.transmit('\u002e')
      case Key.Slash        => transmitter.transmit('\u002f')
      case Key.BackslashOne => transmitter.transmit('\u005c')
      case Key.BackslashTwo => transmitter.transmit('\u005c')
      case Key.Space        => transmitter.transmit('\u0060')
      case Key.RightBracket => transmitter.transmit('\u005d')
      case Key.Letter(l) =>
        transmitter.transmit(if capsLock then l.toUpper else l)
      case Key.CapsLock => _capsLock = !_capsLock
      case Key.Return => _newLine match {
        case NewLine.LineFeed => transmitter.transmit('\u000d')
        case NewLine.NewLine =>
          transmitter.transmit('\u000d')
          transmitter.transmit('\n')
      }
      case key          => ()
    }
  }

  override def pressWithShift(key: Key): Unit = {
    click.click()
    key match {
      case Key.One    => transmitter.transmit('\u0021')
      case Key.Two    => transmitter.transmit('\u0040')
      case Key.Three  => transmitter.transmit('\u0023')
      case Key.Four   => transmitter.transmit('\u0024')
      case Key.Five   => transmitter.transmit('\u0025')
      case Key.Six    => transmitter.transmit('\u005e')
      case Key.Seven  => transmitter.transmit('\u0026')
      case Key.Eight  => transmitter.transmit('\u002a')
      case Key.Nine   => transmitter.transmit('\u0028')
      case Key.Zero   => transmitter.transmit('\u0029')
      case Key.Minus  => transmitter.transmit('\u005f')
      case Key.Equals => transmitter.transmit('\u002b')
      case Key.LeftBracket =>
        transmitter.transmit(
          '{'
        ) // \u007b gives an "unclosed character literal" error for some reason
      case Key.Semicolon    => transmitter.transmit('\u003a')
      case Key.Apostrophe   => transmitter.transmit('\u0022')
      case Key.Comma        => transmitter.transmit('\u003c')
      case Key.Period       => transmitter.transmit('\u003e')
      case Key.Slash        => transmitter.transmit('\u003f')
      case Key.BackslashOne => transmitter.transmit('\u007c')
      case Key.BackslashTwo => transmitter.transmit('\u007e')
      case Key.Space        => transmitter.transmit('\u0060')
      case Key.RightBracket => transmitter.transmit('\u007d')
      case Key.Letter(l) =>
        transmitter.transmit(l.toUpper)
      case Key.CapsLock => _capsLock = !_capsLock
      case Key.Return => _newLine match {
        case NewLine.LineFeed => transmitter.transmit('\u000d')
        case NewLine.NewLine =>
          transmitter.transmit('\u000d')
          transmitter.transmit('\n')
      }
      case key          => ()
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
  case Letter(letter: Char) // TODO Must be lower-case letter
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

enum NewLine {
  case LineFeed
  case NewLine
}