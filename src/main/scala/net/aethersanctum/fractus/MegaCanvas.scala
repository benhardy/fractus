package net.aethersanctum.fractus

import java.awt.Color

/**
 * General drawing surface
 */
trait Canvas {
  /**
   * Do the supplied coordinates correspond to a point on this Canvas?
   */
  def containsPoint(x: Int, y: Int): Boolean

  /**
   * How much paint is at this point on this Canvas?
   */
  def hits(x: Int, y: Int): Double

  /**
   * Put some paint on this point on the Canvas?
   */
  def paint(x: Int, y: Int, c: Color, intensity: Double)

}

/**
 *  MegaCanvas remembers how we've been distributing the paint.
 *  It's a big two-dimensional array of paint buckets.
 *  <p>
 *  It also notifies an observer of paint events, this is used
 *  to update the display.
 */
class MegaCanvas(width: Int, height: Int,
                 paintObserver: PaintObserver) extends Canvas {

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