import io.TryWith

import scala.io.Source
import scala.util.Try

class Maze(
          private val content: Array[MazeContent],
          val width: Int,
          val height: Int,
          val exitX: Int,
          val exitY: Int) {

  if(width * height != content.length)
    throw new IllegalArgumentException("Content doesn't have the given dimensions.")

  if(exitX < 0 || exitX >= width || exitY < 0 || exitY >= height)
    throw new IllegalArgumentException(s"Invalid exiting position: $exitX, $exitY.")

  def apply(x: Int, y: Int) =
    if(x < 0 || x >= width || y < 0 || y >= height)
      Wall
    else
      content(y*width + x)

  def update(x: Int, y: Int, c: MazeContent) =
    if(!(x < 0 || x >= width || y < 0 || y >= height))
      content(y*width + x) = c
}

object Maze {
  private val defaultCharset = Map(
    '#' -> Wall,
    ' ' -> Empty
  )

  def parse(file: Source, exitChar: Char = 'X', charset: Map[Char, MazeContent] = defaultCharset): Try[Maze] =
    TryWith(file) { source =>
      class MapFormatException(s: String) extends RuntimeException(s)

      val contentLines = source.getLines().toIndexedSeq
      val height = contentLines.length
      val width = contentLines(0).length()

      val (exitX, exitY, contentText) = {
        val tmp = contentLines.mkString
        val i = tmp.indexOf(exitChar)
        if(i < 0)
          throw new MapFormatException("Missing exiting position.")

        (i % width, i / width, tmp.updated(i, charset.find(_._2 == Empty).get._1))
      }

      val content = contentText.map(c => charset.getOrElse(c, throw new MapFormatException(s"Invalid char: $c")))

      new Maze(content.toArray, width, height, exitX, exitY)
    }
}
