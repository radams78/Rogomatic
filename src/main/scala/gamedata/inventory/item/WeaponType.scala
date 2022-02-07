trait WeaponType

enum MeleeType extends WeaponType {
  case MACE
  case LONGSWORD
  case TWOHANDEDSWORD
}

enum ThrowerType extends WeaponType {
  case SHORTBOW
}

enum MissileType extends WeaponType {
  case DART
  case ARROW
  case DAGGER
  case SHURIKEN

  def singular : String = this match {
    case DART => "dart"
    case ARROW => "arrow"
    case DAGGER => "dagger"
    case SHURIKEN => "shuriken"
  }

  def plural : String = this match {
    case DART => "darts"
    case ARROW => "arrows"
    case DAGGER => "daggers"
    case SHURIKEN => "shuriken"
  }
}