class TransparentPlayer(user : IUser, rogue : IRogue) {
    user.displayScreen(rogue.getScreen())
    rogue.sendKeypress('Q')
    rogue.sendKeypress('y')
    rogue.sendKeypress(' ')
    rogue.sendKeypress(' ')
}
