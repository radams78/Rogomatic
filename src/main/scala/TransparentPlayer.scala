class TransparentPlayer(user : IUser, rogue : IRogue) {
    user.displayScreen(rogue.getScreen())
    for (k <- Command.keypresses(Command.Quit)) {
        rogue.sendKeypress(k)
    }
}
