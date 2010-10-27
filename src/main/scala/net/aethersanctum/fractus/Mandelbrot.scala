package net.aethersanctum.fractus

import scala.annotation.tailrec
import java.awt.image.BufferedImage
import java.io.{File}
import javax.imageio.ImageIO
import javax.swing._
import java.awt.Color._
import Math._
import java.awt.event._
import java.awt._

case class Complex(val real:Double, val imag:Double) {
  def +(other:Complex) = Complex(real + other.real, imag + other.imag)
  def -(other:Complex) = Complex(real - other.real, imag - other.imag)
  def squared = Complex(real*real - imag*imag, 2*real*imag)
  def magnitudeSquared = real*real + imag*imag
  def magnitude = sqrt(magnitudeSquared)
}

class Histogram(range:Int) {
  private val counts = Array[Int](range)
  def record(value:Int) = {
    counts(value) = counts(value) + 1
  }
  def apply(value:Int) = counts(value)

  def maxCount = counts.foldLeft(0) { (a,b) => max(a,b) }

}

case class MandelbrotView(center:Complex, scale:Double, bailout:Int)

object MandelbrotView {
  val default = MandelbrotView(Complex(0,0), 4, 100)
}

class MandelbrotWindow(imgWidth:Int, imgHeight:Int) extends JFrame {

  /** convert a code block to a Swing ActionListener where required */
  implicit def doSomething(f: => Any) = new ActionListener() {
    override def actionPerformed(e:ActionEvent) = f
  }
  /** convert a code block to a Swing ActionListener where required */
  implicit def doSomething(f: ActionEvent => Any) = new ActionListener() {
    override def actionPerformed(e:ActionEvent) = f(e)
  }

  System.out.println("new MandelbrotWindow created @"+imgWidth+"x"+imgHeight)
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
  buttonPanel.add({
    val z= new JButton( "Zoom Out" )
    z.addActionListener({ (e:ActionEvent) =>
      currentView = currentView.copy(scale = currentView.scale*3)
      drawRunner.quit
      drawRunner = new MandelbrotRender( img2d, imgWidth,imgHeight, currentView )
      new Thread(drawRunner).start
    } )
    z
  })
  val iterationCounts = Array("100","300","1000","3000","10000","30000","100000","300000",
    "1000000","3000000","10000000","30000000")
  val iterationCountSelector =
   {
    val b = new JComboBox()
    iterationCounts.foreach { b.addItem }

    b.setSelectedItem("100")
    b.addActionListener({ (e:ActionEvent) =>
      val bail = e.getSource().asInstanceOf[JComboBox].getSelectedItem.asInstanceOf[String].toInt
      currentView = currentView.copy(bailout = bail)
      drawRunner.quit
      drawRunner = new MandelbrotRender( img2d, imgWidth,imgHeight, currentView )
      new Thread(drawRunner).start
    })
    b
  }
  buttonPanel.add(iterationCountSelector)
  buttonPanel.add( {
    val options = Map(
         "default" -> MandelbrotView.default,
         "pretty" -> MandelbrotView(
                      center=Complex(-0.7490766043418511,-0.047737652356764096),
                      scale=7.023E-9, bailout=100000),
          "crazyjulia" -> MandelbrotView(
                      center=Complex(0.3083774279710356,-0.025192567417452023),
                      scale=1.0019936409854133E-12,
                      bailout=30000),
        "purdyspiralz" -> MandelbrotView(
                      center=Complex(-0.7498767720968768,-0.018511856601685295),
                      scale = 7.987670563364561E-12,
                      bailout=1000000)

      )
    val b = new JComboBox()
    options.keys.foreach { b.addItem }

    b.setSelectedItem("default")
    b.addActionListener({ (e:ActionEvent) =>
      val key = e.getSource().asInstanceOf[JComboBox].getSelectedItem.asInstanceOf[String]
      options get(key) match {
        case Some(view) => {
          currentView = view
          iterationCountSelector.setSelectedItem(
            if(iterationCounts contains view.bailout)
                view.bailout
            else
                "1000"
          )
          drawRunner.quit
          drawRunner = new MandelbrotRender( img2d, imgWidth,imgHeight, view )
          new Thread(drawRunner).start
        }
      }
    })
    b
  })

  val scroller = new JScrollPane( drawPanel )
  scroller.setMinimumSize(new Dimension(400,400))

  getContentPane().setLayout( new BorderLayout() )
  getContentPane().add( scroller, BorderLayout.CENTER )
  getContentPane().add( buttonPanel, BorderLayout.SOUTH )

