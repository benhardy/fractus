package net.aethersanctum.fractus

import java.awt.Color
import java.awt.Graphics
import Math._
import net.aethersanctum.fractus.Colors._
import net.aethersanctum.fractus.Vector._

import annotation.tailrec
import java.util.concurrent.atomic.AtomicBoolean

class DrawRunner(graphics: Graphics, rules: RuleSet, width: Int, height: Int)
        extends Runnable {

  val displayUpdater: ((Int, Int, PaintBucket) => Boolean) = updatePointDisplay // see below
  val canvas = new MegaCanvas(width, height, displayUpdater)
  //  val brush = new InitiallyBlurryBrush(canvas)
  val brush = new AntiAliasedPointBrush(canvas)

  val SCALE = 0.2 * rules.scale
  val (xaspect: Double, yaspect: Double) = if (height > width)
    (1.0, width.toDouble / height)
  else
    (height.toDouble / width, 1.0)
  var pixelCount: Long = 0

  System.out.println("new DrawRunner created @" + width + "x" + height);
  graphics.setColor(Color.WHITE)
  System.out.println("DrawRunner created")


  private val keepGoing: AtomicBoolean = new AtomicBoolean(true)

  def stop() {
    keepGoing.set(false)
  }

  override def run() = {
    System.out.println("DrawRunner is running");
    val pos = Vector(0.0, 0.0)
    val color = Color.BLACK

    loopit(rules.state, pos, color)
  }

  @tailrec
  final def loopit(ruleState:RuleSetRunState, oldPos: Vector2, oldColor: Color) {
    if (!keepGoing.get()) {
      println("DrawRunner got a stop signal")
    }
    else {
      val (rule, nextRuleState) = ruleState.next
      val color = colorMerge(oldColor, rule.color, rule.colorWeight);
      val pos = rule.transform(oldPos);
      val ppos = pos
      val xp = (width * (0.5 + SCALE * ppos.x * xaspect))
      val yp = (height * (0.5 - SCALE * ppos.y * yaspect))
      val xm = xp.toInt
      val ym = yp.toInt
      if (canvas.containsPoint(xm, ym)) {
        brush.paint(xm, ym, color)
        pixelCount = pixelCount + 1;
      }
      loopit(nextRuleState, pos, color)
    }
  }

  def getPixelCount = pixelCount

  def black = (0.0, 0.0, 0.0)

  private def updatePointDisplay(xi: Int, yi: Int, bucket: PaintBucket): Boolean = {
    if (xi >= 0 && yi >= 0 && xi < width && yi < height) {
      val hits = bucket.hits;
      val colorScale = 0.08 * log(hits);
      val colorVector = bucket.colorVector * (colorScale / hits)
      val highest = colorVector.max
      val fixedColor: Vector3 = if (highest > 255) {
        colorVector * (255.0 / highest)
      }
      else if (colorVector.min < 0) {
        black
      }
      else {
        colorVector // it's all good!
      }
      graphics.setColor(fixedColor);
      graphics.drawRect(xi, yi, 0, 0);
      true
    }
    else {
      false
    }
  }
}

