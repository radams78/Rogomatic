enum MessageLine {
  case More(events: Seq[Event])
  case Message(events: Seq[Event])
}
