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

  implicit def color2vector(c: Color) = Vector(c getRed, c getGreen, c getBlue)

  def colorMerge(from: Color, to: Color, howFar: Double) = {
    from + ((to - from) * howFar)
  }

  implicit def vector2color(v: Vector3) = {
    val max = v.max
    val adjusted = if (max > 255) {
      //println("adjusting scale by "+(255.0/max))
      v * (255.0 / max)
    } else if (v.min < 0) {
      Vector(0, 0, 0)
    } else {
      v
    }
    val r = adjusted.x.toInt
    val g = adjusted.y.toInt
    val b = adjusted.z.toInt
    new Color(
      if (r > 255) 255 else if (r < 0) 0 else r,
      if (g > 255) 255 else if (g < 0) 0 else g,
      if (b > 255) 255 else if (b < 0) 0 else b)
  }


  def attenuateColor(colorVector: Vector3): Vector3 = {
    val highest = colorVector.max
    if (highest > 255) {
      colorVector * (255.0 / highest)
    }
    else if (colorVector.min < 0) {
      Vector3.ORIGIN
    }
    else {
      colorVector // it's all good!
    }
  }

  def colorMerge2(from: Color, to: Color, howFar: Double): Color = {
    val r = from.getRed * (1 - howFar) + to.getRed * howFar
    val g = from.getGreen * (1 - howFar) + to.getGreen * howFar
    val b = from.getBlue * (1 - howFar) + to.getBlue * howFar
    new Color(r toInt, g toInt, b toInt)
  }
}