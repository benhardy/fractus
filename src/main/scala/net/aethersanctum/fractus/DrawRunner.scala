package net.aethersanctum.fractus

import java.awt.Color
import java.awt.Graphics
import math._
import net.aethersanctum.fractus.Colors._
import net.aethersanctum.fractus.Vector._

import annotation.tailrec
import java.util.concurrent.atomic.AtomicBoolean
import java.awt.image.{BufferedImage, WritableRaster, Raster}

class DrawRunner(graphics: Graphics, rules: RuleSet, width: Int, height: Int)
        extends Runnable {

  val displayUpdater: ((Int, Int, PaintBucket) => Boolean) = updatePointDisplay // see below
  val canvas = new MegaCanvas(width, height, displayUpdater)
  val brush = new InitiallyBlurryBrush(canvas)
 // val brush = new AntiAliasedPointBrush(canvas)

  val SCALE = 0.2 * rules.scale
  val (xaspect: Double, yaspect: Double) = if (height > width)
    (1.0, width.toDouble / height)
  else
    (height.toDouble / width, 1.0)
  var pixelCount: Long = 0  // should probably be an AtomicLong.

  System.out.println("new DrawRunner created @" + width + "x" + height);
  //graphics.setColor(Color.WHITE)
  System.out.println("DrawRunner created")


  private val keepGoing: AtomicBoolean = new AtomicBoolean(true)

  def stop() {
    keepGoing.set(false)
  }

  override def run() = {
    System.out.println("DrawRunner is running");
    val pos = Vector(0.0, 0.0)
    val color = Color.BLACK

    loopit(new RuleSetRunStateMachine(rules).start, pos, color)
  }

  /**
   * Main rendering loop. For each pass we look at the current rule state
   * to get the next rule to be applied, apply it to generate new pen
   * position and color, and paint that spot with the current brush.
   */
  @tailrec
  final def loopit(ruleState:RuleState, oldPos: Vector2, oldColor: Color) {
    if (!keepGoing.get()) {
      println("DrawRunner got a stop signal")
    }
    else {
      val (rule, nextRuleState) = ruleState.next
      val (pos, color) = rule(oldPos, oldColor)
      val xm = (width  * (0.5 + SCALE * pos.x * xaspect)).toInt
      val ym = (height * (0.5 - SCALE * pos.y * yaspect)).toInt
      if (canvas.containsPoint(xm, ym)) {
        brush.paint(xm, ym, color)
        pixelCount = pixelCount + 1;
      }
      loopit(nextRuleState, pos, color)
    }
  }

  def getPixelCount = pixelCount

  def black = (0.0, 0.0, 0.0)

  val COLOR_SCALING_FACTOR = 0.08

  private def updatePointDisplay(xi: Int, yi: Int, bucket: PaintBucket): Boolean = {
    if (xi >= 0 && yi >= 0 && xi < width && yi < height) {
      val hits = bucket.hits;
      val colorScale = COLOR_SCALING_FACTOR * log(hits)
      val colorVector = bucket.colorVector * (colorScale / hits)
      val fixedColor: Vector3 = attenuateColor(colorVector)
      graphics.setColor(fixedColor);
      graphics.drawRect(xi, yi, 0, 0);
      true
    }
    else {
      false
    }
  }
}

