package player

import scala.util.parsing.combinator.RegexParsers
import gamedata.Bolt
import gamedata.Command
import gamedata.Event
import gamedata.Hunger
import gamedata.MonsterType
import gamedata.TrapType
import gamedata.inventory.Inventory
import gamedata.inventory.Slot
import gamedata.inventory.item.Armor
import gamedata.inventory.item.ArmorType
import gamedata.inventory.item.Bonus
import gamedata.inventory.item.Colour
import gamedata.inventory.item.Item
import gamedata.inventory.item.Food
import gamedata.inventory.item.Material
import gamedata.inventory.item.MeleeType
import gamedata.inventory.item.MissileType
import gamedata.inventory.item.Potion
import gamedata.inventory.item.PotionPower
import gamedata.inventory.item.Scroll
import gamedata.inventory.item.ScrollPower
import gamedata.inventory.item.ThrowerType
import gamedata.inventory.item.Wand
import gamedata.inventory.item.Weapon
import gamedata.inventory.item.WeaponType
import gamedata.inventory.item.Wieldable
import rogue.IRogue

class TransparentPlayer(user : IUser, rogue : IRogue) {
  private val player : RoguePlayer = new RoguePlayer(rogue)
  private val expert = new TransparentExpert(user)

  private val command = expert.getCommand(player.getScreen(), player.getInventory())
  for (k <- command.keypresses) rogue.sendKeypress(k)
}

object TransparentPlayer extends RegexParsers {
  def parseInventoryLine(line : String): InventoryScreenLine =
    parseAll(inventoryLine, line) match {
      case Success(result, _) => result
      case failure : NoSuccess => throw new java.lang.Error(failure.msg)
    }
  private val slot: Parser[Slot] = """[a-v]""".r ^^ { (slot: String) => Slot(slot.charAt(0)) }

  private val bonus: Parser[Bonus] = """[-+]\d+""".r ^^ { (bonus: String) => Bonus(bonus.toInt) }

  private val number: Parser[Int] =
    """\d+""".r ^^ {
      _.toInt
    }

  private val armorType: Parser[ArmorType] =
    "leather armor" ^^^ ArmorType.LEATHER |
      "ring mail" ^^^ ArmorType.RINGMAIL |
      "scale mail" ^^^ ArmorType.SCALEMAIL |
      "chain mail" ^^^ ArmorType.CHAINMAIL |
      "banded mail" ^^^ ArmorType.BANDEDMAIL |
      "splint mail" ^^^ ArmorType.SPLINTMAIL |
      "plate mail" ^^^ ArmorType.PLATEMAIL

  private val weaponType: Parser[WeaponType] =
    "short bow" ^^^ ThrowerType.SHORTBOW |
      "dart" ^^^ MissileType.DART |
      "arrow" ^^^ MissileType.ARROW |
      "dagger" ^^^ MissileType.DAGGER |
      "shuriken" ^^^ MissileType.SHURIKEN |
      "mace" ^^^ MeleeType.MACE |
      "long sword" ^^^ MeleeType.LONGSWORD |
      "two-handed sword" ^^^ MeleeType.TWOHANDEDSWORD

  private val pluralWeaponType: Parser[MissileType] =
    "darts" ^^^ MissileType.DART |
      "arrows" ^^^ MissileType.ARROW |
      "daggers" ^^^ MissileType.DAGGER |
      "shurikens" ^^^ MissileType.SHURIKEN

  private val colour: Parser[Colour] =
    "blue" ^^^ Colour.BLUE |
      "red" ^^^ Colour.RED |
      "green" ^^^ Colour.GREEN |
      "grey" ^^^ Colour.GREY |
      "brown" ^^^ Colour.BROWN |
      "clear" ^^^ Colour.CLEAR |
      "pink" ^^^ Colour.PINK |
      "white" ^^^ Colour.WHITE |
      "purple" ^^^ Colour.PURPLE |
      "black" ^^^ Colour.BLACK |
      "yellow" ^^^ Colour.YELLOW |
      "plaid" ^^^ Colour.PLAID |
      "burgundy" ^^^ Colour.BURGUNDY |
      "beige" ^^^ Colour.BEIGE

