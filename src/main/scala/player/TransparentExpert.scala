package player

import gamedata.inventory.Inventory
import gamedata.Command

class TransparentExpert(user : IUser) {
  def getCommand(screen : Seq[String], inventory : Inventory) : Command = {
      user.displayScreen(screen)
      user.displayInventory(inventory)
      return user.getCommand()
  }
}
