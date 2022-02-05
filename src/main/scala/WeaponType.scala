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
}