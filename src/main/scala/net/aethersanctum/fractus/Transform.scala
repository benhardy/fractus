package net.aethersanctum.fractus


import Math._

/**
 * A Transform is a function that transforms one point into another.
 * They can be combined.
 */
trait Transform extends (Vector2 => Vector2) {
  /**
   * by default, combine transforms by chaining them.
   * however, AffineTransforms can be condensed into one.
   */
  // there's gotta be a more concise way to do this
  def combine(secondary:Transform): Transform = {
    val primary = this
    Transform of ((p:Vector2) => secondary(primary(p)) )
  }

  
  def translate(x:Double,y:Double) = combine(Transform.translate(x,y))
  def scale(x:Double,y:Double) = combine(Transform.scale(x,y))
  def scale(s:Double) = combine(Transform.scale(s))
  def rotate(a:Double) = combine(Transform.rotate(a))
  def polar = combine(Transform.polar)
  def invertRadius = combine(Transform.invertRadius)
  def sine = combine(Transform.sine)
}

class AffineTransform(a:Double,b:Double,c:Double,d:Double,e:Double,f:Double) extends Transform {
  def combine(o:AffineTransform) = {
    new AffineTransform(a * o.A + d * o.B,
					b * o.A + e * o.B, c * o.A
							+ f * o.B + o.C, a * o.D
							+ d * o.E, b * o.D + e
							* o.E, c * o.D + f * o.E
							+ o.F
    )
  }
  override def translate(x:Double,y:Double) = combine(Transform.translate(x,y))
  override def scale(x:Double,y:Double) = combine(Transform.scale(x,y))
  override def scale(s:Double) = combine(Transform.scale(s))
  override def rotate(a:Double) = combine(Transform.rotate(a))
  override def apply(p:Vector2): Vector2 = {
    Vector(a * p.x + b * p.y + c, d * p.x + e * p.y + f)
  }
  protected def A:Double = this.a
  protected def B:Double = this.b
  protected def C:Double = this.c
  protected def D:Double = this.d
  protected def E:Double = this.e
  protected def F:Double = this.f
}

object Transform {
  val none = new AffineTransform(1, 0, 0, 0, 1, 0)
  def translate(x:Double, y:Double) = new AffineTransform(1,0,x,0,1,y)

  def scale(x:Double, y:Double) = new AffineTransform(x,0,0,0,y,0)

  def scale(s:Double) = new AffineTransform(s,0,0,0,s,0)

  def rotate(angle:Double) = {
		val rads = toRadians(angle)
		new AffineTransform(cos(rads), -sin(rads), 0, sin(rads), cos(rads), 0);
	}
  def inPolarSpace(f: PolarVector => PolarVector) = new Transform {
    override def apply(in:Vector2) : Vector2 = {
      Vector.polarToCartesian(f(Vector.cartesianToPolar(in)))
    }
  }
  val polar =  new Transform {
    override def apply(in:Vector2) : Vector2 = {
   	  val r = sqrt(in.x * in.x + in.y * in.y)
			val t = atan2(in.y, in.x)
			Vector(r, t)
    }
  }
  val cartesian =  new Transform {
    override def apply(in:Vector2) : Vector2 = {
   	  val r = in.x
			val t = in.y
			Vector(r*cos(t), r*sin(t))
    }
  }
  val invertRadius =  new Transform {
    override def apply(in:Vector2) : Vector2 = {
   	  val r2 = in.x * in.x + in.y * in.y + 1
			Vector(in.x/r2, in.y/r2)
    }
  }

  val sine =  new Transform {
    override def apply(in:Vector2) = Vector(sin(in.x), sin(in.y))
  }
  /**
   * convert a point to point function into a transform
   */
  implicit def of(f: Vector2 => Vector2) = new Transform {
    override def apply(p:Vector2) : Vector2 = {
      f(p)
    }
  }
}