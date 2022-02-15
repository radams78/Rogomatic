package player

import rogue.IRogue

class TransparentPlayer(user : IUser, rogue : IRogue) {
  private val player : RoguePlayer = new RoguePlayer(rogue)
  private val expert = new TransparentExpert(user)

  private val command = expert.getCommand(player.getScreen(), player.getInventory())
  player.performCommand(command)
}