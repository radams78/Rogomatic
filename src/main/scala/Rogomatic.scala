import rogue.IRoguePlayer
import expert.IExpert

class Rogomatic(val player : IRoguePlayer, val expert : IExpert) {
  def performNextCommand() : Unit = {
    val command = expert.getCommand(player.getScreen(), player.getInventory())
    player.performCommand(command)
  }
}