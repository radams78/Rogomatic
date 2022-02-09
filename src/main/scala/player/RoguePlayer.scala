package player

import rogue.IRogue
import gamedata.inventory.Slot
import gamedata.inventory.item.Item
import player.TransparentPlayer
import gamedata.inventory.Inventory
import gamedata.Command

class RoguePlayer(rogue: IRogue) {
  private val screen = rogue.getScreen()
  rogue.sendKeypress('i')
  var itemsMap = Map[Slot, Item]()
  val inventory = Inventory(
    (for (
      line <- rogue
        .getScreen()
        .takeWhile(s => !s.contains("--press space to continue--"));
      inventoryLine = TransparentPlayer.parseInventoryLine(line)
    ) yield {
      inventoryLine.slot -> inventoryLine.item
    }).toMap
  )
  rogue.sendKeypress(' ')

  def getScreen() = screen

  def getInventory() = inventory

  def performCommand(command: Command) = for (k <- command.keypresses)
    rogue.sendKeypress(k)
}
