import scala.collection.immutable.Queue
class InputBuffer(contents : Queue[Char] = Queue()):
    def add(char: Char) : InputBuffer = InputBuffer(contents :+ char)

    def dequeueOption : Option[(Char, InputBuffer)] = 
        for (c, q) <- contents.dequeueOption yield (c, InputBuffer(q))

    def lift(n : Int) : Option[Char] = contents.lift(n)

    def drop(n : Int) : InputBuffer = InputBuffer(contents.drop(n))
