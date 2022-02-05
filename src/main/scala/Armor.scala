case class Armor(armorType : ArmorType, bonus : Bonus) extends Item

object Armor{
    def apply(armorType: ArmorType, bonus : Int) : Armor = apply(armorType, Bonus(bonus))
}