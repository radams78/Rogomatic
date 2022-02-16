package player

import gamedata.Event

enum MessageLine {
  case More(events: Seq[Event])
  case Message(events: Seq[Event])
}
