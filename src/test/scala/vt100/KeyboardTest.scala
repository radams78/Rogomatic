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
}
