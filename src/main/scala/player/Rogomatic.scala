package player

import rogue.IRogue

class Rogomatic(val player : RoguePlayer, val expert : TransparentExpert) {
  def performNextCommand() : Unit = {
    val command = expert.getCommand(player.getScreen(), player.getInventory())
    player.performCommand(command)
  }
}

object Rogomatic {
    def makeTransparent(rogue : IRogue, user : IUser) : Rogomatic =
        new Rogomatic(new RoguePlayer(rogue), new TransparentExpert(user))
}