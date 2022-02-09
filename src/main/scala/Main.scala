import com.pty4j.PtyProcess
import com.pty4j.PtyProcessBuilder
import collection.convert.ImplicitConversionsToJava._

import terminal.Terminal
import scala.concurrent.Future
import concurrent.ExecutionContext.Implicits.global

import player.TransparentPlayer
import player.User
import rogue.Rogue

@main def rgm : Unit = {
  val rogue = new Rogue()
  val user = new User()
  val player = TransparentPlayer(user, rogue)
}