  private val material: Parser[Material] =
    "steel" ^^^ Material.STEEL |
      "bronze" ^^^ Material.BRONZE |
      "gold" ^^^ Material.GOLD |
      "silver" ^^^ Material.SILVER |
      "copper" ^^^ Material.COPPER |
      "nickel" ^^^ Material.NICKEL |
      "cobalt" ^^^ Material.COBALT |
      "tin" ^^^ Material.TIN |
      "iron" ^^^ Material.IRON |
      "magnesium" ^^^ Material.MAGNESIUM |
      "chrome" ^^^ Material.CHROME |
      "carbon" ^^^ Material.CARBON |
      "platinum" ^^^ Material.PLATINUM |
      "silicon" ^^^ Material.SILICON |
      "titanium" ^^^ Material.TITANIUM |
      "teak" ^^^ Material.TEAK |
      "oak" ^^^ Material.OAK |
      "cherry" ^^^ Material.CHERRY |
      "birch" ^^^ Material.BIRCH |
      "pine" ^^^ Material.PINE |
      "cedar" ^^^ Material.CEDAR |
      "redwood" ^^^ Material.REDWOOD |
      "balsa" ^^^ Material.BALSA |
      "ivory" ^^^ Material.IVORY |
      "walnut" ^^^ Material.WALNUT |
      "maple" ^^^ Material.MAPLE |
      "mahogany" ^^^ Material.MAHOGANY |
      "elm" ^^^ Material.ELM |
      "palm" ^^^ Material.PALM |
      "wooden" ^^^ Material.WOODEN

  private val armor: Parser[Armor] =
    bonus ~ armorType ~ "[" ~ number ~ "]" ^^ { case bonus ~ armorType ~ _ ~ _ ~ _ => Armor(armorType, bonus) }

  private val weapon: Parser[Weapon] =
    "a" ~ bonus ~ "," ~ bonus ~ weaponType ^^ { case _ ~ plusToHit ~ _ ~ plusDamage ~ weaponType => Weapon(weaponType, plusToHit, plusDamage) } |
      number ~ bonus ~ "," ~ bonus ~ pluralWeaponType ^^ { case quantity ~ plusToHit ~ _ ~ plusDamage ~ weaponType =>
        Weapon(quantity, weaponType, plusToHit, plusDamage)
      } |
      "a" ~ weaponType ^^ { case _ ~ weaponType => Weapon(weaponType) } |
      number ~ pluralWeaponType ^^ { case quantity ~ missileType => Weapon(quantity, missileType) }


  private val potion: Parser[Potion] =
    "a" ~ colour ~ "potion" ^^ { case _ ~ colour ~ _ => Potion(1, colour) }

  private val wand: Parser[Wand] =
    "a" ~ material ~ "wand" ^^ { case _ ~ material ~ _ => Wand(material) }

  private val scroll: Parser[Scroll] =
    "a scroll entitled: '" ~ """\w+(\s\w+)*""".r ~ "'" ^^ { case _ ~ title ~ _ => Scroll(title) }

  private val food: Parser[Food] =
    "some food" ^^^ Food.Rations(1) |
      number ~ "rations of food" ^^ { case quantity ~ _ => Food.Rations(quantity) } |
      "a slime-mold" ^^^ Food.SlimeMold

  private val item: Parser[Item] =
    food |
      armor |
      weapon |
      potion |
      wand |
      scroll

