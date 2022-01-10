package terminal

import scala.collection.immutable.Queue
import java.time.temporal.Temporal

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

  private def performAction(action: Terminal.Action) = action match {
    case Terminal.Action.Backspace      => cursor = cursor.left()
    case Terminal.Action.Linefeed       => cursor = cursor.down()
    case Terminal.Action.CarriageReturn => cursor = Position(1, cursor.y)
    case Terminal.Action.PrintCharacter(c) =>
      screen.printChar(cursor.x, cursor.y, c)
      cursor = cursor.right()
    case Terminal.Action.CursorUp(n)      => cursor = cursor.up(n)
    case Terminal.Action.CursorDown(n)    => cursor = cursor.down(n)
    case Terminal.Action.CursorRight(n)   => cursor = cursor.right(n)
    case Terminal.Action.CursorLeft(n)    => cursor = cursor.left(n)
    case Terminal.Action.MoveCursor(x, y) => moveTo(x, y)
    case Terminal.Action.EraseToEndOfScreen =>
      screen.eraseToEndOfScreen(cursor.x, cursor.y)
    case Terminal.Action.EraseFromStartOfScreen =>
      screen.eraseFromStartOfScreen(cursor.x, cursor.y)
    case Terminal.Action.EraseScreen => screen.eraseScreen()
    case Terminal.Action.EraseToEndOfLine =>
      screen.eraseToEndOfLine(cursor.x, cursor.y)
    case Terminal.Action.EraseFromStartOfLine =>
      screen.eraseFromStartOfLine(cursor.x, cursor.y)
    case Terminal.Action.EraseLine => screen.eraseLine(cursor.y)
    case Terminal.Action.UnrecognizedSequence(seq) => {
      // DEBUG
      println("Unrecognized character sequence:")
      println(seq.mkString)
      println(seq.map(_.toInt))
    }
  }

  private def processInputBuffer(): Unit = for
    (action, tail) <- Terminal.processInputBuffer(inputBuffer)
  do {
    performAction(action)
    inputBuffer = tail
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

  private def processInputBuffer(
      inputBuffer: Queue[Char]
  ): Option[(Action, Queue[Char])] = inputBuffer.dequeue match {
    case (BS, tail) => Some(Action.Backspace, tail)
    case (LF | VT | FF, tail) =>
      Some(Action.Linefeed, tail)
    case (CR, tail)  => Some(Action.CarriageReturn, tail)
    case (ESC, tail) => parseSequenceAfterEscape(tail)
    case (c, tail) if !c.isControl =>
      Some(Action.PrintCharacter(c), tail)
    case (c, tail) => Some(Action.UnrecognizedSequence(Seq(c)), tail)
  }

  private def parseSequenceAfterEscape(
      tail: Queue[Char]
  ): Option[(Action, Queue[Char])] = tail.dequeueOption match
    case Some('[', tail) =>
      parseSequenceAfterCSI(tail, Seq('['), Seq(), 0)
    case Some(c, tail) =>
      Some(Action.UnrecognizedSequence(Seq('\u001b', c)), tail)
    case None => None

  private def parseSequenceAfterCSI(
      tail: Queue[Char],
      sequence: Seq[Char],
      parameters: Seq[Int],
      currentParameter: Int
  ): Option[(Action, Queue[Char])] = tail.dequeueOption match {
    case Some('A', tail) =>
      Some(Action.CursorUp(currentParameter.max(1)), tail)
    case Some('B', tail) =>
      Some(Action.CursorDown(currentParameter.max(1)), tail)
    case Some('C', tail) =>
      Some(Action.CursorRight(currentParameter.max(1)), tail)
    case Some('D', tail) =>
      Some(Action.CursorLeft(currentParameter.max(1)), tail)
    case Some(c, tail) if c == 'H' || c == 'f' => 
      Some(hvp(parameters :+ currentParameter, '\u001b' +: sequence :+ c), tail)
    case Some('J', tail) => Some(ep(parameters :+ currentParameter, '\u001b' +: sequence :+ 'J'), tail)
    case Some('K', tail) => Some(el(parameters :+ currentParameter, '\u001b' +: sequence :+ 'J'), tail)
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
      Some(
        Action.UnrecognizedSequence('\u001b' +: sequence :+ 'c'),
        tail
      )
    case None => None
  }

  private def hvp(parameters : Seq[Int], sequence : Seq[Char]) : Action= {
      parameters.length match {
        case 1 =>
          if parameters.last == 0 then
            Action.MoveCursor(1, 1)
          else
            Action.UnrecognizedSequence(sequence)
        case 2 => {
          val x = parameters.last.max(1)
          val y = parameters(0).max(1)
          Action.MoveCursor(x, y)
        }
        case n =>
          Action.UnrecognizedSequence(sequence)
      }
    }

  private def ep(parameters : Seq[Int], sequence : Seq[Char]) : Action = {
      if (parameters.length == 1) then
        parameters.head match {
          case 0 => Action.EraseToEndOfScreen
          case 1 => Action.EraseFromStartOfScreen
          case 2 => Action.EraseScreen
          case n => Action.UnrecognizedSequence(sequence)
        }
      else Action.UnrecognizedSequence(sequence)
    }

  private def el(parameters : Seq[Int], sequence : Seq[Char]) : Action = {
    if parameters.length == 1 then
      parameters.head match {
        case 0 => Action.EraseToEndOfLine
        case 1 => Action.EraseFromStartOfLine
        case 2 => Action.EraseLine
        case n => Action.UnrecognizedSequence(sequence)
      } else Action.UnrecognizedSequence(sequence)
  }

  enum Action {
    case Backspace
    case Linefeed
    case CarriageReturn
    case PrintCharacter(c: Char)
    case CursorUp(distance: Int)
    case CursorDown(distance: Int)
    case CursorRight(distance: Int)
    case CursorLeft(distance: Int)
    case MoveCursor(x: Int, y: Int)
    case EraseToEndOfScreen
    case EraseFromStartOfScreen
    case EraseScreen
    case EraseToEndOfLine
    case EraseFromStartOfLine
    case EraseLine
    case UnrecognizedSequence(seq: Seq[Char])
  }
}
