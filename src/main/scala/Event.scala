enum Event {
  case WasWearing(armorType: ArmorType)
  case PickedUp(item: Item, slot: Slot)
  case Identified(slot: Slot, item: Item)
  case Quaffed(potionPower: PotionPower)
  case FoundTrap(trapType: TrapType)
  case Triggered(trapType: TrapType)
  case Killed(monsterType: Option[MonsterType])
  case Dropped(item: Item)
  case Unhallucinate
  case AteSlimeMold
  case MovedOnto(item: Item)
  case EmptyWand
  case Stole(item: Item)
  case Bounce(bolt: Bolt)
  case BoltHit(bolt: Bolt)
  case BoltMisses(bolt: Bolt)
  case BoltHitsMonster(bolt: Bolt, monsterType: MonsterType)
  case HitBy(monsterType : Option[MonsterType])
  case MissedBy(monsterType: Option[MonsterType])
  case ThrownHit(weaponType: WeaponType)
  case ThrownMisses(weaponType: WeaponType)
  case ConfusedMonster
  case RustVanishes
  case ConfusedBy(monsterType: MonsterType)
  case Unblind
  case ScrollDust
  case ItemVanishes(item: Item)
  case HoldMonsters
  case Undisguise(monsterType: MonsterType)
  case Confusion
  case Wielding(weapon : Weapon)
  case TookOff(armor: Armor)
  case Wear(armor: Armor)
  case LevelUp(newLevel : Int)
  case Hit
  case Miss
  case BeingHeld
  case MoveAgain
  case StillStuck
  case Cursed
  case Read(scrollPower: ScrollPower)
  case Upstairs
  case Unlevitate
  case Unhaste
  case Unconfuse
  case ArmorWeakens
  case ProtectArmor
  case EnchantArmor
  case EnchantWeapon(weaponType: WeaponType)
  case AggravateMonster
  case CaughtInBearTrap
  case StoleGold
  case ReadDetectSomething
  case DrainedLife
  case Frozen
  case Faint
  case TrapDoor
  case AteFood
  case Gold(quantity: Int)
}
