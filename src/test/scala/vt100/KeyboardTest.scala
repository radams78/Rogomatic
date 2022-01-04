package vt100

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class KeyboardTest extends AnyFlatSpec with should.Matchers {
  "A keyboard" should "click when a code-transmitting key is pressed" in {
      object MockClick extends IClick {
          var clicked = false

          override def click() : Unit = clicked = true
      }
      object MockTransmitter extends ITransmitter {
          override def transmit(char : Char) : Unit = ()
      }
      val keyboard = Keyboard(MockTransmitter, MockClick)
      keyboard.press('g')
      MockClick should be (Symbol("clicked"))
  }

  "A keyboard" should "transmit an upper-case character when CAPS LOCK is down" in {
      object MockClick extends IClick {
          override def click() = ()
      }
      object MockTransmitter extends ITransmitter {
          var receivedChar = false
          override def transmit(char : Char) : Unit = {
            char should be ('M')
            receivedChar = true
          }
      }

      val keyboard = Keyboard(MockTransmitter, MockClick, capsLock = true)
      keyboard.press('m')
      MockTransmitter should be (Symbol("receivedChar"))
  }
}
