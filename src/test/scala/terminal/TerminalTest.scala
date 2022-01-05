package terminal

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class TerminalTest extends AnyFlatSpec with should.Matchers {
  "A VT100 display" should "have a blank screen when first turned on" in {
    val terminal = Terminal()
    terminal.getScreen() should contain theSameElementsInOrderAs Seq.fill(24)(
      " " * 80
    )
  }

  it should "have the cursor at position (1,1) when first turned on" in {
    val terminal = Terminal()
    terminal.getCursorX() should be(1)
    terminal.getCursorY() should be(1)
  }

  it should "print a character on the screen" in {
    val terminal = Terminal()
    terminal.receiveChar('a')
    terminal.getScreen() should contain theSameElementsInOrderAs Seq(
      "a" + " " * 79
    ) ++ Seq.fill(23)(" " * 80)
    terminal.getCursorX() should be(2)
    terminal.getCursorY() should be(1)
  }

  it should "print two characters on the screen" in {
    val terminal = Terminal()
    terminal.receiveChar('a')
    terminal.receiveChar('b')
    terminal.getScreen() should contain theSameElementsInOrderAs Seq(
      "ab" + " " * 78
    ) ++ Seq.fill(23)(" " * 80)
    terminal.getCursorX() should be(3)
    terminal.getCursorY() should be(1)
  }

  it should "when it reaches the final column, overwrite the last character" in {
    val terminal = Terminal()
    for i <- 1 to 80 do terminal.receiveChar('a')
    terminal.receiveChar('b')
    terminal.getScreen() should contain theSameElementsInOrderAs Seq(
      "a" * 79 + "b"
    ) ++ Seq.fill(23)(" " * 80)
    terminal.getCursorX() should be(80)
    terminal.getCursorY() should be(1)
  }

  it should "ignore a NUL character" in {
    val terminal = Terminal()
    terminal.receiveChar('\u0000')
    terminal.getScreen() should contain theSameElementsInOrderAs Seq.fill(24)(
      " " * 80
    )
    terminal.getCursorX() should be(1)
    terminal.getCursorY() should be(1)
  }

  it should "ignore a DEL character" in {
    val terminal = Terminal()
    terminal.receiveChar('\u007f')
    terminal.getScreen() should contain theSameElementsInOrderAs Seq.fill(24)(
      " " * 80
    )
    terminal.getCursorX() should be(1)
    terminal.getCursorY() should be(1)
  }

  // TODO ENQ character

  // TODO BEL character

  it should "when given a BS character, move the cursor left" in {
    val terminal = Terminal(23, 14)
    terminal.receiveChar('\u0008')
    terminal.getCursorX() should be(22)
    terminal.getCursorY() should be(14)
  }

  it should "when given a BS character while cursor is at the left margin, do nothing" in {
    val terminal = Terminal(1, 14)
    terminal.receiveChar('\u0008')
    terminal.getCursorX() should be(1)
    terminal.getCursorY() should be(14)
  }

  it should "when given an LF character, move the cursor down" in {
    val terminal = Terminal(23, 14)
    terminal.receiveChar('\n')
    terminal.getCursorX() should be(23)
    terminal.getCursorY() should be(15)
  }

  it should "when given a VT character, move the cursor down" in {
    val terminal = Terminal(23, 14)
    terminal.receiveChar('\u000b')
    terminal.getCursorX() should be(23)
    terminal.getCursorY() should be(15)
  }

  it should "when given an FF character, move the cursor down" in {
    val terminal = Terminal(23, 14)
    terminal.receiveChar('\u000c')
    terminal.getCursorX() should be(23)
    terminal.getCursorY() should be(15)
  }

  it should "when given a CR character, move to the left margin" in {
    val terminal = Terminal(23, 14)
    terminal.receiveChar('\u000d')
    terminal.getCursorX() should be(1)
    terminal.getCursorY() should be(14)
  }

  // TODO CAN and SUB codes
  it should "when given a CUB sequence with no parameter, move the cursor one space left" in {
    val terminal = Terminal(23, 14)
    terminal.receiveChar('\u001b')
    terminal.receiveChar('[')
    terminal.receiveChar('D')
    terminal.getCursorX() should be(22)
    terminal.getCursorY() should be(14)
  }

  it should "when given a CUB sequence, move the cursor multiple spaces left" in {
    val terminal = Terminal(23, 14)
    terminal.receiveChar('\u001b')
    terminal.receiveChar('[')
    terminal.receiveChar('3')
    terminal.receiveChar('D')
    terminal.getCursorX() should be(20)
    terminal.getCursorY() should be(14)
  }

  it should "when given a CUB sequence with two digits, move the cursor multiple spaces left" in {
    val terminal = Terminal(23, 14)
    terminal.receiveChar('\u001b')
    terminal.receiveChar('[')
    terminal.receiveChar('1')
    terminal.receiveChar('3')
    terminal.receiveChar('D')
    terminal.getCursorX() should be(10)
    terminal.getCursorY() should be(14)
  }

  it should "when given a CUD sequence with default parameter, move the cursor one line down" in {
    val terminal = Terminal(23, 14)
    terminal.receiveChar('\u001b')
    terminal.receiveChar('[')
    terminal.receiveChar('B')
    terminal.getCursorX() should be(23)
    terminal.getCursorY() should be(15)
  }

  it should "when given a CUD sequence when at the bottom of the screen, do nothing" in {
    val terminal = Terminal(23, 24)
    terminal.receiveChar('\u001b')
    terminal.receiveChar('[')
    terminal.receiveChar('B')
    terminal.getCursorX() should be(23)
    terminal.getCursorY() should be(24)
  }

  it should "when given a CUF sequence, move the cursor right" in {
    val terminal = Terminal(23, 14)
    terminal.receiveChar('\u001b')
    terminal.receiveChar('[')
    terminal.receiveChar { '7' }
    terminal.receiveChar('C')
    terminal.getCursorX() should be(30)
    terminal.getCursorY() should be(14)
  }

  it should "when given a CUP sequence with default parameter, move the cursor home" in {
    val terminal = Terminal(23, 14)
    terminal.receiveChar('\u001b')
    terminal.receiveChar('[')
    terminal.receiveChar('H')
    terminal.getCursorX() should be(1)
    terminal.getCursorY() should be(1)
  }

  it should "when given a CUP sequence, move the cursor to the specified position" in {
    val terminal = Terminal(23, 14)
    terminal.receiveChar('\u001b')
    terminal.receiveChar('[')
    terminal.receiveChar('7')
    terminal.receiveChar(';')
    terminal.receiveChar('1')
    terminal.receiveChar('6')
    terminal.receiveChar('H')
    terminal.getCursorX() should be(16)
    terminal.getCursorY() should be(7)
  }

  it should "ignore a CUP sequence with invalid parameters" in {
    val terminal = Terminal(23, 14)
    terminal.receiveChar('\u001b')
    terminal.receiveChar('[')
    terminal.receiveChar('9')
    terminal.receiveChar('9')
    terminal.receiveChar(';')
    terminal.receiveChar('9')
    terminal.receiveChar('9')
    terminal.receiveChar('H')
    terminal.getCursorX() should be(23)
    terminal.getCursorY() should be(14)
  }

  it should "interpret a CUU sequence" in {
    val terminal = Terminal(23, 14)
    terminal.receiveChar('\u001b')
    terminal.receiveChar('[')
    terminal.receiveChar('9')
    terminal.receiveChar('A')
    terminal.getCursorX() should be(23)
    terminal.getCursorY() should be(5)
  }

  it should "when given an ED sequence with no parameter, erase to the end of the screen" in {
    val terminal : Terminal = Terminal(10, 10,
      (("abcdefghijklmnopqrstuvwxyz".padTo(80,' ')) + '\n') * 24
    )
    terminal.receiveChar('\u001b')
    terminal.receiveChar('[')
    terminal.receiveChar('J')
    terminal.getScreen() should contain theSameElementsInOrderAs (
      (Seq.fill(9)("abcdefghijklmnopqrstuvwxyz".padTo(80,' '))
      :+ "abcdefghi".padTo(80,' '))
      ++ Seq.fill(14)(" " * 80)
    )
  }

  it should "when given an ED sequence with parameter 1, erase to the start of the screen" in {
    val terminal : Terminal = Terminal(10, 10,
      (("abcdefghijklmnopqrstuvwxyz".padTo(80,' ')) + '\n') * 24
    )
    terminal.receiveChar('\u001b')
    terminal.receiveChar('[')
    terminal.receiveChar('1')
    terminal.receiveChar('J')
    terminal.getScreen() should contain theSameElementsInOrderAs (
      (Seq.fill(9)(" " * 80)
      :+ "          klmnopqrstuvwxyz".padTo(80, ' '))
      ++ Seq.fill(14)("abcdefghijklmnopqrstuvwxyz".padTo(80,' '))
    )
  }

  it should "when given an ED sequence with parameter 2, clear the screen" in {
    val terminal : Terminal = Terminal(10, 10,
      (("abcdefghijklmnopqrstuvwxyz".padTo(80,' ')) + '\n') * 24
    )
    terminal.receiveChar('\u001b')
    terminal.receiveChar('[')
    terminal.receiveChar('2')
    terminal.receiveChar('J')
    terminal.getScreen() should contain theSameElementsInOrderAs Seq.fill(24)(" " * 80)
  }


  it should "when given an EL sequence with no parameter, erase to the end of the line" in {
    val terminal : Terminal = Terminal(10, 10,
      (("abcdefghijklmnopqrstuvwxyz".padTo(80,' ')) + '\n') * 24
    )
    terminal.receiveChar('\u001b')
    terminal.receiveChar('[')
    terminal.receiveChar('K')
    terminal.getScreen() should contain theSameElementsInOrderAs (
      (Seq.fill(9)("abcdefghijklmnopqrstuvwxyz".padTo(80,' '))
      :+ "abcdefghi".padTo(80,' '))
      ++ Seq.fill(14)("abcdefghijklmnopqrstuvwxyz".padTo(80,' '))
    )
  }
}