  private val monsterType: Parser[MonsterType] =
    "aquator" ^^^ MonsterType.Aquator |
      "bat" ^^^ MonsterType.Bat |
      "centaur" ^^^ MonsterType.Centaur |
      "dragon" ^^^ MonsterType.Dragon |
      "emu" ^^^ MonsterType.Emu |
      "venus fly-trap" ^^^ MonsterType.VenusFlyTrap |
      "griffin" ^^^ MonsterType.Griffin |
      "hobgoblin" ^^^ MonsterType.Hobgoblin |
      "ice monster" ^^^ MonsterType.IceMonster |
      "jabberwock" ^^^ MonsterType.Jabberwock |
      "kestrel" ^^^ MonsterType.Kestrel |
      "leprechaun" ^^^ MonsterType.Leprechaun |
      "medusa" ^^^ MonsterType.Medusa |
      "nymph" ^^^ MonsterType.Nymph |
      "orc" ^^^ MonsterType.Orc |
      "phantom" ^^^ MonsterType.Phantom |
      "quagga" ^^^ MonsterType.Quagga |
      "rattlesnake" ^^^ MonsterType.Rattlesnake |
      "snake" ^^^ MonsterType.Snake |
      "troll" ^^^ MonsterType.Troll |
      "black unicorn" ^^^ MonsterType.Unicorn |
      "vampire" ^^^ MonsterType.Vampire |
      "wraith" ^^^ MonsterType.Wraith |
      "xeroc" ^^^ MonsterType.Xeroc |
      "yeti" ^^^ MonsterType.Yeti |
      "zombie" ^^^ MonsterType.Zombie

  private val bolt: Parser[Bolt] = "fire" ^^^ Bolt.Fire | "ice" ^^^ Bolt.Ice

