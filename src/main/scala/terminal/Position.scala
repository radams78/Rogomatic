package terminal

final case class Position(x : Int, y : Int) {
    assert (1 <= x)
    assert (x <= Terminal.WIDTH)
    assert (1 <= y)
    assert (y <= Terminal.HEIGHT)

    def setX(x : Int) : Position = Position(x.max(1).min(Terminal.WIDTH), y)

    def setY(y : Int) : Position = Position(x, y.max(1).min(Terminal.HEIGHT))

    def left(n : Int = 1) = setX(x - n)

    def right(n : Int = 1) = setX(x + n)
    
    def up(n : Int = 1) = setY(y - n)

    def down(n : Int = 1) = setY(y + n)
}