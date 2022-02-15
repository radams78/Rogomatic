package player

import rogue.IRogue

// TODO Keep the game going after one turn
class TransparentPlayer(user : IUser, rogue : IRogue) {
  private val expert = new TransparentExpert(user)
  private val mediator : Mediator = new Mediator(new RoguePlayer(rogue))

  private val command = expert.getCommand(mediator.player.getScreen(), mediator.player.getInventory())
  mediator.player.performCommand(command)
}