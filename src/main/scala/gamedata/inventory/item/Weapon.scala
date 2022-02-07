enum Weapon extends Wieldable {
  case Melee(meleeType: MeleeType, bonuses: Weapon.Bonuses)
  case Thrower(throwerType: ThrowerType, bonuses: Weapon.Bonuses)
  case Missile(quantity: Int, missileType: MissileType, bonuses: Weapon.Bonuses)

  override def toString : String = this match {
      case Melee(meleeType, bonuses) => s"a $bonuses $meleeType"
      case Thrower(throwerType, bonuses) => s"a $bonuses $throwerType"
      case Missile(quantity, missileType, bonuses) => 
          s"$quantity $bonuses ${if (quantity > 1) missileType.plural else missileType.singular}"
  }
}

object Weapon {
  def apply(
      quantity: Int,
      missileType: MissileType,
      plusToHit: Bonus,
      plusDamage: Bonus
  ): Weapon =
    new Missile(
      quantity,
      missileType,
      Bonuses.Identified(plusToHit, plusDamage)
    )

  def apply(
      quantity: Int,
      missileType: MissileType,
      plusToHit: Int,
      plusDamage: Int
  ): Weapon =
    apply(quantity, missileType, Bonus(plusToHit), Bonus(plusDamage))

  def apply(
      weaponType: WeaponType,
      plusToHit: Bonus,
      plusDamage: Bonus
  ): Weapon = weaponType match {
    case meleeType: MeleeType =>
      Melee(meleeType, Bonuses.Identified(plusToHit, plusDamage))
    case throwerType: ThrowerType =>
      Thrower(throwerType, Bonuses.Identified(plusToHit, plusDamage))
    case missileType: MissileType =>
      Missile(1, missileType, Bonuses.Identified(plusToHit, plusDamage))
  }

  def apply(weaponType: WeaponType, plusToHit: Int, plusDamage: Int): Weapon =
    apply(weaponType, Bonus(plusToHit), Bonus(plusDamage))

  def apply(quantity: Int, missileType: MissileType): Weapon =
    Missile(quantity, missileType, Bonuses.Unidentified)

  def apply(weaponType: WeaponType): Weapon = weaponType match {
    case meleeType: MeleeType     => Melee(meleeType, Bonuses.Unidentified)
    case throwerType: ThrowerType => Thrower(throwerType, Bonuses.Unidentified)
    case missileType: MissileType =>
      Missile(1, missileType, Bonuses.Unidentified)
  }

  enum Bonuses {
    case Unidentified
    case Identified(plusToHit: Bonus, plusDamage: Bonus)
  }

}
