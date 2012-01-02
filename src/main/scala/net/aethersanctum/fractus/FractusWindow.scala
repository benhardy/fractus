package net.aethersanctum.fractus

import java.awt.image.BufferedImage
import java.awt.event.ActionEvent
import examples.Examples
import java.awt.{Color, BorderLayout, Graphics, Dimension}

import util.SwingUtil._
import javax.swing._

/**
 * Swing GUI for the app. Delegates handling of user messages to messageReceiver.
 */
class FractusWindow(imgWidth: Int, imgHeight: Int, messageReceiver: GuiMessageReceiver) extends JFrame {

  System.out.println("new Fractus created @" + imgWidth + "x" + imgHeight)

  setPreferredSize(new Dimension(imgWidth + 50, imgHeight + 50))
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

  private val img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB)
  private val img2d = img.createGraphics

  private val drawPanel = createDrawPanel
  private val imageSelector = createImageSelector

  private val countLabel = new JLabel("")
  private val buttonPanel = createButtonPanel(countLabel)

  private val scroller = new JScrollPane(drawPanel)
  scroller.setMinimumSize(new Dimension(400, 400))

  getContentPane.setLayout(new BorderLayout())
  getContentPane.add(scroller, BorderLayout.CENTER)
  getContentPane.add(buttonPanel, BorderLayout.SOUTH)


  def updateCountLabel(label: String) {
    countLabel.setText(label)
    drawPanel.repaint()
  }

  def getDrawingCanvasGraphics = img2d

  /**
   * stop whatever we're doing and start drawing a new fractal with the given fractalName
   */
  def blackenCanvas() {
    img2d.setColor(Color.BLACK)
    img2d.fillRect(0, 0, imgWidth, imgHeight)
    drawPanel.repaint();
  }

  def showFractalSelection(name:String) = {
    imageSelector.setSelectedItem(name)
  }

  def createDrawPanel: JPanel = {
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


  def createImageSelector: JComboBox = {
    val selector = new JComboBox()

    Examples.getFractalNames.foreach {
      selector addItem _
    }
    selector.addActionListener {
      (e: ActionEvent) =>
        val newFractalName = e.getSource.asInstanceOf[JComboBox].getSelectedItem.asInstanceOf[String]
        messageReceiver.handleNewFractalMessage(newFractalName)
    }
    selector
  }

  def createButtonPanel(countLabel: JLabel): JPanel = {
    val buttonPanel = new JPanel()
    buttonPanel.add(button("Save") {
      messageReceiver.handleSaveMessage(img)
    })

    buttonPanel.add(button("Refresh") {
      drawPanel.repaint()
    })

    buttonPanel.add(button("Quit") {
      messageReceiver.handleQuitMessage()
    })
    buttonPanel.add(new JLabel("Pixel count:"))
    buttonPanel.add(countLabel)
    buttonPanel.add(imageSelector)
    buttonPanel
  }

}
