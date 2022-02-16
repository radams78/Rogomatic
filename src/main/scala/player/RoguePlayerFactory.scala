package player

import rogue.IRogue
import rogue.RogueFactory

object RoguePlayerFactory {
  def makeRoguePlayer(rogue : IRogue) : IRoguePlayer = new RoguePlayer(rogue)
  
  def makeRoguePlayer() : IRoguePlayer = new RoguePlayer(RogueFactory.makeRogue())
}
