import io.IOUtils

import scala.io.Source
import scala.util.{Failure, Success}

object Main extends App {
  private val utfCharset = Map[MazeContent, Char](
    Wall -> '\u2588',
    Empty -> ' ',
    Goal -> '\u2573',
    WalledGoal -> '\u2612',
    PlayerUp -> '\u25b2',
    PlayerRight -> '\u25b6',
    PlayerDown -> '\u25bc',
    PlayerLeft -> '\u25c0'
  )

  private val asciiCharset = Map[MazeContent, Char](
    Wall -> '#',
    Empty -> ' ',
    Goal -> 'X',
    WalledGoal -> '*',
    PlayerUp -> '^',
    PlayerRight -> '>',
    PlayerDown -> 'v',
    PlayerLeft -> '<'
  )

  def uglyPrintMaze(maze: Maze) =
    for(y <- 0 until maze.height) {
      for(x <- 0 until maze.width) {
        print(asciiCharset(maze(x,y)))
      }
      println()
    }

  private val prettyWalls = Array(
    '\u25a0', '\u2575', '\u2576', '\u2514', '\u2577', '\u2502', '\u250c', '\u251c',
    '\u2574', '\u2518', '\u2500', '\u2534', '\u2510', '\u2524', '\u252c', '\u253c'
  )

  def prettyPrintMaze(maze: Maze) =
    for(y <- 0 until maze.height) {
      for (x <- 0 until maze.width) {
        val c = maze(x,y) match {
          case Wall =>
            var i = 0
            if(y != 0 && maze(x,y-1) == Wall)
              i += 1
            if(x != maze.width-1 && maze(x+1,y) == Wall)
              i += 2
            if(y != maze.height-1 && maze(x,y+1) == Wall)
              i += 4
            if(x != 0 && maze(x-1,y) == Wall)
              i += 8

            prettyWalls(i)

          case c => utfCharset(c)
        }
        print(c)
      }
      println()
    }

  if(args.length == 0)
    printHelpAndQuit(1)

  val(file, useAscii) = {
    var useAscii = false
    var file: Source = null

    for(arg <- args) {
      arg match {
        case "-ascii" => useAscii = true
        case "-help" | "-h" => printHelpAndQuit()
        case s if s(0) == '-' =>
          println(s"Unrecognised option $arg.")
          printHelpAndQuit(1)
        case f => IOUtils.find(f) match {
          case Success(s) => file = s
          case Failure(e) =>
            System.err.println(s"${e.toString}")
            sys.exit(1)
        }
      }
    }

    (file, useAscii)
  }

  Maze.parse(file) match {
    case Success(m) => new ETTGame(m, if(useAscii) uglyPrintMaze else prettyPrintMaze)
    case Failure(e) =>
      System.err.println(e.getLocalizedMessage)
      sys.exit(1)
  }

  def printHelpAndQuit(err: Int = 0) = {
    println("scala Main [options] <maze file>")
    println("\tmap file:\tAbsolute or relative path to file that contains a maze.")
    println()
    println("\tOptions:")
    println("\t\t-ascii\tUse ascii characters instead of unicode.")
    println("\t\t-h[elp]\tShow this help.")
    sys.exit(err)
  }
}
