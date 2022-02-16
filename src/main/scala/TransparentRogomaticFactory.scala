import rogue.IRogue
import rogue.RoguePlayerFactory
import expert.transparent.IUser
import expert.transparent.TransparentExpertFactory

object TransparentRogomaticFactory {
    def makeTransparentRogomatic(rogue : IRogue, user : IUser) : Rogomatic =
        new Rogomatic(RoguePlayerFactory.makeRoguePlayer(rogue), TransparentExpertFactory.makeExpert(user))

    def makeTransparentRogomatic() : Rogomatic =
      new Rogomatic(RoguePlayerFactory.makeRoguePlayer(), TransparentExpertFactory.makeExpert())
}
