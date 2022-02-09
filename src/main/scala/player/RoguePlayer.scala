package player

import rogue.IRogue
import gamedata.inventory.Slot
import gamedata.inventory.item.Item
import player.TransparentPlayer
import gamedata.inventory.Inventory

class RoguePlayer(rogue : IRogue) {
    private val screen = rogue.getScreen()
    rogue.sendKeypress('i')
    var itemsMap = Map[Slot,Item]()
    for (line <- rogue.getScreen().takeWhile(s => ! s.contains("--press space to continue--"))) do {
        val inventoryLine = TransparentPlayer.parseInventoryLine(line)
        itemsMap = itemsMap.updated(inventoryLine.slot, inventoryLine.item)
    }
    val inventory = Inventory(itemsMap)
    rogue.sendKeypress(' ')

    def getScreen() = screen

    def getInventory() = inventory
}
