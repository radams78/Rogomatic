class TransparentPlayer(user : IUser, rogue : IRogue) {
    user.displayScreen(rogue.getScreen())
    user.displayInventory(
        Inventory(Map(
          Slot('a') -> Food(1),
          Slot('b') -> RingMail(+1),
          Slot('c') -> Mace(+1,+1),
          Slot('d') -> ShortBow(+1,+0),
          Slot('e') -> Arrow(31, +0, +0)
        ))
    )
    for (k <- Command.keypresses(user.getCommand())) {
        rogue.sendKeypress(k)
    }
}
