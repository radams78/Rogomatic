package player

import gamedata.inventory.Inventory
import gamedata.Command

// TODO Throw UnparsableInventoryLineException in the right place
trait IRoguePlayer {
  def getScreen() : Seq[String]
  def getInventory() : Inventory
  def performCommand(command : Command) : Unit
}
