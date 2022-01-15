class TransparentPlayer(user : IUser, rogue : IRogue) {
    user.displayScreen(rogue.getScreen())
    rogue.sendKeypress('i')
    val inventory =
        if rogue.getScreen().slice(0,6) == Seq(
        "                                                a) some food",
        "                                                b) +1 ring mail [4] being worn",
        "                                                c) a +1,+1 mace in hand",
        "                                                d) a +1,+0 short bow",
        "                                                e) 31 +0,+0 arrows",
        "                                                --press space to continue--"
        ).map(_.padTo(80,' ')) then         Inventory(Map(
          Slot('a') -> Food(1),
          Slot('b') -> RingMail(+1),
          Slot('c') -> Mace(+1,+1),
          Slot('d') -> ShortBow(+1,+0),
          Slot('e') -> Arrow(31, +0, +0)
        ))
        else throw new Error(s"Unrecoginsed screen: \n${rogue.getScreen()}")
    rogue.sendKeypress(' ')
    user.displayInventory(inventory)
    for (k <- Command.keypresses(user.getCommand())) {
        rogue.sendKeypress(k)
    }
}
