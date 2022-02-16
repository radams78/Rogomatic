import rogue.IRogue
import rogue.IRoguePlayer
import rogue.RoguePlayerFactory
import expert.IExpert
import expert.transparent.IUser
import expert.transparent.TransparentExpertFactory

class Rogomatic(val player : IRoguePlayer, val expert : IExpert) {
  def performNextCommand() : Unit = {
    val command = expert.getCommand(player.getScreen(), player.getInventory())
    player.performCommand(command)
  }
}

object Rogomatic {
    def makeTransparent(rogue : IRogue, user : IUser) : Rogomatic =
        new Rogomatic(RoguePlayerFactory.makeRoguePlayer(rogue), TransparentExpertFactory.makeExpert(user))

    def makeTransparent() : Rogomatic =
      new Rogomatic(RoguePlayerFactory.makeRoguePlayer(), TransparentExpertFactory.makeExpert())
}