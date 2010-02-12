package net.aethersanctum.fractus

import java.awt.image.BufferedImage
import java.awt.{BorderLayout, Graphics, Dimension}
import java.io.{File}
import javax.imageio.ImageIO
import java.awt.event.{ActionListener, ActionEvent}
import javax.swing._

class Fractus(rules:RuleSet, imgWidth:Int, imgHeight:Int) extends JFrame {

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
  val save= new JButton( "Save" );
  save.addActionListener {   // Save as PNG
    val file = new File("fractus.png")
    ImageIO.write(img, "png", file)
  }

  val refresh= new JButton( "Refresh" );
  refresh.addActionListener{ drawPanel.repaint }

  val quit= new JButton( "Quit" );
  quit.addActionListener { System.exit(0) }
  
  val buttonPanel = new JPanel();
  buttonPanel.add( save );
  buttonPanel.add( refresh );
  buttonPanel.add( quit );
  buttonPanel.add( new JLabel("Pixel count:") );
  private val countLabel = new JLabel("")
  buttonPanel.add( countLabel )

  val scroller = new JScrollPane( drawPanel )
  scroller.setMinimumSize(new Dimension(400,400))

  getContentPane().setLayout( new BorderLayout() )
  getContentPane().add( scroller, BorderLayout.CENTER )
  getContentPane().add( buttonPanel, BorderLayout.SOUTH )

	private val refreshTicker = new Runnable() {
		override def run = {
        while(true) {
          java.lang.Thread.sleep(1000);
          val ratio = drawRunner.getPixelCount.toDouble / (imgWidth * imgHeight);
          countLabel.setText(" " + ratio);
          drawPanel.repaint();
        }
		}
	};


  val drawRunner = new DrawRunner( img2d, rules, imgWidth,imgHeight )
  private val drawer = new Thread( drawRunner )

	def startThreads = {
		drawer.start()
		new Thread(refreshTicker).start()
	}

}
