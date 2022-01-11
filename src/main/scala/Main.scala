import com.pty4j.PtyProcess
import com.pty4j.PtyProcessBuilder
import collection.convert.ImplicitConversionsToJava._

import terminal.Terminal
import scala.concurrent.Future
import concurrent.ExecutionContext.Implicits.global

@main def rgm : Unit = {
  val cmd = Array("/usr/games/rogue")
  val env = Map("TERM" -> "vt100")
  val process = new PtyProcessBuilder()
    .setCommand(cmd)
    .setEnvironment(env)
    .start()

  val os = process.getOutputStream
  val is = process.getInputStream

  val terminal = Terminal()
  Future{
    while(true) {
      terminal.receiveChar(is.read.toChar)
    }
  }
  Thread.sleep(1000)
  println(terminal.getScreen().mkString("\n"))

  process.destroy()
}