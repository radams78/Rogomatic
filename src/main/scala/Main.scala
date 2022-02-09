import player.TransparentPlayer
import player.IUser
import player.User
import rogue.IRogue
import rogue.Rogue

@main def rgm : Unit = {
  val rogue : IRogue = new Rogue()
  val user : IUser = new User()
  val player = TransparentPlayer(user, rogue)
}