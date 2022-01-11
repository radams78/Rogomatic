import com.pty4j.PtyProcess
import com.pty4j.PtyProcessBuilder
import collection.convert.ImplicitConversionsToJava._

import terminal.Terminal
import scala.concurrent.Future
import concurrent.ExecutionContext.Implicits.global

@main def rgm : Unit = {
  val rogue = Rogue()
  val user = User()
  val player = TransparentPlayer(user, rogue)
}