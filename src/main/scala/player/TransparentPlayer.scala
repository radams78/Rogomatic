package player

import rogue.IRogue

// TODO Keep the game going after one turn
class TransparentPlayer(user : IUser, rogue : IRogue) {
  private val player : RoguePlayer = new RoguePlayer(rogue)
  private val expert = new TransparentExpert(user)
  private val mediator : Mediator = new Mediator(player)

  private val command = expert.getCommand(mediator.player.getScreen(), player.getInventory())
  player.performCommand(command)
}