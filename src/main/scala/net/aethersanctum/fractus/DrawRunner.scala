package net.aethersanctum.fractus

import java.awt.Color
import java.awt.Graphics
import math._
import net.aethersanctum.fractus.Colors._
import net.aethersanctum.fractus.Vector._

import annotation.tailrec
import java.awt.image.{BufferedImage}
import java.util.concurrent.atomic.{AtomicLong, AtomicBoolean}

/**
 * whenever paint levels change in a PaintBucket, a PaintObserver can be notified
 * to update the visible display accordingly.
 */
trait PaintObserver {
  def updateDisplayPoint(x:Int, y:Int, bucket:PaintBucket) : Boolean
}

/**
 * DrawRunner takes a Graphics object to draw on and a RuleBasedFractal defining
 * a fractal, and using a MegaCanvas to store paint information it
 * performs the rendering of that fractal.
 *
 * Implements Runnable so that it can be run in a background thread.
 * Contains hooks to stop rendering with the stop() method.
 */
class DrawRunner(graphics: Graphics, fractal: RuleBasedFractal, width: Int, height: Int)
        extends Runnable with PaintObserver {

  val canvas = new MegaCanvas(width, height, this)
  val brush = new InitiallyBlurryBrush(canvas)
 // val brush = new AntiAliasedPointBrush(canvas)

  val SCALE = 0.2 * fractal.scale
  val (xaspect: Double, yaspect: Double) = if (height > width)
    (1.0, width.toDouble / height)
  else
    (height.toDouble / width, 1.0)

  System.out.println("new DrawRunner created @" + width + "x" + height);
  //graphics.setColor(Color.WHITE)
  System.out.println("DrawRunner created")

  /**
   * Flag which when set to false by the gui calls to stop() will stop the main loop in loopit().
   * AtomicBoolean since the AWT thread will be writing to this.
   */
  private val keepGoing: AtomicBoolean = new AtomicBoolean(true)
  /**
   * A rough count of how many pixels have been written. Updated periodically by loopit().
   * AtomicLong since updater thread will be reading this.
   */
  private val roughPixelCount: AtomicLong = new AtomicLong(0L)

  /**
   * called every so often by the updater thread
   */
  def getPixelCount = roughPixelCount.get()

  /**
   * called by GUI thread when we want to shut this DrawRunner down
   */
  def stop() {
    keepGoing.set(false)
  }

  override def run() = {
    System.out.println("DrawRunner is running");
    val pos = Vector(0.0, 0.0)
    val color = Color.BLACK

    loopit(new RuleSetRunStateMachine(fractal).start, pos, color)
  }

  /**
   * Main rendering loop. For each pass we look at the current rule state
   * to get the next rule to be applied, apply it to generate new pen
   * position and color, and paint that spot with the current brush.
   */
  @tailrec
  final def loopit(ruleState:RuleState, oldPos: Vector2, oldColor: Color, pixelCount:Long = 0L) {
    if (!keepGoing.get()) {
      println("DrawRunner got a stop signal")
    }
    else {
      val (rule, nextRuleState) = ruleState.next
      val (pos, color) = rule(oldPos, oldColor)
      val xm = (width  * (0.5 + SCALE * pos.x * xaspect)).toInt
      val ym = (height * (0.5 - SCALE * pos.y * yaspect)).toInt
      val newPixelCount = if (canvas.containsPoint(xm, ym)) {
        brush.paint(xm, ym, color)
        pixelCount + 1
      }
      else {
        pixelCount
      }
      // update the rough pixel count infrequently (it's atomic)
      // when least significant 16 bits are all 0.
      if (newPixelCount.toShort == 0)  {
        roughPixelCount.set(newPixelCount)
      }
      loopit(nextRuleState, pos, color, newPixelCount)
    }
  }

  def black = Vector3.ORIGIN

  val COLOR_SCALING_FACTOR = 0.08

  /**
   * Update a point on the display according to its corresponding PaintBucket after
   * that got rendered. This is a callback called by MegaCanvas.paint().
   * 
   * Performs logarithmic scaling of color based on the amount of paint in the bucket.
   * Furthermore, colors are attenuated to fit within RGB range, and preserve saturation and hue.
   * 
   * @return true if point is in visible canvas area, false otherwise
   */
  override def updateDisplayPoint(xi: Int, yi: Int, bucket: PaintBucket): Boolean = {
    if (xi >= 0 && yi >= 0 && xi < width && yi < height) {
      val hits = bucket.hits;
      val colorScale = COLOR_SCALING_FACTOR * log(hits)
      val colorVector = bucket.colorVector * (colorScale / hits)
      val fixedColor: Vector3 = attenuateColor(colorVector)
      graphics.setColor(fixedColor)
      graphics.drawRect(xi, yi, 0, 0)
      true
    }
    else {
      false
    }
  }
}

