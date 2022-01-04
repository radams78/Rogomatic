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
    terminal.sendChar('a')
    terminal.getScreen() should contain theSameElementsInOrderAs Seq(
      "a" + " " * 79
    ) ++ Seq.fill(23)(" " * 80)
    terminal.getCursorX() should be(2)
    terminal.getCursorY() should be(1)
  }

  it should "print two characters on the screen" in {
    val terminal = Terminal()
    terminal.sendChar('a')
    terminal.sendChar('b')
    terminal.getScreen() should contain theSameElementsInOrderAs Seq(
      "ab" + " " * 78
    ) ++ Seq.fill(23)(" " * 80)
    terminal.getCursorX() should be(3)
    terminal.getCursorY() should be(1)
  }

  it should "when it reaches the final column, overwrite the last character" in {
    val terminal = Terminal()
    for i <- 1 to 80 do terminal.sendChar('a')
    terminal.sendChar('b')
    terminal.getScreen() should contain theSameElementsInOrderAs Seq(
      "a" * 79 + "b"
    ) ++ Seq.fill(23)(" " * 80)
    terminal.getCursorX() should be(80)
    terminal.getCursorY() should be(1)
  }

  it should "ignore a NUL character" in {
    val terminal = Terminal()
    terminal.sendChar('\u0000')
    terminal.getScreen() should contain theSameElementsInOrderAs Seq.fill(24)(
      " " * 80
    )
    terminal.getCursorX() should be(1)
    terminal.getCursorY() should be(1)
  }

  it should "ignore a DEL character" in {
    val terminal = Terminal()
    terminal.sendChar('\u007f')
    terminal.getScreen() should contain theSameElementsInOrderAs Seq.fill(24)(
      " " * 80
    )
    terminal.getCursorX() should be(1)
    terminal.getCursorY() should be(1)
  }

  // TODO ENQ character

  // TODO BEL character

