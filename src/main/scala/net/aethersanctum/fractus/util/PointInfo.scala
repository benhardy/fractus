package net.aethersanctum.fractus.util

/**
 * Copright 2000-2010 eHarmony, Inc. All rights reserved.
 * @author bhardy
 */

sealed abstract case class PointInfo

case class Unknown extends PointInfo
         /*
sealed abstract case class Known extends PointInfo

case class Assumed(assumption:Known) extends PointInfo   // might be redundant

case class Iterated(iterations:Int) extends Known

case class Inside extends Known

object PointInfo {
  val unknown = Unknown()
  val inside = Inside()
}

class PointInfoGrid(width:Int, height:Int) {
  private val infos:Array[Array[PointInfo]] = new Array(height, width)
  update(0, 0, width, height, PointInfo.unknown)

  def apply(x:Int, y:Int) : PointInfo = infos(y)(x)

  def update(x:Int, y:Int, info: PointInfo) = { infos(y)(x) = info }

  def update(xStart:Int, yStart:Int, blockWidth:Int, blockHeight:Int, info:PointInfo) = {
    val xMax = max(width, xStart+blockWidth)
    val yMax = max(height, yStart+blockHeight)
    for (y <- yStart until yMax) {
      for (x <- 0 until xMax) { 
        infos(y)(x) = info
      }
    }

  }

  def gridNeighbors(scale:Int)(where:(Int,Int)) = {
    val x,y = where
    val xMin = if (x>= scale) x - scale else x
    val yMin = if (y>= scale) y - scale else y
    val xMax = if (x< (width-scale-1)) x + scale else x
    val yMax = if (y< (height-scale-1)) y + scale else y
    var locations: List[(Int,Int)] = Nil
    for {
      yv <- yMin to yMax by scale
      xv <- xMix to xMax by scale
    } yield (xv,yv)
  }
  
  def gridProcess(start:(Int,Int), scale:Int, action:(Int,Int)=>Boolean) = {
    Search.depthFirstSearch(start, action, gridNeighbors(scale))
  }
}

object Search {

  /**
   * perform a depth first search of neighboring cells where the result of
   * performing a particular action on it returns true.
   * if the action returns true, we'll act on the neighbors recursively.
   * this method is completely generic
   */
  def depthFirstSearch[POS](start:POS, action:POS=>Boolean,
          neighborListGenerator: POS=>Iterable[POS]) {

    // remember where we've been
    val marked = scala.collection.mutable.Set[POS]()


    def search[POS](position:POS): Unit = {
      if (! marked(position)) {
        marked.add(position)
        if (action(position)) {
          val neighbors = neighborListGenerator(position)
          neighbors foreach { search }
        }
      }
    }
    search(start)
  }

}
*/