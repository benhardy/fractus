package net.aethersanctum.fractus

import java.awt.Color


object Colors {
  val PURPLE = new Color(128,0,255)
  val CREAM = new Color(255,0xe0,0xc0)
  val BURGUNDY = new Color(0xff,0x40,0x80)


  def colorMerge( from:Color, to:Color, howFar:Double) = {
    from + ((to-from) * howFar)
  }

  implicit def color2vector(c:Color) = Vector(c getRed, c getGreen, c getBlue)

  implicit def vector2color(v:Vector3) = {
    val max = v.max
    val adjusted = if (max>255) {
      //println("adjusting scale by "+(255.0/max))
      v * (255.0 / max)
    } else if (v.min<0) {
      Vector(0,0,0)
    } else {
      v
    }
    val r = adjusted.x.toInt
    val g = adjusted.y.toInt
    val b = adjusted.z.toInt
    new Color(
      if (r>255) 255 else if (r<0) 0 else r,
      if (g>255) 255 else if (g<0) 0 else g,
      if (b>255) 255 else if (b<0) 0 else b)
  }
}