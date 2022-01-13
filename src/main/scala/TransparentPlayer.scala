class TransparentPlayer(user : IUser, rogue : IRogue) {
    user.displayScreen(rogue.getScreen())
    for (k <- Command.keypresses(user.getCommand())) {
        rogue.sendKeypress(k)
    }
}
