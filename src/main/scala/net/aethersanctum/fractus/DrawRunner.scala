package net.aethersanctum.fractus

import java.awt.Color
import java.awt.Graphics
import Math._
import java.lang.System

class DrawRunner(graphics:Graphics, rules:RuleSet, width:Int, height:Int)
        extends Runnable {

  class PointPaint {
    var red: Double = 0;
    var green: Double = 0;
    var blue: Double = 0;
    var hits: Double = 0;
    def paint(color:Color, intensity:Double) = {
      red = red + color.getRed * intensity
      green = green + color.getGreen * intensity
      blue = blue + color.getBlue * intensity
      hits = hits + intensity
    }
    def colorVector = Vector(red,green,blue)
  }
  class Canvas(width:Int,height:Int) extends ((Int,Int)=>PointPaint) {
    println("Initializing Canvas "+width+"x"+height)
    val dots: Array[Array[PointPaint]] = new Array[Array[PointPaint]](height)
    for (row <- 0 until height) {
      dots(row) = new Array[PointPaint](width)
      for (col <- 0 until width) {
        dots(row)(col)= new PointPaint
      }
    }
    override def apply(x:Int, y:Int) = {
      dots(y)(x)
    }
    def in(x:Int, y:Int) = {
      x>0 && y>0 && x<width && y<height
    }
  }
	val canvas = new Canvas(width,height)
	val SCALE = 0.2 * rules.scale
  val (xaspect:Double, yaspect:Double) = if (height>width)
    (1.0, width.toDouble / height)
  else
    (height.toDouble / width, 1.0)
  var pixelCount:Long = 0

  System.out.println("new DrawRunner created @"+width+"x"+height);
	graphics.setColor(Color.WHITE)
	System.out.println("DrawRunner created")

  implicit def color2vector(c:Color) = Vector(c getRed, c getGreen, c getBlue)
  implicit def vector2color(v:Vector3) = {
    val max = v.max
    val adjusted = if (max>255) {
      //println("adjusting scale by "+(255.0/max))
      v * (255.0 / max)
    } else if (v.min<0) {
      Vector(0,0,0)
    } else {
      v
    }
    val r = adjusted.x.toInt
    val g = adjusted.y.toInt
    val b = adjusted.z.toInt
    new Color(
      if (r>255) 255 else if (r<0) 0 else r,
      if (g>255) 255 else if (g<0) 0 else g,  
      if (b>255) 255 else if (b<0) 0 else b)
  }

	def colorMerge( from:Color, to:Color, howFar:Double) = {
    from + ((to-from) * howFar)
	}

  def pixelWidth(hits:Double) = {
    1 + 4 / (hits+1)
  }

	override def run() = {
		System.out.println("DrawRunner is running");
		var pos = Vector(0.0, 0.0)
		var color = Color.BLACK
    val ruleGetProb: (Rule=>Double) = _ weight
		
    var rule = rules.getRules(0)

		while(true) {
			color = colorMerge( color, rule.color, rule.colorWeight );
			pos = rule.transform( pos );
      rule = rules.next
      var ppos = pos
			val xp = ( width  * ( 0.5 + SCALE* ppos.x * xaspect ) )
			val yp = ( height * ( 0.5 - SCALE* ppos.y * yaspect ) )
      val xm = xp.toInt
      val ym = yp.toInt
      if (canvas.in(xm,ym)) {
        val brushWidth = pixelWidth(canvas(xm,ym).hits)
      /*
      if (brushWidth==1) {
        val xspill = xp - xi
        val yspill = yp - yi
        val xhere = 1 - xspill
        val yhere = 1 - yspill
        val topleft  = xhere  * yhere
        val topright = xspill * yhere
        val botleft  = xhere  * yspill
        val botright = xspill * yspill
        updatePoint(xi-1, yi-1, color, topleft)
        updatePoint(xi,   yi-1, color, topright)
        updatePoint(xi-1, yi,   color, botleft)
        updatePoint(xi,   yi,   color, botright)
      }
      else {
      */
        val ystart = yp - brushWidth/2
        var yi = ystart.toInt
        var ystartOff = ystart - yi
        var yPaintLeft = brushWidth
        while(yPaintLeft>0) {
          val yPaintNeeded = min(yPaintLeft, 1-ystartOff)
          val xstart = xp - brushWidth/2
          var xi = xstart.toInt
          var xstartOff = xstart - xi
          var xPaintLeft = brushWidth
          while(xPaintLeft > 0) {
            var xPaintNeeded = min(yPaintLeft, 1-xstartOff)
            var stroke = xPaintNeeded * yPaintNeeded / (brushWidth * brushWidth)
            updatePoint(xi,yi, color, stroke)
            xPaintLeft = xPaintLeft - xPaintNeeded
            xi = xi + 1
            xstartOff=0
          }
          yPaintLeft = yPaintLeft - yPaintNeeded
          yi = yi + 1
          ystartOff=0
        }

        pixelCount = pixelCount+1;
      }

		}
  }

  def getPixelCount = pixelCount


  private def updatePoint(xi:Int, yi:Int, color:Color, intensity:Double) {
		if (xi>=0 && yi>=0 && xi<width && yi<height) {
      canvas(xi,yi).paint(color, intensity)
			val hits = canvas(xi,yi).hits;
			val colorScale = 0.08* log(hits);
      val colorVector =  canvas(xi,yi).colorVector  * (colorScale/hits)
			val highest = colorVector.max
      val fixedColor:Vector3 = if (highest>255) {
				colorVector * (255.0/highest)
			}
			else if (colorVector.min < 0) {
				(0,0,0)
			}
      else {
        colorVector // it's all good!
      }
			graphics.setColor( fixedColor );
			graphics.drawRect(xi,yi,0,0);
		}
	}
}

