import expert.transparent.IUser
import expert.transparent.User
import rogue.IRogue
import rogue.Rogue

@main def rgm : Unit = {
  val rogue : IRogue = new Rogue()
  val user : IUser = new User()
  val player = Rogomatic.makeTransparent(rogue, user)
  player.performNextCommand()
}