  private val event: Parser[Event] =
    "was wearing" ~ armorType ^^ { case _ ~ armorType => Event.WasWearing(armorType) } |
      item ~ "(" ~ slot ~ ")" ^^ { case item ~ _ ~ slot ~ _ => Event.PickedUp(item, slot) } |
      slot ~ ")" ~ item ^^ { case slot ~ _ ~ item => Event.Identified(slot, item) } |
      "a cloak of darkness falls around you" ^^^ Event.Quaffed(PotionPower.Blindness) |
      "a teleport trap" ^^^ Event.FoundTrap(TrapType.Teleport) |
      "a bear trap" ^^^ Event.FoundTrap(TrapType.Bear) |
      "a strange white mist envelops you and you fall asleep" ^^^ Event.Triggered(TrapType.Sleep) |
      "a sleeping gas trap" ^^^ Event.FoundTrap(TrapType.Sleep) |
      "a small dart just hit you in the shoulder" ^^^ Event.Triggered(TrapType.Dart) |
      "a poison dart trap" ^^^ Event.FoundTrap(TrapType.Dart) |
      "a rust trap" ^^^ Event.FoundTrap(TrapType.Rust) |
      "a gush of water hits you on the head" ^^^ Event.Triggered(TrapType.Rust) |
      "defeated the something" ^^^ Event.Killed(None) |
      "defeated the" ~ monsterType ^^ { case _ ~ monsterType => Event.Killed(Some(monsterType)) } |
      "dropped" ~ item ^^ { case _ ~ item => Event.Dropped(item) } |
      "everything looks SO boring now" ^^^ Event.Unhallucinate |
      "hmm, this potion tastes like slime-moldjuice" ^^^ Event.Quaffed(PotionPower.SeeInvisible) |
      "my, that was a yummy slime-mold" ^^^ Event.AteSlimeMold |
      "moved onto" ~ item ^^ { case _ ~ item => Event.MovedOnto(item) } |
      "nothing happens" ^^^ Event.EmptyWand |
      "oh wow, everything seems so cosmic" ^^^ Event.Quaffed(PotionPower.Hallucination) |
      "she stole" ~ item ^^ { case _ ~ item => Event.Stole(item) } |
      "the" ~ bolt ~ "bounces" ^^ { case _ ~ bolt ~ _ => Event.Bounce(bolt) } |
      "the" ~ bolt ~ "hits" ^^ { case _ ~ bolt ~ _ => Event.BoltHit(bolt) } |
      "the" ~ bolt ~ "misses" ^^ { case _ ~ bolt ~ _ => Event.BoltMisses(bolt) } |
      "the" ~ bolt ~ "hits the" ~ monsterType ^^ { case _ ~ bolt ~ _ ~ monsterType => Event.BoltHitsMonster(bolt, monsterType) } |
      "the something hit" ^^^ Event.HitBy(None) |
      "the" ~ monsterType ~ "hit" ^^ { case _ ~ monsterType ~ _ => Event.HitBy(Some(monsterType)) } |
      "the something misses" ^^^ Event.HitBy(None) |
      "the" ~ monsterType ~ "misses" ^^ { case _ ~ monsterType ~ _ => Event.MissedBy(Some(monsterType)) } |
      "the" ~ weaponType ~ "hit" ^^ { case _ ~ weaponType ~ _ => Event.ThrownHit(weaponType) } |
      "the" ~ weaponType ~ "misses" ^^ { case _ ~ weaponType ~ _ => Event.ThrownMisses(weaponType) } |
      "the monster appears confused" ^^^ Event.ConfusedMonster |
      "the rust vanishes instantly" ^^^ Event.RustVanishes |
      "the gaze of the" ~ monsterType ~ "has confused you" ^^ { case _ ~ monsterType ~ _ => Event.ConfusedBy(monsterType) } |
      "the veil of darkness lifts" ^^^ Event.Unblind |
      "the scroll turns to dust as you pick it up" ^^^ Event.ScrollDust |
      "the" ~ item ~ "vanishes as it hits the ground" ^^ { case _ ~ item ~ _ => Event.ItemVanishes(item) } |
      "the monsters around you freeze" ^^^ Event.HoldMonsters |
      "the monster freezes" ^^^ Event.HoldMonsters |
      "wait, that's a" ~ monsterType ~ "!" ^^ { case _ ~ monsterType ~ _ => Event.Undisguise(monsterType) } |
      "what a trippy feeling" ^^^ Event.Quaffed(PotionPower.Confusion) |
      "wielding" ~ weapon ^^ { case _ ~ weapon => Event.Wielding(weapon) } |
      "was wearing" ~ armor ^^ { case _ ~ armor => Event.TookOff(armor) } |
      "wearing" ~ armor ^^ { case _ ~ armor => Event.Wear(armor) } |
      "welcome to level" ~ number ^^ { case _ ~ newLevel => Event.LevelUp(newLevel) } |
      "you hit" ^^^ Event.Hit |
      "you miss" ^^^ Event.Miss |
      "you are being held" ^^^ Event.BeingHeld |
      "you can move again" ^^^ Event.MoveAgain |
      "you are still stuck in the bear trap" ^^^ Event.StillStuck |
      "you can't, it appears to be cursed" ^^^ Event.Cursed |
      "you begin to feel better" ^^^ Event.Quaffed(PotionPower.Healing) |
      "you begin to feel much better" ^^^ Event.Quaffed(PotionPower.ExtraHealing) |
      "you feel a strange sense of loss" ^^^ Event.Read(ScrollPower.HoldMonster) |
      "you feel a wrenching sensation in your gut" ^^^ Event.Upstairs |
      "you feel stronger now, what bulging muscles!" ^^^ Event.Quaffed(PotionPower.IncreaseStrength) |
      "you feel very sick now" ^^^ Event.Quaffed(PotionPower.Poison) |
      "you suddenly feel much more skillful" ^^^ Event.Quaffed(PotionPower.RaiseLevel) |
      "you start to float in the air" ^^^ Event.Quaffed(PotionPower.Levitation) |
      "you float gently to the ground" ^^^ Event.Unlevitate |
      "you feel yourself moving much faster" ^^^ Event.Quaffed(PotionPower.HasteSelf) |
      "you feel yourself slowing down" ^^^ Event.Unhaste |
      "you feel less confused now" ^^^ Event.Unconfuse |
      "you feel less trippy now" ^^^ Event.Unconfuse |
      "you feel as though someone is watching over you" ^^^ Event.Read(ScrollPower.RemoveCurse) |
      "you feel in touch with the universal oneness" ^^^ Event.Read(ScrollPower.RemoveCurse) |
      "your armor weakens" ^^^ Event.ArmorWeakens |
      "your armor is covered by a shimmering gold shield" ^^^ Event.ProtectArmor |
      "your armor glows" ~ colour ~ "for a moment" ^^^ Event.EnchantArmor |
      "your" ~ weaponType ~ "glows" ~ colour ~ "for a moment" ^^ { case _ ~ weaponType ~ _ ~ _ ~ _ => Event.EnchantWeapon(weaponType) } |
      "you hear a high pitched humming noise" ^^^ Event.AggravateMonster |
      "you hear a maniacal laughter in the distance" ^^^ Event.Read(ScrollPower.ScareMonster) |
      "you hear a faint cry of anguish in the distance" ^^^ Event.Read(ScrollPower.CreateMonster) |
      "you fall asleep" ^^^ Event.Read(ScrollPower.Sleep) |
      "you are caught in a bear trap" ^^^ Event.CaughtInBearTrap |
      "your purse feels lighter" ^^^ Event.StoleGold |
      "you have a strange feeling for a moment, orElse it passes" ^^^ Event.ReadDetectSomething |
      "you feel weaker" ^^^ Event.DrainedLife |
      "you are frozen" ^^^ Event.Frozen |
      "you faint" ^^^ Event.Faint |
      "you fell down a trap" ^^^ Event.TrapDoor |
      "yum, that tasted good" ^^^ Event.AteFood |
      "yuk, that food tasted awful" ^^^ Event.AteFood |
      number ~ "pieces of gold" ^^ { case quantity ~ _ => Event.Gold(quantity) }

