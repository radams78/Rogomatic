import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class VT100Scala extends AnyFlatSpec with should.Matchers {
  "A VT100 terminal" should "have a blank screen when first turned on" in {
      val terminal = VT100()
      terminal.getScreen() should contain theSameElementsInOrderAs Seq.fill(24)(" " * 80)
  }

  "A VT100 terminal" should "print a character on the screen" in {
      val terminal = VT100()
      terminal.sendChar('a')
      terminal.getScreen() should contain theSameElementsInOrderAs Seq("a" + " " * 79) ++ Seq.fill(23)(" " * 80)
  }
}
