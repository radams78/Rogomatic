package gamedata

enum Command {
    case Quit

    def keypresses : Seq[Char] = this match {
        case Quit => Seq('Q','y',' ',' ')
    }
}
