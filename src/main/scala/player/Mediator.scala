package player

class Mediator(val player : RoguePlayer, val expert : TransparentExpert) {
  def performNextCommand() : Unit = {
    val command = expert.getCommand(player.getScreen(), player.getInventory())
    player.performCommand(command)
  }
}
