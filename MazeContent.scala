sealed trait MazeContent
case object Wall extends MazeContent
case object Empty extends MazeContent

sealed trait Troll extends MazeContent
case object TrollLeft extends Troll
case object TrollUp extends Troll
case object TrollRight extends Troll
case object TrollDown extends Troll

sealed trait Player extends MazeContent
case object PlayerLeft extends Player
case object PlayerUp extends Player
case object PlayerRight extends Player
case object PlayerDown extends Troll