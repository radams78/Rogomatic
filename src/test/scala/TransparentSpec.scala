import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class TransparentSpec extends AnyFlatSpec with should.Matchers:
  "A transparent game player" should "pass on a quit command to Rogue" in {
    val firstScreen =
      """
        |
        |
        |
        |
        |
        |
        |
        |                               --------------+---
        |                               |............:...|
        |                               |....@...........|
        |                               |......H.........|
        |                               |................|
        |                               |%......!........+
        |                               ---------------+--
        |
        |
        |
        |
        |
        |
        |
        |
        |Level: 1  Gold: 0      Hp: 12(12)   Str: 16(16) Arm: 4  Exp: 1/0"""
        .stripMargin
        .split("\n")
        .map(_.padTo(80, ' '))

    val inventoryScreen =
      """                                                a) some food
        |                                                b) +1 ring mail [4] being worn
        |                                                c) a +1,+1 mace in hand
        |                                                d) a +1,+0 short bow
        |                                                e) 31 +0,+0 arrows
        |                                                --press space to continue--
        |
        |
        |                               --------------+---
        |                               |............:...|
        |                               |....@...........|
        |                               |......H.........|
        |                               |................|
        |                               |%......!........+
        |                               ---------------+--
        |
        |
        |
        |
        |
        |
        |
        |
        |Level: 1  Gold: 0      Hp: 12(12)   Str: 16(16) Arm: 4  Exp: 1/0"""
        .stripMargin
        .split("\n")
        .map(_.padTo(80, ' '))

    val secondScreen : Seq[String] = 
        """really quit? 
          |
          |
          |
          |
          |
          |
          |
          |                               --------------+---
          |                               |............:...|
          |                               |....@...........|
          |                               |......H.........|
          |                               |................|
          |                               |%......!........+
          |                               ---------------+--
          |
          |
          |
          |
          |
          |
          |
          |
          |
          |Level: 1  Gold: 0      Hp: 12(12)   Str: 16(16) Arm: 4  Exp: 1/0"""
        .stripMargin
        .split("\n")
        .map(_.padTo(80, ' '))

    val thirdScreen : Seq[String] = 
        """quit with 0 gold-more-
          |
          |
          |
          |
          |
          |
          |
          |                               --------------+---
          |                               |............:...|
          |                               |....@...........|
          |                               |......H.........|
          |                               |................|
          |                               |%......!........+
          |                               ---------------+--
          |
          |
          |
          |
          |
          |
          |
          |
          |
          |Level: 1  Gold: 0      Hp: 12(12)   Str: 16(16) Arm: 4  Exp: 1/0"""
        .stripMargin
        .split("\n")
        .map(_.padTo(80, ' '))

    val fourthScreen =
      """-more-
          |
          |
          |                              Top  Ten  Rogueists
          |
          |
          |
          |
          |Rank   Score   Name
          |
          | 1      1224   robin: died of starvation on level 11
          |
          |
          |
          |
          |
          |
          |
          |
          |
          |
          |
          |
          |
          |"""
        .stripMargin
        .split("\n")
        .map(_.padTo(80, ' '))
        
    object MockRogue extends IRogue {
      var state = 1

      override def getScreen(): Seq[String] = state match {
        case 1 => firstScreen
        case 2 => inventoryScreen
        case 3 => secondScreen
        case 4 => thirdScreen
        case 5 => fourthScreen
        case 6 => Seq()
      }

      override def sendKeypress(keypress : Char) : Unit = state match {
        case 1 => keypress match
          case 'i' => state = 2
          case 'Q' => state = 3
          case c => fail("Unrecognized keypress in state 1: " + c)
        case 2 =>
          keypress should be (' ')
          state = 1
        case 3 =>
          keypress should be ('y')
          state = 4
        case 4 =>
          keypress should be (' ')
          state = 5
        case 5 =>
          keypress should be (' ')
          state = 6
        case 6 =>
          fail("Received keypress after game ended")
      }
    }

    object MockUser extends IUser {
      var seenScreen = false
      var seenInventory = false

      override def displayScreen(screen: Seq[String]) = {
        screen should contain theSameElementsInOrderAs (firstScreen)
        seenScreen = true
      }

      override def displayInventory(inventory : Inventory) : Unit = {
        inventory should be (Inventory(Map(
          Slot('a') -> Food.Rations(1),
          Slot('b') -> Armor(ArmorType.RINGMAIL, +1),
          Slot('c') -> Weapon(MeleeType.MACE, +1,+1),
          Slot('d') -> Weapon(ThrowerType.SHORTBOW, +1,+0),
          Slot('e') -> Weapon(31, MissileType.ARROW, +0, +0)
        )))
        seenInventory = true
      }

      override def getCommand() : Command = {
        seenScreen should be (true)
        Command.Quit
      }
    }

    val transparentPlayer = TransparentPlayer(MockUser, MockRogue)
    MockUser.seenScreen should be (true)
    MockUser.seenInventory should be (true)
    MockRogue.state should be (6)
  }
