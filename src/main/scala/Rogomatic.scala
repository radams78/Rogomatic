import player.IRoguePlayer
import expert.IExpert

class Rogomatic(val player : IRoguePlayer, val expert : IExpert) {
  // @throws UnparsableInventoryLineException if inventory cannot be read
  def performNextCommand() : Unit = {
    val command = expert.getCommand(player.getScreen(), player.getInventory())
    player.performCommand(command)
  }
}