package gamedata

enum Command {
    case Quit
}

object Command {
    def keypresses (command : Command) : Seq[Char] = command match {
        case Quit => Seq('Q','y',' ',' ')
    }
}