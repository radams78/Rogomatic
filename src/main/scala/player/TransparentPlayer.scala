package player

import rogue.IRogue

// TODO Keep the game going after one turn
class TransparentPlayer(user : IUser, rogue : IRogue, mediator : Mediator) {
  private val command = mediator.expert.getCommand(mediator.player.getScreen(), mediator.player.getInventory())
  mediator.player.performCommand(command)
}

object TransparentPlayer {
  def apply(user : IUser, rogue : IRogue) : TransparentPlayer = 
    new TransparentPlayer(user, rogue, new Mediator(new RoguePlayer(rogue), new TransparentExpert(user)))
}