package io


import scala.io.Source
import java.io.{File, FileNotFoundException}
import java.net.URL

import scala.util.Try

object IOUtils {
  def find(path: String): Try[Source] = Try {
    val f = new File(path)
    if(f.exists)
      Source.fromFile(f)
    else
      getClass().getResource(if(path(0) != '/') "/" + path else path) match {
        case null => throw new FileNotFoundException(path)
        case v => Source.fromFile(v.toURI())
      }
  }
  
  def findPath(path: String): Try[URL] = Try {
    val f = new File(path)
    if(f.exists)
      f.toURI.toURL
    else
      getClass().getResource(if(path(0) != '/') "/" + path else path) match {
        case null => throw new FileNotFoundException(path)
        case v => v
      }
  }
}