  private def getDrawRunner = drawRunner

  var currentView = MandelbrotView.default
  var drawRunner = new MandelbrotRender(img2d, imgWidth, imgHeight, currentView)

  this.setMinimumSize(new Dimension(700,500))
  this.setVisible(true)

  object RefreshTicker extends Runnable {
    val thread = new Thread(this).start
    var doingAnything = false

    def start = {
      doingAnything = true
    }
    def stop = {
      doingAnything = false
    }
    override def run = {
        while(true) {
          java.lang.Thread.sleep(1000)
          if (doingAnything) {
            drawPanel.repaint()
          }
        }
    }
  };

  drawPanel.addMouseListener(new MouseListener() {
    private var pressed:Option[Point] = None
    def mouseExited(e: MouseEvent) = {}

    def mouseEntered(e: MouseEvent) = {}

    def mouseReleased(e: MouseEvent) = {
      pressed match {
        case Some(down) => {
          println("mouseReleased")
          RefreshTicker.stop
          val up = e.getPoint
          println("mouseReleased down="+down+", up="+up)
          val newWidth = abs(up.getX - down.getX)
          println("mouseReleased newWidth="+newWidth)
          val pxCenter = (up.getX + down.getX) / 2
          val pyCenter = (up.getY + down.getY) / 2
          println("mouseReleased pxCenter="+pxCenter+", pyCenter="+pyCenter)
          val newx= currentView.center.real + (pxCenter / imgWidth -0.5) * currentView.scale
          val newy= currentView.center.imag + (pyCenter / imgHeight -0.5) * currentView.scale
          println("mouseReleased newx="+newx+", up="+newy)
          if (newWidth>0) {
          currentView = new MandelbrotView(
            center = new Complex(newx,newy),
            scale = newWidth * currentView.scale /imgWidth,
            bailout = currentView.bailout)
            drawRunner.quit
            drawRunner = new MandelbrotRender( img2d, imgWidth,imgHeight, currentView )
            new Thread( drawRunner ).start

            RefreshTicker.start
          }
        }
      }
      pressed = None
    }

    def mousePressed(e: MouseEvent) = {
      pressed = Some(e.getPoint)
    }

    def mouseClicked(e: MouseEvent) = {   /*
      RefreshTicker.stop
      val point = e.getPoint()
      val newx= center.real + (point.x.toDouble / imgWidth -0.5) *scale
      val newy= center.imag + (point.y.toDouble / imgHeight -0.5) *scale
      center = new Complex(newx,newy)
      scale = scale /2
      drawRunner = new Mandelbrot( img2d, imgWidth,imgHeight, center, scale, bailout )
      drawer = new Thread( drawRunner )
      drawer.start
      RefreshTicker.start
      */
    }
  })

  def startThreads = {
    RefreshTicker.start
  }


}

object Mandelbrot {
  def main(args:Array[String]) = {
    val (width,height) = if (args.length >= 2) {
      (Integer.parseInt(args(0)),Integer.parseInt(args(1)))
    } else {
      (1000,1000)
    }
    val win = new MandelbrotWindow(width,height)
    win.startThreads
  }

}

object Palette {

  val NAVY = new Color(0,32, 96)
  val PINK = new Color(255, 128, 128)
  val OLIVE = new Color(64, 192, 32)
  val PURPLE = new Color(64, 32, 192)
  val GOLD = new Color(0xff,0xe6, 0x25)
  val BURGUNDY = new Color(0xd1, 0x1e, 0x3f)
  val SKY = new Color(0x1e,0x99,0xd2)
  val ORANGE_CREAM = new Color(0xf6,0xc9,0x90)
  val LAVENDER = new Color(0xdf,0xb4,0xe5)
  val DEEP_PURPLE = new Color(0x56,0x1c,0x79)
  val MUSHROOM = new Color(0xb5,0xb4,0x98)
  val STEEL = new Color(0x98,0xa4,0xb5)
  /*
  val palette = Array(BLACK, NAVY, MUSHROOM, PINK, GOLD, DEEP_PURPLE, SKY, RED, STEEL, BLUE,
    ORANGE_CREAM, LAVENDER, OLIVE, PURPLE,  MUSHROOM, PINK, GOLD, DEEP_PURPLE, SKY, RED, STEEL,
    BLUE,  MUSHROOM, PINK, GOLD, DEEP_PURPLE, WHITE
    )
  */
  val palette = 0 until 300 map { i=>
    val col = randomColor
    i & 3 match {
      case 0 => col
      case 1 => col.darker
      case 2 => col.brighter
      case 3 => {
        val avg = (col.getRed + col.getGreen + col.getBlue) /3
        val cAvg = new Color(avg,avg,avg)
        colorSpread(col, cAvg, 0.75)
      }
    }
  } toArray

