package rogue

object RoguePlayerFactory {
  def makeRoguePlayer(rogue : IRogue) : IRoguePlayer = new RoguePlayer(rogue)
  
  def makeRoguePlayer() : IRoguePlayer = new RoguePlayer(new Rogue())
}
