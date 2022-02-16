package gamedata.inventory.item

final case class Bonus(value : Int) {
    override def toString : String = (if (value >= 0) "+" else "") + value
}