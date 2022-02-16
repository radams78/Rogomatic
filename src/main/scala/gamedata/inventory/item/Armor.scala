package gamedata.inventory.item

// TODO Unidentified Armor
final case class Armor(armorType : ArmorType, bonus : Bonus) extends Item {
    override def toString : String = s"$bonus $armorType"
}

object Armor{
    def apply(armorType: ArmorType, bonus : Int) : Armor = apply(armorType, Bonus(bonus))
}