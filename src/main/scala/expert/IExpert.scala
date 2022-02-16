package expert

import gamedata.inventory.Inventory
import gamedata.Command

trait IExpert {
  def getCommand(screen : Seq[String], inventory: Inventory): Command
}
