class TransparentPlayer(user : IUser, rogue : IRogue) {
    user.displayScreen(rogue.getScreen())
    rogue.sendKeypress('i')
    var itemsMap = Map[Slot,Item]()
    for (line <- rogue.getScreen().takeWhile(s => ! s.contains("--press space to continue--"))) do {
        if (line.contains("a) some food")) then itemsMap = itemsMap.updated(Slot('a'), Food(1))
        else if (line.contains("b) +1 ring mail [4] being worn")) then itemsMap = itemsMap.updated(Slot('b'), RingMail(+1))
        else if (line.contains("c) a +1,+1 mace in hand")) then itemsMap = itemsMap.updated(Slot('c'), Mace(+1,+1))
        else if (line.contains("d) a +1,+0 short bow")) then itemsMap = itemsMap.updated(Slot('d'), ShortBow(+1, 0))
        else if (line.contains("e) 31 +0,+0 arrows")) then itemsMap = itemsMap.updated(Slot('e'), Arrow(31, +0, +0))
        else throw new Error("Unrecognised line in inventory screen: " + line)
    }
    val inventory = Inventory(itemsMap)
    rogue.sendKeypress(' ')
    user.displayInventory(inventory)
    for (k <- Command.keypresses(user.getCommand())) {
        rogue.sendKeypress(k)
    }
}
