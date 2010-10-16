package net.aethersanctum.fractus

import java.awt.image.BufferedImage
import java.io.{File}
import javax.imageio.ImageIO
import java.awt.event.{ActionListener, ActionEvent}
import javax.swing._
import ruleset.RuleMap
import java.awt.{Color, BorderLayout, Graphics, Dimension}

class Fractus(var fractalName:String, var rules:RuleSet, imgWidth:Int, imgHeight:Int) extends JFrame {

  /** convert a code block to a Swing ActionListener where required */
  implicit def doSomething(f: => Any) = new ActionListener() {
    override def actionPerformed(e:ActionEvent) = f
  }
  /** convert a code block to a Swing ActionListener where required */
  implicit def doSomething(f: ActionEvent => Any) = new ActionListener() {
    override def actionPerformed(e:ActionEvent) = f(e)
  }

  System.out.println("new Fractus created @"+imgWidth+"x"+imgHeight)
  setPreferredSize(new Dimension(imgWidth+50,imgHeight+50))
  val img = new BufferedImage( imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB )
  val img2d = img.createGraphics
  //setDefaultLookAndFeelDecorated(true)
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

  val drawPanel = new JPanel {
    val myPrefSize = new Dimension( imgWidth, imgHeight )
    override def isOpaque = true
    def paintComponent = {
      val g = getGraphics
      g.drawImage( img, 0, 0 , null )
    }
    override def paint(g:Graphics) = {
      g.drawImage( img, 0, 0, null )
    }
    override def getPreferredSize = myPrefSize
  };


  val buttonPanel = new JPanel()
  buttonPanel.add( {
    val save= new JButton( "Save" )
    save.addActionListener {   // Save as PNG
      ImageIO.write(img, "png", new File("fractus.png"))
    }
    save
  })
  buttonPanel.add({
    val refresh= new JButton( "Refresh" )
    refresh.addActionListener{ drawPanel.repaint() }
    refresh
  })
  buttonPanel.add({
    val quit= new JButton( "Quit" )
    quit.addActionListener { System.exit(0) }
    quit
  })
  buttonPanel.add( new JLabel("Pixel count:") )
  private val countLabel = new JLabel("")
  buttonPanel.add( countLabel )
  val items:Array[String] = RuleMap.items.keySet.toArray
  buttonPanel.add( {
    val b = new JComboBox()
    items.foreach{ b.addItem }
    b.setSelectedItem(fractalName)
    b.addActionListener({ (e:ActionEvent) =>
      val res = e.getSource().asInstanceOf[JComboBox].getSelectedItem.asInstanceOf[String]
      startAfresh(res)
    })
    b
  })

  val scroller = new JScrollPane( drawPanel )
  scroller.setMinimumSize(new Dimension(400,400))

  getContentPane().setLayout( new BorderLayout() )
  getContentPane().add( scroller, BorderLayout.CENTER )
  getContentPane().add( buttonPanel, BorderLayout.SOUTH )

  private def getDrawRunner = drawRunner

  private val refreshTicker = new Runnable() {
    override def run = {
        while(true) {
          java.lang.Thread.sleep(1000);
          val ratio = getDrawRunner.getPixelCount.toDouble / (imgWidth * imgHeight);
          countLabel.setText(" " + ratio);
          drawPanel.repaint();
        }
    }
  };


  var drawRunner = new DrawRunner( img2d, rules, imgWidth,imgHeight )
  private var drawer = new Thread( drawRunner )

  def startThreads = {
    drawer.start()
    new Thread(refreshTicker).start()
  }

  def startAfresh(name:String) = {
    rules = RuleMap(fractalName) match {
      case Some(x) => x
      case _ => throw new IllegalArgumentException("unknown ruleset " + name)
    }
    fractalName = name
    drawRunner.stop
    img2d.setColor(Color.BLACK)
    img2d.fillRect(0,0,imgWidth,imgHeight);
    drawRunner = new DrawRunner(img2d, rules, imgWidth, imgHeight)
    drawer = new Thread(drawRunner)
    drawer.start
  }

}
