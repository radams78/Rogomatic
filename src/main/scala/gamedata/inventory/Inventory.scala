package gamedata.inventory

import item.Item

final case class Inventory(items : Map[Slot, Item])