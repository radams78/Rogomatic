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
  player.performCommand(command)
}