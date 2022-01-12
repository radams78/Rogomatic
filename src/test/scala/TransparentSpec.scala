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
        case 2 => secondScreen
        case 3 => thirdScreen
        case 4 => fourthScreen
        case 5 => Seq()
      }

      override def sendKeypress(keypress : Char) : Unit = state match {
        case 1 =>
          keypress should be ('Q')
          state = 2
        case 2 =>
          keypress should be ('y')
          state = 3
        case 3 =>
          keypress should be (' ')
          state = 4
        case 4 =>
          keypress should be (' ')
          state = 5
        case 5 =>
          fail("Received keypress after game ended")
      }
    }

    object MockUser extends IUser {
      var seenScreen = false

      override def displayScreen(screen: Seq[String]) = {
        screen should contain theSameElementsInOrderAs (firstScreen)
        seenScreen = true
      }

      override def getCommand() : Command = {
        seenScreen should be (true)
        Command.Quit
      }
    }

    val transparentPlayer = TransparentPlayer(MockUser, MockRogue)
    MockRogue.state should be (5)
  }
