package expert.transparent

import scala.io.StdIn
import scala.annotation.tailrec

import gamedata.Command
import gamedata.inventory.Inventory

// Humble object
class User extends IUser {
  override def displayScreen(screen : Seq[String]) : Unit = println(screen.mkString("\n"))

  override def displayInventory(inventory : Inventory) : Unit = println(inventory)

  override def getCommand() : Command = {
    @tailrec
    def getCommand0() : Command = StdIn.readChar match {
      case 'Q' => return Command.Quit
      case c => 
        println(s"Unrecognised command: $c")
        getCommand0()
      }
    getCommand0()
  }
}