  private val messageLine: Parser[MessageLine] =
    event.* ~ "-more-" ^^ { case events ~ _ => MessageLine.More(events) } |
      (event ~ """\s*""".r).* ^^ { (events: Seq[Event ~ String]) => MessageLine.Message(events.map(_._1)) }

  private val hunger: Parser[Hunger] =
    "hungry" ^^^ Hunger.Hungry |
      "weak" ^^^ Hunger.Weak |
      "faint" ^^^ Hunger.Faint |
      "" ^^^ Hunger.NotHungry

  private val statusLine: Parser[StatusLine] =
    "Level:" ~ number ~ "Gold:" ~ number ~ "Hp:" ~ number ~ "(" ~ number ~ ")" ~ "Str:" ~ number ~ "(" ~ number ~ ")" ~ "Arm:" ~ number ~ "Exp:" ~ number ~ "/" ~ number ~ hunger ^^ { case _ ~ level ~ _ ~ gold ~ _ ~ hp ~ _ ~ maxhp ~ _ ~ _ ~ str ~ _ ~ maxstr ~ _ ~ _ ~ armor ~ _ ~ exp ~ _ ~ expLevel ~ hunger =>
      StatusLine(level, gold, hp, maxhp, str, maxstr, armor, exp, expLevel, hunger)
    }

  private val inventoryLine: Parser[InventoryScreenLine] =
    """[^a-z]*""".r ~ slot ~ ")" ~ armor ~ "being worn" ^^ { case _ ~ slot ~ _ ~ armor ~ _ => InventoryScreenLine.Wearing(slot, armor) } |
      """[^a-z]*""".r ~ slot ~ ")" ~ weapon ~ "in hand" ^^ { case _ ~ slot ~ _ ~ wieldable ~ _ => InventoryScreenLine.Wielding(slot, wieldable) } |
      """[^a-z]*""".r ~ slot ~ ")" ~ item ^^ { case _ ~ slot ~ _ ~ item => InventoryScreenLine.InventoryItem(slot, item) }
}

enum InventoryScreenLine(val slot : Slot, val item : Item) {
  case Wearing(override val slot: Slot, armor: Armor) extends InventoryScreenLine(slot, armor)
  case Wielding(override val slot: Slot, wieldable: Wieldable) extends InventoryScreenLine(slot, wieldable)
  case InventoryItem(override val slot: Slot, override val item: Item) extends InventoryScreenLine(slot, item)
}