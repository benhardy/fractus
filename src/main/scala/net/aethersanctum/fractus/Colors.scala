package net.aethersanctum.fractus

import java.awt.Color

/**
 * Some color manipulation functions. Treat Colors as Vectors for convenience
 * and allow easy conversions.
 */
object Colors {
  val PURPLE = new Color(128, 0, 255)
  val CREAM = new Color(255, 0xe0, 0xc0)
  val BURGUNDY = new Color(0xff, 0x40, 0x80)
  val DARK_GREEN = new Color(0x00, 0x80, 0x00)

  /**
   * provide implicit conversions of Colors to Vectors
   */
  implicit def color2vector(c: Color) = Vector(c.getRed, c.getGreen, c.getBlue)

  /**
   * Provide implicit conversions of vector to colors.
   * <p>
   * Values out of RGB space are scaled back into it to preserve
   * hue and saturation.
   */
  implicit def vector2color(v: Vector3) = {
    val adjusted = attenuated(v)
    def rgbConstrain(pos:Double) : Int = {
      val v = pos.toInt
      if (v > 255) 255 else if (v < 0) 0 else v
    }
    new Color(rgbConstrain(adjusted.x), rgbConstrain(adjusted.y), rgbConstrain(adjusted.z))
  }

  /**
   * Pick a color between two colors.
   * <p>
   * If the two colors "from" and "to" are treated as points in 3-dimensional
   * RGB space, then on the line between these two points, select a third point
   * at the proportional distance to the "to" point from the "from" point, where
   * that proportion "howfar" is between 0 and 1.
   */
  def colorMerge(from: Color, to: Color, howFar: Double) = {
    from + ((to - from) * howFar)
  }

  /**
   * This slightly faster version of colorMerge avoids intermediary
   * creation of Vector3 objects.
   */
  def fastColorMerge(from: Color, to: Color, howFar: Double): Color = {
    val r = from.getRed * (1 - howFar) + to.getRed * howFar
    val g = from.getGreen * (1 - howFar) + to.getGreen * howFar
    val b = from.getBlue * (1 - howFar) + to.getBlue * howFar
    new Color(r toInt, g toInt, b toInt)
  }

  /**
   * Squish a color vector into RGB space constraints (max value 255)
   * while preserving hue and saturation.
   */
  def attenuated(color: Vector3) = {
    val max = color.max
    if (max > 255) {
      color * (255.0 / max)
    } else if (color.min < 0) {
      Vector3.ORIGIN
    } else {
      color
    }
  }

}