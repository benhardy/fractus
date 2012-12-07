package net.aethersanctum.fractus

import java.awt.Color
import math._


/**
 * Different ways of painting the canvas
 */
sealed trait Brush {
  def paint(x: Double, y: Double, c: Color)
}

/**
 * dumbly draw a single point at the requested pixel coordinates
 */
case class IntegerPointBrush(canvas: MegaCanvas) extends Brush {
  override def paint(x: Double, y: Double, c: Color) {
    canvas.paint(x.toInt, y.toInt, c, 1.00)
  }
}

/**
 * draw a single anti-aliased point beginning at the requested pixel coordinates
 */
case class AntiAliasedPointBrush(canvas: Canvas) extends Brush {
  override def paint(x: Double, y: Double, c: Color) {
    val xi = x.toInt
    val yi = y.toInt
    val leftFraction = x - xi
    val topFraction = y - yi
    val rightFraction = 1 - leftFraction
    val bottomFraction = 1 - topFraction
    canvas.paint(xi, yi, c, leftFraction * topFraction)
    canvas.paint(xi + 1, yi, c, rightFraction * topFraction)
    canvas.paint(xi, yi + 1, c, leftFraction * bottomFraction)
    canvas.paint(xi + 1, yi + 1, c, rightFraction * bottomFraction)
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
case class InitiallyBlurryBrush(canvas: Canvas) extends Brush {

  /**
   * Define the width of the brush in terms of how much paint is
   * already at the spot we want to paint.
   * <p>
   * How much paint splatters depends on how much is already there.
   * If there's not much there, it splatters a lot. This creates a
   * blur in areas with not much paint.
   * @param hits
   * @return
   */
  def splatter(hits: Double) = {
    1 + 4 / (hits + 1)
  }

  /**
   * Given a center position and a stripe thickness,
   * @return a range of pixels which appear in the stripe and a
   *         function which given a pixel number in that range,
   *         returns the fraction of the pixel which the stripe
   *         covers.
   *         e.g. a stripe centered at 3.25 with a thickness of
   *         1.50 will extend from 2.50 to 4.00. The range of
   *         affected pixels is [2..3]. The stripe fatness function
   *         at 2 is 0.5, and at 3 is 1.0.
   */
  def stripeGenerator(center:Double, thickness:Double) : (Range, Int => Double) = {
    val halfBrush = thickness / 2
    val start = center - halfBrush
    val end = center + halfBrush
    val startPixel = start.toInt
    val startGap = start - startPixel
    val endPixel = end.toInt
    val endStub = end - endPixel
    val startStub = if (startPixel<endPixel) 1 - startGap else thickness

    def fatness(pos:Int) = {
      if (pos > startPixel) {
        if (pos < endPixel) {
          1.0
        } else if (pos == endPixel) {
          endStub
        } else {
          0.0
        }
      } else if (pos == startPixel) {
        startStub
      } else {
        0.0
      }
    }
    (Range(startPixel, endPixel), fatness)
  }

  override def paint(xp: Double, yp: Double, color: Color) {
    val brushWidth = splatter(canvas.hits(xp.toInt, yp.toInt))
    val strokeFade = 1.0 / (brushWidth * brushWidth)
    val (xrange,xstripe) = stripeGenerator(xp, brushWidth)
    val (yrange,ystripe) = stripeGenerator(yp, brushWidth)
    for(
        yi <- yrange;
        xi <- xrange
    ) {
      val stroke = xstripe(xi) * ystripe(yi) * strokeFade
      canvas.paint(xi, yi, color, stroke)
    }
  }


}