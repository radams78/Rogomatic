package expert.transparent

import gamedata.Command
import gamedata.inventory.Inventory

trait IUser {
  def displayScreen(screen : Seq[String]) : Unit

  def displayInventory(inventory : Inventory) : Unit

  def getCommand() : Command
}
