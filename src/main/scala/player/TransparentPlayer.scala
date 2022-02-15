package player

import rogue.IRogue

// TODO Keep the game going after one turn
class TransparentPlayer(user : IUser, rogue : IRogue) {
  private val mediator : Mediator = new Mediator(new RoguePlayer(rogue), new TransparentExpert(user))

  private val command = mediator.expert.getCommand(mediator.player.getScreen(), mediator.player.getInventory())
  mediator.player.performCommand(command)
}