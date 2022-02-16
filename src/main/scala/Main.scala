@main def rgm : Unit = {
  val player = TransparentRogomaticFactory.makeTransparentRogomatic()
  player.performNextCommand()
}