package player

import rogue.IRogue

class Mediator(val player : RoguePlayer, val expert : TransparentExpert) {
  def performNextCommand() : Unit = {
    val command = expert.getCommand(player.getScreen(), player.getInventory())
    player.performCommand(command)
  }
}

object Mediator {
    def apply(rogue : IRogue, user : IUser) : Mediator =
        new Mediator(new RoguePlayer(rogue), new TransparentExpert(user))
}