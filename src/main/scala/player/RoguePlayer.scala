package player

import gamedata.inventory.Inventory
import gamedata.Command
import rogue.IRogue

import player.Parser

// TODO Read screen and inventory after performing command
private[player] class RoguePlayer(rogue: IRogue) extends IRoguePlayer {
  private val screen = rogue.getScreen()
  private val inventory = readInventoryScreen()

  def getScreen() = screen

  def getInventory() = inventory

  def performCommand(command: Command) = 
    for (k <- command.keypresses)
      rogue.sendKeypress(k)

  // @throws UnparsableInventoryLineException if inventory screen cannot be read
  private def readInventoryScreen(): Inventory = {
    summonInventoryScreen()
    val inventory = Parser.parseInventoryScreen(rogue.getScreen())
    dismissInventoryScreen()
    return inventory
  }

  private def summonInventoryScreen() = rogue.sendKeypress('i')

  private def dismissInventoryScreen() = rogue.sendKeypress(' ')
}