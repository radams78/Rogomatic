package player

import rogue.IRogue

// TODO Keep the game going after one turn
class TransparentPlayer(mediator : Rogomatic) {
  def playTurn() : Unit = mediator.performNextCommand()
}

object TransparentPlayer {
  def apply(user : IUser, rogue : IRogue) : TransparentPlayer = 
    new TransparentPlayer(Rogomatic(rogue, user))
}