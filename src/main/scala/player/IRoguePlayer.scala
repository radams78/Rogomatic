package player

import gamedata.inventory.Inventory
import gamedata.Command

trait IRoguePlayer {
  def getScreen() : Seq[String]
  def getInventory() : Inventory
  def performCommand(command : Command) : Unit
}
