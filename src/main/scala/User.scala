class User extends IUser {
  def displayScreen(screen : Seq[String]) : Unit = println(screen.mkString("\n"))
}
