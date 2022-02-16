package gamedata.inventory.item

enum Food extends Item {
    case Rations(quantity : Int) 

    case SlimeMold

    override def toString : String = this match {
        case Rations(quantity) => s"$quantity ${if (quantity == 1) "ration" else "rations"}"
        case SlimeMold => "a slime-mold"
    }
}
