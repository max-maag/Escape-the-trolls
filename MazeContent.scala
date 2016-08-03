sealed trait MazeContent {
  val isBlocking: Boolean
}

sealed trait NonBlocking extends MazeContent {
  val isBlocking = false
}

case object Empty extends NonBlocking
case object Goal extends NonBlocking

sealed trait Blocking extends MazeContent {
  val isBlocking = true
}

case object Wall extends Blocking
case object WalledGoal extends Blocking

sealed trait Troll extends Blocking
case object TrollLeft extends Troll
case object TrollUp extends Troll
case object TrollRight extends Troll
case object TrollDown extends Troll

sealed trait Player extends NonBlocking
case object PlayerLeft extends Player
case object PlayerUp extends Player
case object PlayerRight extends Player
case object PlayerDown extends Troll