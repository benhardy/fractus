package net.aethersanctum.fractus

import java.awt.Color

/**
 * A bucket that collects paint. Used to represent a point in a canvas.
 *
 * Yeah, these are mutable. Because they get mutated a LOT and typically there's
 * millions of them inside a MegaCanvas,
 * using copy-on-write semantics here would probably be painful.
 *
 */
class PaintBucket {
  private var red: Double = 0.0
  private var green: Double = 0.0
  private var blue: Double = 0.0
  private var hitCount: Double = 0.0

  /**
   * Add paint to this bucket.
   * @param color is what paint we're using
   * @param intensity is how much paint's being added (0 to 1)
   */
  def paint(color:Color, intensity:Double) {
    red +=   color.getRed * intensity
    green += color.getGreen * intensity
    blue +=  color.getBlue * intensity
    hitCount +=  intensity
  }

  /**
   * what are the totals of color components present in this bucket?
   * Note that this doesn't tell you how much paint is in the bucket. For that, look at hits.
   */
  def colorVector = Vector(red, green, blue)

  /**
   * How much paint is in this bucket?
   */
  def hits = hitCount
}