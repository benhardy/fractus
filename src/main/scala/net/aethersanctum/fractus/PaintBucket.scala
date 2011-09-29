package net.aethersanctum.fractus

import java.awt.Color

/**
 * a bucket that collects paint.
 * used to represent a point in a canvas.
 */
class PaintBucket {
  var red: Double = 0.0;
  var green: Double = 0.0;
  var blue: Double = 0.0;
  var hits: Double = 0.0;

  def paint(color:Color, intensity:Double) = {
    red +=   color.getRed * intensity
    green += color.getGreen * intensity
    blue +=  color.getBlue * intensity
    hits +=  intensity
  }

  def colorVector = Vector(red, green, blue)
}