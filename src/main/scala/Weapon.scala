abstract class Weapon(weaponType : WeaponType, bonuses : Bonuses) extends Wieldable

case class Melee(meleeType : MeleeType, bonuses: Bonuses) extends Weapon(meleeType, bonuses)

case class Thrower(throwerType : ThrowerType, bonuses: Bonuses) extends Weapon(throwerType, bonuses)

case class Missile(quantity : Int, missileType : MissileType, bonuses : Bonuses) extends Weapon(missileType, bonuses)

trait Bonuses

case object Unidentified extends Bonuses

case class Identified(plusToHit : Bonus, plusDamage : Bonus) extends Bonuses

object Weapon {
    def apply(quantity : Int, missileType : MissileType, plusToHit : Bonus, plusDamage : Bonus) : Weapon = 
        new Missile(quantity, missileType, Identified(plusToHit, plusDamage))

    def apply(quantity : Int, missileType : MissileType, plusToHit : Int, plusDamage : Int) : Weapon = 
        apply(quantity, missileType, Bonus(plusToHit), Bonus(plusDamage))

    def apply(weaponType : WeaponType, plusToHit : Bonus, plusDamage : Bonus) : Weapon = weaponType match {
        case meleeType : MeleeType => Melee(meleeType, Identified(plusToHit, plusDamage))
        case throwerType : ThrowerType => Thrower(throwerType, Identified(plusToHit, plusDamage))
        case missileType : MissileType => Missile(1, missileType, Identified(plusToHit, plusDamage))
    }

    def apply(weaponType : WeaponType, plusToHit : Int, plusDamage : Int) : Weapon = 
        apply(weaponType, Bonus(plusToHit), Bonus(plusDamage))

    def apply(quantity : Int, missileType : MissileType) : Weapon = Missile(quantity, missileType, Unidentified)

    def apply(weaponType : WeaponType) : Weapon = weaponType match {
        case meleeType : MeleeType => Melee(meleeType, Unidentified)
        case throwerType : ThrowerType => Thrower(throwerType, Unidentified)
        case missileType : MissileType => Missile(1, missileType, Unidentified)
    }
}