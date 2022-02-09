trait Weapon extends Wieldable

object Weapon {
  case class Melee(meleeType: MeleeType, bonuses: Weapon.Bonuses) extends Weapon {
    override def toString = s"a $bonuses $meleeType"
  }

  case class Thrower(throwerType: ThrowerType, bonuses: Weapon.Bonuses) extends Weapon {
    override def toString = s"a $bonuses $throwerType"
  }

  case class Missile(quantity: Int, missileType: MissileType, bonuses: Weapon.Bonuses) extends Weapon {
    override def toString = s"$quantity $bonuses " +
      s"${if (quantity > 1) missileType.plural else missileType.singular}"
  }

  def apply(
      quantity: Int,
      missileType: MissileType,
      plusToHit: Bonus,
      plusDamage: Bonus
  ): Weapon = Missile(quantity, missileType, Bonuses.Identified(plusToHit, plusDamage))

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
