package player

import rogue.IRogue

// TODO Keep the game going after one turn
class TransparentPlayer(mediator : Mediator) {
  mediator.performNextCommand()
}

object TransparentPlayer {
  def apply(user : IUser, rogue : IRogue) : TransparentPlayer = 
    new TransparentPlayer(new Mediator(new RoguePlayer(rogue), new TransparentExpert(user)))
}