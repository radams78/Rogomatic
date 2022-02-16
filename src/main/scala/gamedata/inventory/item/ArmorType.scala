package gamedata.inventory.item

enum ArmorType {
  case LEATHER
  case RINGMAIL
  case SCALEMAIL
  case CHAINMAIL
  case BANDEDMAIL
  case SPLINTMAIL
  case PLATEMAIL

  override def toString : String = this match {
    case LEATHER => "leather armor"
    case RINGMAIL => "ring mail"
    case SCALEMAIL => "scale mail"
    case CHAINMAIL => "chain mail"
    case BANDEDMAIL => "banded mail"
    case SPLINTMAIL => "splint mail"
    case PLATEMAIL => "plate mail"
  }
}
