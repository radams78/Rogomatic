import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class TransparentSpec extends AnyFlatSpec with should.Matchers :
    "A transparent game player" should "pass on the first screen to the user" in {
        val firstScreen =                 
                  """                                                                                
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
                  |
                  |
                  |
                  |                           ---------+----------
                  |                           +..............B]..|
                  |                           |..................|
                  |                           |K.......@.........+
                  |                           --------------------
                  |
                  |Level: 1  Gold: 0      Hp: 12(12)   Str: 16(16) Arm: 4  Exp: 1/0""".stripMargin
                  .split("\n")
                  .map(_.padTo(80,' '))

        object MockRogue extends IRogue {
            override def getScreen : Array[String] = firstScreen
        }

        object MockUser extends IUser {
            var seenScreen = false

            override def displayScreen(screen : Array[String]) = {
                screen should contain theSameElementsInOrderAs (firstScreen)
                seenScreen = true
            }
        }

        val transparentPlayer = TransparentPlayer(MockUser, MockRogue)
        MockUser shouldBe Symbol("seenScreen")
    }