package net.aethersanctum.fractus

import examples.Examples
import javax.imageio.ImageIO
import java.io.File
import java.awt.image.RenderedImage
import java.util.concurrent.{TimeUnit, Callable, ScheduledExecutorService, Executors}


/**
 * Callbacks for messages from the GUI.
 */
trait GuiMessageReceiver {
  def handleSaveMessage(image:RenderedImage)

  def handleQuitMessage()

  def handleNewFractalMessage(fractalName: String)
}

/**
 * FractusApp runs one DrawingSession at a time to handle the rendering of a fractal.
 * If the user selects a new fractal to draw, then we start a new DrawingSession.
 */
class FractusApp(imageWidth: Int, imageHeight: Int) extends GuiMessageReceiver {

  private val window = new FractusWindow(imageWidth, imageHeight, this)
  window.pack()
  window.setVisible(true)

  /**
   * Encapsulate mutable items relating to a drawing session in an immutable container.
   */
  class DrawSession(fractal: RuleBasedFractal) {
    private val drawRunner = new DrawRunner(window.getDrawingCanvasGraphics, fractal, imageWidth, imageHeight)
    private val drawThread = new Thread(drawRunner)

    def start() {
      println("Starting DrawRunner Thread")
      drawThread.start()
    }

    def stop() {
      println("Starting DrawRunner Thread")
      drawRunner.stop()
      try {
        drawThread.interrupt()
      } catch {
        case e: Exception => println("Caught an exception interrupting drawrunner but we don't care")
      }
    }

    def pixelsDrawn = drawRunner.pixelsDrawn
  }

  private var drawSession: Option[DrawSession] = None

  /**
   * Contains a thread which after starting updates the display periodically
   */
  class RefreshTicker extends Runnable {
    val REFRESH_INTERVAL_MILLIS = 10000
    val TOTAL_IMAGE_PIXELS = (imageWidth * imageHeight)
    private val executor =  Executors newScheduledThreadPool 1
    var timer = 0
    override def run() {
      drawSession.foreach { session =>
          timer = timer + 1
          val ratio = session.pixelsDrawn.toDouble / TOTAL_IMAGE_PIXELS
          val rate = ratio / timer
          window.updateCountLabel("%1.1f total, rate %.1f/sec".format(ratio, rate))
      }
    }

    def start() {
      println("Starting refresh ticker")
      executor.scheduleAtFixedRate(this, REFRESH_INTERVAL_MILLIS, REFRESH_INTERVAL_MILLIS, TimeUnit.MILLISECONDS)
    }
  }

  val refreshTicker = new RefreshTicker

  /**
   * Stops whatever we're doing (if anything), finds next fractal to draw, and
   * sets up a new drawing session to run it.
   */
  def startDrawing(fractal: RuleBasedFractal) {
    drawSession.foreach {
      _.stop()
    }
    window.blackenCanvas()

    val session = new DrawSession(fractal)
    session.start()
    drawSession = Some(session)
    window.showFractalSelection(fractal.name)
    refreshTicker.start()
  }

  override def handleSaveMessage(image:RenderedImage) {
    println("Received Save Message")
    ImageIO.write(image, "png", new File("fractus.png"))
  }

  override def handleQuitMessage {
    println("Received Quit Message")
    System.exit(0)
  }

  override def handleNewFractalMessage(fractalName: String) {
    println("Received New Fractal Message:" + fractalName)
    startDrawing(Examples(fractalName).getOrElse{
      throw new IllegalStateException("i got told to draw something i don't know")
    })
  }
}