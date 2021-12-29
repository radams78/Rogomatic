import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class VT100Scala extends AnyFlatSpec with should.Matchers {
  "A VT100 terminal when first turned on" should "have a blank screen" in {
      val terminal = VT100()
      terminal.getScreen() should contain theSameElementsInOrderAs Seq.fill(24)(" " * 80)
  }

  it should "have the cursor at position (1,1)" in {
      val terminal = VT100()
      terminal.getCursorX() should be (1)
      terminal.getCursorY() should be (1)
  }

  "A VT100 terminal" should "print a character on the screen" in {
      val terminal = VT100()
      terminal.sendChar('a')
      terminal.getScreen() should contain theSameElementsInOrderAs Seq("a" + " " * 79) ++ Seq.fill(23)(" " * 80)
  }

  "A VT100 terminal" should "print two characters on the screen" in {
      val terminal = VT100()
      terminal.sendChar('a')
      terminal.sendChar('b')
      terminal.getScreen() should contain theSameElementsInOrderAs Seq("ab" + " " * 78) ++ Seq.fill(23)(" " * 80)
  }
}