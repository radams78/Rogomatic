package terminal

import scala.collection.immutable.Queue
import scala.annotation.meta.param

/** This terminal: * ignores tab characters * does not scroll - ignores line
  * feeds and cursor down commands when at the bottom of the screen * ignores SO
  * and SI codes * ignores XON and XOFF codes, and does not transmit them *
  * Ignores these sequences: CPR, DA, DECALN, DECDHL, DECDWL, DECID, DECKPAM,
  * DECLL, DECRC, DECREPTPARM, DECREQTPARM, DECSC, DECSTBM, DECSWL, DECTST, DSR
  */
class Terminal(x: Int = 1, 
               y: Int = 1,
               initialScreenContents : String = "") {
  private var screenContents: Array[Array[Char]] =
    initialScreenContents
    .split('\n')
    .padTo(24, "")
    .map(_.padTo(80,' ').toCharArray)
    .toArray
  private var cursorX = x
  private var cursorY = y
  private var inputBuffer: Queue[Char] = Queue()
  private var escape = 0

  def getScreen(): Seq[String] = screenContents.map(_.mkString).toSeq
  def getCursorX(): Int = cursorX
  def getCursorY(): Int = cursorY

  def receiveChar(char: Char): Unit = char match {
    case Terminal.NUL | Terminal.DEL => ()
    case c =>
      inputBuffer = inputBuffer :+ c
      inputBuffer.dequeue match
        case (Terminal.BS, tail) =>
          if cursorX > 1 then cursorX -= 1
          inputBuffer = tail
        case (Terminal.LF | Terminal.VT | Terminal.FF, tail) =>
          if cursorY < Terminal.HEIGHT then cursorY += 1
          inputBuffer = tail
        case (Terminal.CR, tail) =>
          cursorX = 1
          inputBuffer = tail
        case (Terminal.ESC, tail) => parseSequenceAfterEscape(tail)
        case (c, tail) if !c.isControl => {
          printChar(cursorX, cursorY, c)
          if (cursorX < Terminal.WIDTH) then cursorX += 1
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
      if (parameters.isEmpty) then currentParameter match {
        case 0 => {
          for x <- cursorX to Terminal.WIDTH do printChar(x, cursorY, ' ')
          for y <- cursorY + 1 to Terminal.HEIGHT do eraseLine(y)
        }
        case 1 => {
          for y <- 1 until cursorY do eraseLine(y)
          for x <- 1 to cursorX do printChar(x, cursorY, ' ')
        }
        case 2 => {
          for y <- 1 to Terminal.HEIGHT do eraseLine(y)
        }
        case n => {
          // DEBUG
          println(s"Illegal control sequence: ESC ${sequence.mkString}J")
        }
      }
      else println(s"Illegal control sequence: ESC ${sequence.mkString}J")
      inputBuffer = tail
    }
    case Some('K', tail) =>
      for x <- cursorX to Terminal.WIDTH do printChar(x, cursorY, ' ')
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
    case Some(c, tail) => println(s"Unrecognized escape sequence: ESC + ${sequence.mkString}$c")
    case None => ()

    private def printChar(x : Int, y : Int, char : Char) : Unit = {
      assert(1 <= x)
      assert(x <= Terminal.WIDTH)
      assert(1 <= y)
      assert(y <= Terminal.HEIGHT)
      screenContents(y - 1)(x - 1) = char
    }

    private def moveUp(n : Int = 1) : Unit = {
      cursorY = (cursorY - n).max(1)
    }

    private def moveDown(n : Int = 1) : Unit = {
      cursorY = (cursorY + n).min(Terminal.HEIGHT)
    }

    private def moveRight(n : Int = 1) : Unit = {
      cursorX = (cursorX + n).min(Terminal.WIDTH)
    }

    private def moveLeft(n : Int = 1) : Unit = {
      cursorX = (cursorX - n).max(1)
    }

    // Ignores invalid positions
    private def moveTo(x : Int, y : Int) : Unit = {
      if 1 <= x && x <= Terminal.WIDTH && 1 <= y && y <= Terminal.HEIGHT
      then {
        cursorX = x
        cursorY = y
      }
      else {
        // DEBUG
        println(s"Illegal position: $x,$y")
      }
    }

    private def eraseLine(y : Int) : Unit = {
      if 1 <= y && y <= Terminal.HEIGHT
      then 
        for x <- 1 to Terminal.WIDTH do printChar(x, y, ' ')
      else println(s"Illegal y-position: $y")

    }
}

object Terminal {
  private val HEIGHT = 24
  private val WIDTH = 80
  private val NUL = '\u0000'
  private val BS = '\u0008'
  private val DEL = '\u007f'
  private val LF = '\n' // same as

  private val VT = '\u000b'
  private val FF = '\u000c'
  private val CR = '\u000d'
  private val ESC = '\u001b'
}
