package net.aethersanctum.fractus

import java.lang.Thread._
import java.lang.Thread
import javax.imageio.ImageIO
import java.io.File

/**
 * Callbacks for messages from the GUI.
 */
trait GuiMessageReceiver {
  def handleSaveMessage()
  def handleQuitMessage()
  def handleNewFractalMessage(fractalName:String)
}

/**
 * FractusApp runs one DrawingSession at a time to handle the rendering of a fractal.
 * If the user selects a new fractal to draw, then we start a new DrawingSession.
 */
class FractusApp(imageWidth:Int, imageHeight:Int) extends GuiMessageReceiver {

  private val window = new FractusWindow(imageWidth, imageHeight, this)
  window.pack()
  window.setVisible(true)

  /**
   * Encapsulate mutable items relating to a drawing session in an immutable container.
   */
  class DrawSession(fractal:RuleBasedFractal) {
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

    def getPixelsDrawn = drawRunner.getPixelCount
  }

  private var drawSession : Option[DrawSession] = None

  /**
   * Contains a thread which after starting updates the display periodically
   */
  class RefreshTicker {
    private var started = false
    private val thread = new Thread(new Runnable() {
      override def run() {
        println("Starting refresh ticker")
        while(true) {
          sleep(1000)
          drawSession.foreach { session =>
            val ratio = session.getPixelsDrawn.toDouble / (imageWidth * imageHeight)
            window.updateCountLabel(" " + ratio)
          }
        }
      }
    });
    def start() {
      if (!started) {
        thread.start()
        started = true
      }
    }
  }

  val refreshTicker = new RefreshTicker

  /**
   * Stops whatever we're doing (if anything), finds next fractal to draw, and
   * sets up a new drawing session to run it.
   */
  def startDrawing(name:String) {
    drawSession.foreach{ _.stop() }

    val fractal = RuleBasedFractal findByName name
    window.blackenCanvas()

    drawSession = Some(new DrawSession(fractal))
    drawSession.foreach{ _.start() }
    refreshTicker.start()
  }

  override def handleSaveMessage {
    println("Received Save Message")
    ImageIO.write(window.getSaveableImage, "png", new File("fractus.png"))
  }

  override def handleQuitMessage {
    println("Received Quit Message")
    System.exit(0)
  }
  
  override def handleNewFractalMessage(fractalName:String) {
    println("Received New Fractal Message:" + fractalName)
    startDrawing(fractalName)
  }
}