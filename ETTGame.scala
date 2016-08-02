import scala.io.StdIn
import scala.util.Random

class ETTGame(maze: Maze, printMaze: Maze => Unit) {
  val startingLocations = for {
    y <- 0 until maze.height
    x <- 0 until maze.width
    if(!(x == maze.exitX && y == maze.exitY) && maze(x,y) == Empty)
  } yield (x,y)

  val rand = new Random()

  var (playerX, playerY) = startingLocations(rand.nextInt(startingLocations.length))

  maze(playerX, playerY) = rand.nextInt(4) match {
    case 0 => PlayerRight
    case 1 => PlayerUp
    case 2 => PlayerDown
    case _ => PlayerLeft
  }

  var quit = false

  sealed trait Direction {
    val dx: Int
    val dy: Int
  }

  case object Left extends Direction {
    val dx = -1
    val dy = 0
  }

  case object Up extends Direction {
    val dx = 0
    val dy = -1
  }

  case object Right extends Direction {
    val dx = 1
    val dy = 0
  }

  case object Down extends Direction {
    val dx = 0
    val dy = 1
  }

  case object NoDirection extends Direction {
    val dx = 0
    val dy = 0
  }

  while(!quit) {
    printMaze(maze)
    print("Enter one of w,a,s,d to move, or q to quit: ")

    val (dir, canMove) = StdIn.readChar() match {
      case 'w' => (Up, maze(playerX, playerY) == PlayerUp)
      case 'a' => (Left, maze(playerX, playerY) == PlayerLeft)
      case 's' => (Down, maze(playerX, playerY) == PlayerDown)
      case 'd' => (Right, maze(playerX, playerY) == PlayerRight)
      case 'q' => sys.exit()
      case c =>
        println(s"\nUnrecognised command: $c")
        (NoDirection, false)
    }

    if(canMove) {
      if(maze(playerX+dir.dx, playerY+dir.dy) == Empty) {
        maze(playerX+dir.dx, playerY+dir.dy) = maze(playerX, playerY)
        maze(playerX, playerY) = Empty
        playerX += dir.dx
        playerY += dir.dy
      } else if(maze(playerX+dir.dx, playerY+dir.dy) == Wall &&
                maze(playerX+2*dir.dx, playerY+2*dir.dy) == Empty) {
        maze(playerX+2*dir.dx, playerY+2*dir.dy) = Wall
        maze(playerX+dir.dx, playerY+dir.dy) = maze(playerX, playerY)
        maze(playerX, playerY) = Empty
        playerX += dir.dx
        playerY += dir.dy
      }
    } else if(dir != NoDirection) {
      maze(playerX, playerY) = dir match {
        case Left => PlayerLeft
        case Up => PlayerUp
        case Right => PlayerRight
        case Down => PlayerDown
      }
    }

    if(playerX == maze.exitX && playerY == maze.exitY) {
      println("You reached the exit. Congratulations, you won!")
      sys.exit()
    }
  }
}
