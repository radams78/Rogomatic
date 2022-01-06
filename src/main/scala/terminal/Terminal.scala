package terminal

import scala.collection.immutable.Queue

/** This terminal: * ignores tab characters * does not scroll - ignores line
  * feeds and cursor down commands when at the bottom of the screen * ignores SO
  * and SI codes * ignores XON and XOFF codes, and does not transmit them *
  * Ignores these sequences: CPR, DA, DECALN, DECDHL, DECDWL, DECID, DECKPAM,
  * DECLL, DECRC, DECREPTPARM, DECREQTPARM, DECSC, DECSTBM, DECSWL, DECTST, DSR
  */
class Terminal(x: Int = 1, y: Int = 1, initialScreenContents: String = "") {
  private var cursor: Position = Position(x, y)
  private var inputBuffer: Queue[Char] = Queue()
  private var screen: Screen = Screen(initialScreenContents)

  def getScreen(): Seq[String] = screen.getContents()
  def getCursorX(): Int = cursor.x
  def getCursorY(): Int = cursor.y

  def receiveChar(char: Char): Unit = char match {
    case Terminal.NUL | Terminal.DEL => ()
    case c =>
      inputBuffer = inputBuffer :+ c
      processInputBuffer()
  }

  private def processInputBuffer(): Unit = {
    inputBuffer.dequeue match
      case (Terminal.BS, tail) =>
        cursor = cursor.left()
        inputBuffer = tail
      case (Terminal.LF | Terminal.VT | Terminal.FF, tail) =>
        cursor = cursor.down()
        inputBuffer = tail
      case (Terminal.CR, tail) =>
        cursor = Position(1, cursor.y)
        inputBuffer = tail
      case (Terminal.ESC, tail) => parseSequenceAfterEscape(tail)
      case (c, tail) if !c.isControl => {
        screen.printChar(cursor.x, cursor.y, c)
        cursor = cursor.right()
        inputBuffer = tail
      }
      case (c, tail) => throw new Error("Unrecognized character " + c)
  }

  private def parseSequenceAfterEscape(tail: Queue[Char]): Unit =
    tail.dequeueOption match
      case Some('[', tail) => parseSequenceAfterCSI(tail, Seq('['), Seq(), 0)
      case Some(c, tail) =>
        throw new Error("Unrecognized escape sequence: ESC + " + c)
      case None => ()

  private def parseSequenceAfterCSI(
      tail: Queue[Char],
      sequence: Seq[Char],
      parameters: Seq[Int],
      currentParameter: Int
  ): Unit = tail.dequeueOption match
    case Some('A', tail) =>
      moveUp(currentParameter.max(1))
      inputBuffer = tail
    case Some('B', tail) =>
      moveDown(currentParameter.max(1))
      inputBuffer = tail
    case Some('C', tail) =>
      moveRight(currentParameter.max(1))
      inputBuffer = tail
    case Some('D', tail) =>
      moveLeft(currentParameter.max(1))
      inputBuffer = tail
    case Some('H', tail) => {
      parameters.length match {
        case 0 =>
          if currentParameter == 0 then moveTo(1, 1)
        case 1 => {
          val x = currentParameter.max(1)
          val y = parameters(0).max(1)
          moveTo(x, y)
        }
        case n => {
          // DEBUG
          println(s"Illegal command sequence: ESC ${sequence.mkString}H")
        }
      }
      inputBuffer = tail
    }
    case Some('J', tail) => {
      if (parameters.isEmpty) then
        currentParameter match {
          case 0 => screen.eraseToEndOfScreen(cursor.x, cursor.y)
          case 1 => screen.eraseFromStartOfScreen(cursor.x, cursor.y)
          case 2 => screen.eraseScreen()
          case n => {
            // DEBUG
            println(s"Illegal control sequence: ESC ${sequence.mkString}J")
          }
        }
      else println(s"Illegal control sequence: ESC ${sequence.mkString}J")
      inputBuffer = tail
    }
    case Some('K', tail) =>
      currentParameter match {
        case 0 => screen.eraseToEndOfLine(cursor.x, cursor.y)
        case 1 => screen.eraseFromStartOfLine(cursor.x, cursor.y)
        case 2 => screen.eraseLine(cursor.y)
        case n => {
          // DEBUG
          println(s"Illegal control sequence: ESC ${sequence.mkString}K")
        }
      }
      inputBuffer = tail
    case Some(';', tail) =>
      parseSequenceAfterCSI(
        tail,
        sequence :+ ';',
        parameters :+ currentParameter,
        0
      )
    case Some(n, tail) if n.isDigit =>
      parseSequenceAfterCSI(
        tail,
        sequence :+ n,
        parameters,
        10 * currentParameter + n.asDigit
      )
    case Some(c, tail) =>
      println(s"Unrecognized escape sequence: ESC + ${sequence.mkString}$c")
    case None => ()

  private def moveUp(n: Int = 1): Unit = {
    cursor = cursor.up(n)
  }

  private def moveDown(n: Int = 1): Unit = {
    cursor = cursor.down(n)
  }

  private def moveRight(n: Int = 1): Unit = {
    cursor = cursor.right(n)
  }

  private def moveLeft(n: Int = 1): Unit = {
    cursor = cursor.left(n)
  }

  // Ignores invalid positions
  private def moveTo(x: Int, y: Int): Unit = {
    if 1 <= x && x <= Terminal.WIDTH && 1 <= y && y <= Terminal.HEIGHT then {
      cursor = Position(x, y)
    } else {
      // DEBUG
      println(s"Illegal position: $x,$y")
    }
  }
}

object Terminal {
  val HEIGHT = 24
  val WIDTH = 80
  private val NUL = '\u0000'
  private val BS = '\u0008'
  private val DEL = '\u007f'
  private val LF = '\n' // same as

  private val VT = '\u000b'
  private val FF = '\u000c'
  private val CR = '\u000d'
  private val ESC = '\u001b'
}
