import collection.convert.ImplicitConversionsToJava._
import scala.concurrent.Future
import concurrent.ExecutionContext.Implicits.global

import com.pty4j.PtyProcessBuilder

import terminal.Terminal

class Rogue extends IRogue {
  private val process = new PtyProcessBuilder()
    .setCommand(Rogue.cmd)
    .setEnvironment(Rogue.env)
    .start()

  private val os = process.getOutputStream
  private val is = process.getInputStream

  private val terminal = Terminal()
  Future {
    while (true) {
      terminal.receiveChar(is.read.toChar)
    }
  }
  Thread.sleep(1000)

  override def getScreen(): Seq[String] = terminal.getScreen()

  override def sendKeypress(keypress : Char) : Unit = os.write(keypress.toInt)
}

private object Rogue {
  private val cmd = Array("/usr/games/rogue")
  private val env = Map("TERM" -> "vt100")
}