package net.aethersanctum.fractus

import java.awt.Color

/**
 *  MegaCanvas remembers how we've been distributing the paint.
 *  It's a big two-dimensional array of paint buckets.
 */
class MegaCanvas(width: Int, height: Int,
                 paintObserver: PaintObserver) {

  println("Initializing MegaCanvas " + width + "x" + height)

  private val dots: Array[Array[PaintBucket]] = new Array[Array[PaintBucket]](height)

  for (row <- 0 until height) {
    dots(row) = new Array[PaintBucket](width)
    for (col <- 0 until width) {
      dots(row)(col) = new PaintBucket
    }
  }

  /**
   * is the point suggested by (x,y) actually within the confines of this canvas dimensions?
   */
  def containsPoint(x: Int, y: Int) = {
    x > 0 && y > 0 && x < width && y < height
  }

  /**
   * how many splashes of paint have been added to  the bucket at these coordinates
   */
  def hits(x: Int, y: Int) = dots(y)(x).hits

  /**
   * add paint to an individual paint bucket in the array.
   *
   * this is attenuated by intensity to allow addition of partial splashes of paint
   * (for example for anti-aliasing)
   */
  def paint(x: Int, y: Int, c: Color, intensity: Double) = {
    if (containsPoint(x, y)) {
      val bucket = dots(y)(x)
      bucket.paint(c, intensity)
      paintObserver.updateDisplayPoint(x, y, bucket)
    }
  }

}