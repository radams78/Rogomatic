trait IUser {
  def displayScreen(screen : Seq[String]) : Unit

  def getCommand() : Command
}
