package gamedata.inventory.item

sealed trait WeaponType

enum MeleeType extends WeaponType {
  case MACE
  case LONGSWORD
  case TWOHANDEDSWORD

  override def toString : String = this match {
    case MACE => "mace"
    case LONGSWORD => "long sword"
    case TWOHANDEDSWORD => "two-handed sword"
  }
}

enum ThrowerType extends WeaponType {
  case SHORTBOW

  override def toString : String = "short bow"
}

enum MissileType(val singular: String, val plural: String) extends WeaponType {
  case DART extends MissileType("dart", "darts")
  case ARROW extends MissileType("arrow", "arrows")
  case DAGGER extends MissileType("dagger", "daggers")
  case SHURIKEN extends MissileType("shuriken", "shuriken")
}
