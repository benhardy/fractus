package net.aethersanctum.fractus

import java.awt.Color
import java.awt.Graphics
import math._
import net.aethersanctum.fractus.Colors._

import annotation.tailrec
import java.util.concurrent.atomic.{AtomicLong, AtomicBoolean}

/**
 * whenever paint levels change in a PaintBucket, a PaintObserver can be notified
 * to update the visible display accordingly.
 */
trait PaintObserver {
  def updateDisplayPoint(x: Int, y: Int, red: Double, green:Double, blue:Double, hits:Double): Boolean
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

  private val canvas : Canvas = new MegaCanvas(width, height, this)
  private val brush : Brush = new InitiallyBlurryBrush(canvas)

  val SCALE = 0.2 * fractal.scale
  private val (xaspect: Double, yaspect: Double) = if (height > width)
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
  private val keepGoing = new AtomicBoolean(true)

  /**
   * A rough count of how many pixels have been written. Updated periodically by loopit().
   * AtomicLong since updater thread will be reading this.
   */
  private val roughPixelCount: AtomicLong = new AtomicLong(0L)

  val COLOR_SCALING_FACTOR = 0.08

  /**
   * called every so often by the updater thread
   */
  def pixelsDrawn = roughPixelCount.get()

  /**
   * called by GUI thread when we want to shut this DrawRunner down
   */
  def stop() {
    keepGoing.set(false)
  }

  override def run() {
    System.out.println("DrawRunner is running")
    val pos = Vector(0.0, 0.0)
    val color = Color.BLACK
    loopit(RuleState.INITIAL, pos, color)
  }

  /**
   * Main rendering loop. For each pass we look at the current rule state
   * to get the next rule to be applied, apply it to generate new pen
   * position and color, and paint that spot with the current brush.
   */
  @tailrec
  final def loopit(ruleState: RuleState, oldPos: Vector2, oldColor: Color, pixelCount: Long = 0L) {
    if (!keepGoing.get()) {
      println("DrawRunner got a stop signal")
    }
    else {
      val nextIndex = fractal.nextIndex(ruleState)
      val rule = fractal.rules(nextIndex)
      val new_state = RuleState(current = nextIndex, previous = ruleState.current)

      val (pos, color) = rule(oldPos, oldColor)
      val xm = (width * (0.5 + SCALE * pos.x * xaspect)).toInt
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
      if (newPixelCount.toShort == 0) {
        roughPixelCount.set(newPixelCount)
      }
      loopit(new_state, pos, color, newPixelCount)
    }
  }

  /**
   * Update a point on the display according to its corresponding PaintBucket after
   * that got rendered. This is a callback called by MegaCanvas.paint().
   *
   * Performs logarithmic scaling of color based on the amount of paint in the bucket.
   * Furthermore, colors are attenuated to fit within RGB range, and preserve saturation and hue.
   *
   * @return true if point is in visible canvas area, false otherwise
   */
  override def updateDisplayPoint(xi: Int, yi: Int, red: Double, green:Double, blue:Double, hits:Double): Boolean = {
    if (xi >= 0 && yi >= 0 && xi < width && yi < height) {
      val colorScale = COLOR_SCALING_FACTOR * log(hits)
      val hitScaler = colorScale / hits
      var scaledRed = red * hitScaler
      var scaledGreen = green * hitScaler
      var scaledBlue = blue * hitScaler
      val maxColor = max(scaledRed, max(scaledGreen, scaledBlue))

      if (maxColor > 255) {
        val maxer = 255.0 / maxColor
        scaledRed *= maxer
        scaledGreen *= maxer
        scaledBlue *= maxer
      } else {
        val minColor = min(scaledRed, min(scaledGreen, scaledBlue))
        if (minColor < 0.0) {
          scaledRed = 0
          scaledGreen = 0
          scaledBlue = 0
        }
      }
      val fixedColor = new Color(scaledRed.toInt, scaledGreen.toInt, scaledBlue.toInt)
      graphics.setColor(fixedColor)
      graphics.drawRect(xi, yi, 0, 0)
      true
    }
    else {
      false
    }
  }
}

