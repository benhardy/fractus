package net.aethersanctum.fractus

import java.awt.Color

import net.aethersanctum.fractus.Colors._

/**
 * Different ways of painting the canvas
 */
trait Brush {
  def paint(x:Double, y:Double, c:Color) : Unit
}

/**
 * dumbly draw a single point at the requested pixel coordinates
 */
class IntegerPointBrush(canvas:MegaCanvas) extends Brush {
  override def paint(x:Double, y:Double, c:Color) : Unit = {
    canvas.paint(x.toInt, y.toInt, c, 1.00)
  }
}

/**
 * draw a single anti-aliased point beginning at the requested pixel coordinates
 */
class AntiAliasedPointBrush(canvas:MegaCanvas) extends Brush {
  override def paint(x:Double, y:Double, c:Color) : Unit = {
    val xi = x.toInt
    val yi = y.toInt
    val leftFraction = x - xi
    val topFraction = y - yi
    val rightFraction = 1 - leftFraction
    val bottomFraction = 1 - topFraction
    canvas.paint(xi,   yi,   c, leftFraction  * topFraction)
    canvas.paint(xi+1, yi,   c, rightFraction * topFraction)
    canvas.paint(xi,   yi+1, c, leftFraction  * bottomFraction)
    canvas.paint(xi+1, yi+1, c, rightFraction * bottomFraction)
  }
}

/**
 * This brush examines the canvas first to decide how blurry to paint.
 * If there's not much paint on the canvas at the point we're gonna paint,
 * then it spreads out the paint. But if there was more paint there, it
 * paints more sharply.
 *
 * It does this to make less densely spotted areas appear smoother.
 *
 * The brush shape ends up being a rectangle.
 *
 * I'm pretty sure the paint method has a bug that causes paint to smear to the right
 */
class InitiallyBlurryBrush(canvas:MegaCanvas) extends Brush {

  def pixelWidth(hits:Double) = {
    1 + 4 / (hits+1)
  }

  override def paint(xp:Double, yp:Double, color:Color) : Unit = {
    val brushWidth = pixelWidth(canvas.hits(xp.toInt, yp.toInt))

    val ystart = yp - brushWidth/2
    var yi = ystart.toInt
    var ystartOff = ystart - yi
    var yPaintLeft = brushWidth
    while(yPaintLeft>0) {
      val yPaintNeeded = Math.min(yPaintLeft, 1-ystartOff)
      val xstart = xp - brushWidth/2
      var xi = xstart.toInt
      var xstartOff = xstart - xi
      var xPaintLeft = brushWidth
      while(xPaintLeft > 0) {
        var xPaintNeeded = Math.min(yPaintLeft, 1-xstartOff)
        var stroke = xPaintNeeded * yPaintNeeded / (brushWidth * brushWidth)
        canvas.paint(xi, yi, color, stroke)
        xPaintLeft = xPaintLeft - xPaintNeeded
        xi = xi + 1
        xstartOff=0
      }
      yPaintLeft = yPaintLeft - yPaintNeeded
      yi = yi + 1
      ystartOff=0
    }
  }
}