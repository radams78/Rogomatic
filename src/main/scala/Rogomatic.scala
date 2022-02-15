package player

import rogue.IRogue
import rogue.IRoguePlayer
import rogue.RoguePlayerFactory

class Rogomatic(val player : IRoguePlayer, val expert : TransparentExpert) {
  def performNextCommand() : Unit = {
    val command = expert.getCommand(player.getScreen(), player.getInventory())
    player.performCommand(command)
  }
}

object Rogomatic {
    def makeTransparent(rogue : IRogue, user : IUser) : Rogomatic =
        new Rogomatic(RoguePlayerFactory.makeRoguePlayer(rogue), new TransparentExpert(user))
}