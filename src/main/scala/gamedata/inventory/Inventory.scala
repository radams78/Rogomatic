package gamedata.inventory

import item.Item

final case class Inventory(items : Map[Slot, Item]) {
    override def toString : String = items.toList
        .sortWith({case ((Slot(x),_),(Slot(y),_)) => x <= y})
        .map((slot, item) => s"$slot) $item")
        .mkString("\n")
}