/*  it should "when given a BS character, move the cursor left" in {
    val terminal = VT100(23, 14)
    terminal.sendChar(VT100.BS)
    terminal.getCursorX() should be(22)
    terminal.getCursorY() should be(14)
  }

  it should "when given a BS character while cursor is at the left margin, do nothing" in {
    val terminal = VT100(1, 14)
    terminal.sendChar(VT100.BS)
    terminal.getCursorX() should be(1)
    terminal.getCursorY() should be(14)
  }

  // TODO HT char

  it should "when given an LF character, move the cursor down" in {
    val terminal = VT100(23, 14)
    terminal.sendChar(VT100.LF)
    terminal.getCursorX() should be(23)
    terminal.getCursorY() should be(15)
  }

  it should "when given an LF character at the bottom of the screen, scroll the screen" in {
    val terminal = VT100(
      1,
      24,
      """a
        |b
        |c
        |d
        |e
        |f
        |g
        |h
        |i
        |j
        |k
        |l
        |m
        |n
        |o
        |p
        |q
        |r
        |s
        |t
        |u
        |v
        |w
        |x""".stripMargin
    )
    terminal.sendChar(VT100.LF)
    terminal.getCursorX() should be(1)
    terminal.getCursorY() should be(24)
    terminal.getScreen() should contain theSameElementsInOrderAs (
      """b
            |c
            |d
            |e
            |f
            |g
            |h
            |i
            |j
            |k
            |l
            |m
            |n
            |o
            |p
            |q
            |r
            |s
            |t
            |u
            |v
            |w
            |x
            | """.stripMargin
        .split('\n')
        .map(_.padTo(80, ' '))
    )
  }

  it should "when given a VT character, move the cursor down" in {
    val terminal = VT100(23, 14)
    terminal.sendChar(VT100.VT)
    terminal.getCursorX() should be(23)
    terminal.getCursorY() should be(15)
  }

  it should "when given an FF character, move the cursor down" in {
    val terminal = VT100(23, 14)
    terminal.sendChar(VT100.FF)
    terminal.getCursorX() should be(23)
    terminal.getCursorY() should be(15)
  }

  it should "when given a CR character, move to the left margin" in {
    val terminal = VT100(23, 14)
    terminal.sendChar(VT100.CR)
    terminal.getCursorX() should be(1)
    terminal.getCursorY() should be(14)
  }

  // todo SO and SI chars etc.
  // todo CPR sequence

  it should "when given a CUB sequence with no parameter, move the cursor one space left" in {
    val terminal = VT100(23, 14)
    terminal.sendChar(VT100.ESC)
    terminal.sendChar('[')
    terminal.sendChar('D')
    terminal.getCursorX() should be(22)
    terminal.getCursorY() should be(14)
  }

  it should "when given a CUB sequence, move the cursor multiple spaces left" in {
    val terminal = VT100(23, 14)
    terminal.sendChar(VT100.ESC)
    terminal.sendChar('[')
    terminal.sendChar('3')
    terminal.sendChar('D')
    terminal.getCursorX() should be(20)
    terminal.getCursorY() should be(14)
  }

  it should "when given a CUB sequence with two digits, move the cursor multiple spaces left" in {
    val terminal = VT100(23, 14)
    terminal.sendChar(VT100.ESC)
    terminal.sendChar('[')
    terminal.sendChar('1')
    terminal.sendChar('3')
    terminal.sendChar('D')
    terminal.getCursorX() should be(10)
    terminal.getCursorY() should be(14)
  }

  it should "when given a CUD sequence with default parameter, move the cursor one line down" in {
    val terminal = VT100(23, 14)
    terminal.sendChar(VT100.ESC)
    terminal.sendChar('[')
    terminal.sendChar('B')
    terminal.getCursorX() should be(23)
    terminal.getCursorY() should be(15)
  }

  it should "when given a CUD sequence when at the bottom of the screen, do nothing" in {
    val terminal = VT100(23, 24)
    terminal.sendChar(VT100.ESC)
    terminal.sendChar('[')
    terminal.sendChar('B')
    terminal.getCursorX() should be(23)
    terminal.getCursorY() should be(24)
  }

  it should "when given a CUF sequence, move the cursor right" in {
    val terminal = VT100(23, 14)
    terminal.sendChar(VT100.ESC)
    terminal.sendChar('[')
    terminal.sendChar { '7' }
    terminal.sendChar('C')
    terminal.getCursorX() should be(30)
    terminal.getCursorY() should be(14)
  }

  it should "when given a CUP sequence with default parameter, move the cursor home" in {
    val terminal = VT100(23, 14)
    terminal.sendChar(VT100.ESC)
    terminal.sendChar('[')
    terminal.sendChar('H')
    terminal.getCursorX() should be(1)
    terminal.getCursorY() should be(1)
  }

  it should "when given a CUP sequence, move the cursor to the specified position" in {
    val terminal = VT100(23, 14)
    terminal.sendChar(VT100.ESC)
    terminal.sendChar('[')
    terminal.sendChar('7')
    terminal.sendChar(';')
    terminal.sendChar('1')
    terminal.sendChar('6')
    terminal.sendChar('H')
    terminal.getCursorX() should be(16)
    terminal.getCursorY() should be(7)
  }

  it should "ignore a CUP sequence with invalid parameters" in {
    val terminal = VT100(23, 14)
    terminal.sendChar(VT100.ESC)
    terminal.sendChar('[')
    terminal.sendChar('9')
    terminal.sendChar('9')
    terminal.sendChar(';')
    terminal.sendChar('9')
    terminal.sendChar('9')
    terminal.sendChar('H')
    terminal.getCursorX() should be(23)
    terminal.getCursorY() should be(14)
  }

  it should "interpret a CUU sequence" in {
    val terminal = VT100(23, 14)
    terminal.sendChar(VT100.ESC)
    terminal.sendChar('[')
    terminal.sendChar('9')
    terminal.sendChar('A')
    terminal.getCursorX() should be(23)
    terminal.getCursorY() should be(5)
  }

  it should "when a letter key is pressed, transmit the appropriate code to the host" in {
    object MockHost extends IHost {
        var receivedSignal = false

        override def sendChar(char : Char) : Unit = {
            char should be ('A')
            receivedSignal = true
        }
    }
    val terminal = VT100(MockHost)
    terminal.pressWithShift(Key.Letter('a'))
    MockHost should be (Symbol("receivedSignal"))
  }
  // todo Input buffer overflow

  // todo DECALN
  // todo DECDHL
  // todo DECDWL
  // todo DECID
  // todo DECKPAM
  // todo DECKPNM
  // todo DECLL ETC */
}
