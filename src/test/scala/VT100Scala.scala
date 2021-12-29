import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class VT100Scala extends AnyFlatSpec with should.Matchers {
  "A VT100 terminal" should "have a blank screen when first turned on" in {
      val terminal = VT100()
      terminal.getScreen() should contain theSameElementsInOrderAs Seq.fill(24)(" " * 80)
  }
}
