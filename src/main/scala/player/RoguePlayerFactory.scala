package player

import rogue.IRogue
import rogue.Rogue

object RoguePlayerFactory {
  def makeRoguePlayer(rogue : IRogue) : IRoguePlayer = new RoguePlayer(rogue)
  
  def makeRoguePlayer() : IRoguePlayer = new RoguePlayer(new Rogue())
}
