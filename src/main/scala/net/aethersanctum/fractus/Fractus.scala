package net.aethersanctum.fractus

import java.awt.image.BufferedImage
import java.io.{File}
import javax.imageio.ImageIO
import java.awt.event.{ActionListener, ActionEvent}
import javax.swing._
import examples.Examples
import java.awt.{Color, BorderLayout, Graphics, Dimension}
import java.lang.Thread.sleep

object SwingUtil {

  /** convert a code block to a Swing ActionListener where required */
  implicit def doSomething(f: => Any) = new ActionListener() {
    override def actionPerformed(e:ActionEvent) = f
  }
  /** convert a code block to a Swing ActionListener where required */
  implicit def doSomething(f: ActionEvent => Any) = new ActionListener() {
    override def actionPerformed(e:ActionEvent) {
      f(e)
    }
  }

  /** create a button with an attached Action block */
  def button(label:String)(todo : =>Any) = {
    val gadget= new JButton(label)
    gadget.addActionListener(todo)
    gadget
  }  
}

/**
 * Swing GUI for the app.
 */
class Fractus(var fractalName:String, var rules:RuleSet, imgWidth:Int, imgHeight:Int) extends JFrame {
  import SwingUtil._

  System.out.println("new Fractus created @"+imgWidth+"x"+imgHeight)
  setPreferredSize(new Dimension(imgWidth+50,imgHeight+50))
  val img = new BufferedImage( imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB )
  val img2d = img.createGraphics
  //setDefaultLookAndFeelDecorated(true)
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

  val drawPanel = createDrawPanel


  val countLabel = new JLabel("")
  val buttonPanel = createButtonPanel(countLabel)

  val scroller = new JScrollPane( drawPanel )
  scroller.setMinimumSize(new Dimension(400,400))

  getContentPane.setLayout( new BorderLayout() )
  getContentPane.add( scroller, BorderLayout.CENTER )
  getContentPane().add( buttonPanel, BorderLayout.SOUTH )



  var drawRunner = new DrawRunner( img2d, rules, imgWidth,imgHeight )
  private var drawThread = new Thread( drawRunner )
  private def getDrawRunner = drawRunner

  private val refreshTicker = new Runnable() {
    override def run() = {
        while(true) {
          sleep(1000)
          val ratio = getDrawRunner.getPixelCount.toDouble / (imgWidth * imgHeight)
          countLabel.setText(" " + ratio)
          drawPanel.repaint()
        }
    }
  };

  def startThreads() {
    drawThread.start()
    new Thread(refreshTicker).start()
  }

  /**
   * stop whatever we're doing and start drawing a new fractal with the given name
   */
  def startAfresh(name:String) = {
    rules = Examples(fractalName) getOrElse {
      throw new IllegalArgumentException("unknown ruleset " + name)
    }
    fractalName = name
    drawRunner.stop()
    img2d.setColor(Color.BLACK)
    img2d.fillRect(0,0,imgWidth,imgHeight);
    drawPanel.repaint();
    drawRunner = new DrawRunner(img2d, rules, imgWidth, imgHeight)
    drawThread = new Thread(drawRunner)
    drawThread.start()
  }
  
  def createDrawPanel : JPanel = {
    new JPanel {
      val myPrefSize = new Dimension(imgWidth, imgHeight)

      override def isOpaque = true

      def paintComponent = {
        val g = getGraphics
        g.drawImage(img, 0, 0, null)
      }

      override def paint(g: Graphics) {
        g.drawImage(img, 0, 0, null)
      }

      override def getPreferredSize = myPrefSize
    }
  }


  def createImageSelector(items: Array[String]): JComboBox = {

    val b = new JComboBox()
    items.foreach {
      b addItem _
    }
    b.setSelectedItem(fractalName)
    b.addActionListener({
      (e: ActionEvent) =>
        val res = e.getSource.asInstanceOf[JComboBox].getSelectedItem.asInstanceOf[String]
        startAfresh(res)
    })
    b

  }

  def createButtonPanel(countLabel:JLabel) : JPanel = {
    val buttonPanel = new JPanel()
    buttonPanel.add(button("Save") {
      ImageIO.write(img, "png", new File("fractus.png"))
    })

    buttonPanel.add(button("Refresh") {
      drawPanel.repaint()
    })

    buttonPanel.add(button("Quit") {
      System.exit(0)
    })
    buttonPanel.add(new JLabel("Pixel count:"))
    buttonPanel.add(countLabel)
    val items: Array[String] = Examples.items.keySet.toArray.sortWith((_: String).compareTo(_: String) > 0)
    buttonPanel.add(createImageSelector(items))
    buttonPanel
  }

}
