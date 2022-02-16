package expert.transparent

import expert.IExpert

object TransparentExpertFactory {
  def makeExpert(user : IUser) = new TransparentExpert(user)

  def makeExpert() : IExpert = makeExpert(new User)
}
