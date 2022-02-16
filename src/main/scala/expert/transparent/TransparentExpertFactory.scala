package expert.transparent

import expert.IExpert

object TransparentExpertFactory {
  def makeExpert() : IExpert = new TransparentExpert(new User)
}
