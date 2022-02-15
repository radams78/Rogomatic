import player.IUser
import player.User
import rogue.IRogue
import rogue.Rogue
import player.Rogomatic

@main def rgm : Unit = {
  val rogue : IRogue = new Rogue()
  val user : IUser = new User()
  val player = Rogomatic.makeTransparent(rogue, user)
  player.performNextCommand()
}