class User extends IUser {
  override def displayScreen(screen : Seq[String]) : Unit = println(screen.mkString("\n"))

  override def getCommand() : Command = Command.Quit
}