  def randomColor = {
    val r = (Math.random * 256).toInt
    val g = (Math.random * 256).toInt
    val b = (Math.random * 256).toInt
    new Color(r, g, b)
  }

  def colorSpread(a:Color, b:Color, howFar:Double) : Color = {
    val r = a.getRed + ((b.getRed - a.getRed) * howFar).toInt
    val g = a.getGreen + ((b.getGreen - a.getGreen) * howFar).toInt
    val B = a.getBlue + ((b.getBlue - a.getBlue) * howFar).toInt
    new Color(r,g,B)
  }

}

class MandelbrotRender(graphics:Graphics, width:Int, height: Int,
        view:MandelbrotView) extends Runnable {

  println("new Mandelbrot view="+view)
  val aspect = height.toDouble /width

    case class Bailout(val iterations:Option[Int], val location:Complex, val last:Complex)

    def escapes(C:Complex, bailout:Int):Bailout = {
      @tailrec
      def check(Z:Complex, current:Int, prev:Complex): Bailout = {
        if (current >= bailout ) {
          Bailout(iterations = None, location = Z, last=prev)
        } else {
          // Calculate Z^2. Reuse components to calculate magnitude too.
          val r2 = Z.real * Z.real
          val i2 = Z.imag * Z.imag
          val Zsquared = Complex(r2 - i2, 2*Z.real*Z.imag)
          val magnitudeSquared = r2 + i2
          val next = current +1
          if (magnitudeSquared >= 4.0) {
            Bailout(iterations = Some(next), location = Z, last=prev)
          } else {
            check(Zsquared + C, next, Z) // tail-recursive
          }
        }
      }
      check(C, 0, C)
    }

  private var forgetIt = false

  def quit = {
    forgetIt = true
  }

  def run = {
    val TOP_POWER = 7
    TOP_POWER to 0 by -1 foreach {
      power =>
        val blockSize = 1 << power
        0 until height by blockSize foreach {
          yp =>
            val yf = view.center.imag + view.scale * aspect * (yp.toDouble / height - 0.5)
            val evenRow = ((yp & blockSize) == 0) && (power != TOP_POWER)
            val xOffset = if (evenRow) blockSize else 0
            val xStep = if (evenRow) blockSize * 2 else blockSize
            xOffset until width by xStep foreach {
              xp =>
                if (forgetIt)
                  throw new InterruptedException
                val xf = view.center.real + view.scale * (xp.toDouble / width - 0.5)
                val pos = Complex(xf, yf)
                val result = escapes(pos, view.bailout)
                val extra = result.location // + pos
                val color = result iterations match {
                  case None => Color.BLACK
                  case Some(iter_count) => extra.real match {
                  /*
                  case x if x > 0 => {
                    val angle = atan2(extra.imag, x)/(2*Pi)+0.5
                    if (angle > -0.75) {
                      val col:Int = (abs(angle)*255).toInt
                      new Color(col,0,0)
                    } else {
                      iter_count & 0x07 match {
                        case 0 => BLACK
                        case 1 => RED
                        case 2 => GREEN
                        case 3 => YELLOW
                        case 4 => BLUE
                        case 5 => MAGENTA
                        case 6 => CYAN
                        case 7 => WHITE
                      }
                    }
                  }
                  */
                    case _ => {
                      val modulus = sqrt(extra.magnitudeSquared);
                      val inter = ((1- log(log(modulus))/log(2))-0.3466)/(1.52876-0.3466)
                      //val mu = max(0, iter_count - (log(log(modulus))) / log(2.0))
                      val c = 2.5 * max(0,log(iter_count + inter + 1))
                      val segment = max(c.floor.toInt, 0)
                      val colpos = c - segment
                      try {
                        import Palette._
                        colorSpread(palette(segment%palette.length), palette((segment + 1)%palette.length), colpos)
                      } catch {
                        case e: Exception => {
                          println("color problums")
                          println("modulus=" + modulus)
                          println("iter_count=" + iter_count)
                          //println("mu=" + mu)
                          println("c=" + c)
                          println("segment=" + segment)
                          println("colpos=" + colpos)
                          throw e
                        }
                      }
                    }

                  }
                }
                graphics.setColor(color)
                graphics.fillRect(xp, yp, blockSize, blockSize)
            }
        }
    }
  }
}
