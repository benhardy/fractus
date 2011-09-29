package net.aethersanctum.fractus

import java.awt.Color

/**
 *  MegaCanvas remembers what we've been doing with the paint.
 *  It's a big two-dimensional array of paint buckets.
 */
class MegaCanvas(width:Int, height:Int,
                 displayUpdater: ((Int, Int, PaintBucket) => Boolean)) {

  println("Initializing MegaCanvas "+width+"x"+height)

  private val dots: Array[Array[PaintBucket]] = new Array[Array[PaintBucket]](height)

  for (row <- 0 until height) {
    dots(row) = new Array[PaintBucket](width)
    for (col <- 0 until width) {
      dots(row)(col)= new PaintBucket
    }
  }

  def containsPoint(x:Int, y:Int) = {
    x>0 && y>0 && x<width && y<height
  }

  def hits(x:Int, y:Int) = dots(y)(x).hits

  /**
   * add paint to an individual pixel in the array
   */
  def paint(x:Int, y:Int, c:Color, intensity:Double) = {
    if (containsPoint(x,y)) {
      val bucket = dots(y)(x)
      bucket.paint(c, intensity)
      displayUpdater(x,y,bucket)
    }
  }

}