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
    Transform((p:Vector2) => secondary(primary(p)) )
  }

  
  def translate(x:Double,y:Double) = combine(Transform.translate(x,y))
  def scale(x:Double,y:Double) = combine(Transform.scale(x,y))
  def scale(s:Double) = combine(Transform.scale(s))
  def rotate(a:Double) = combine(Transform.rotate(a))
  def polar = combine(Transform.polar)
  def invertRadius = combine(Transform.invertRadius)
  def sine = combine(Transform.sine)
}

case class AffineTransform(a:Double, b:Double, c:Double,
                           d:Double, e:Double, f:Double) extends Transform {
  def combine(o:AffineTransform) = {
    new AffineTransform(a * o.a + d * o.b,
      b * o.a + e * o.b, c * o.a
        + f * o.b + o.c, a * o.d
        + d * o.e, b * o.d + e
        * o.e, c * o.d + f * o.e
        + o.f
    )
  }
  override def translate(x:Double,y:Double) = combine(Transform.translate(x,y))
  override def scale(x:Double,y:Double) = combine(Transform.scale(x,y))
  override def scale(s:Double) = combine(Transform.scale(s))
  override def rotate(a:Double) = combine(Transform.rotate(a))
  override def apply(p:Vector2): Vector2 = {
    Vector(a * p.x + b * p.y + c, d * p.x + e * p.y + f)
  }
}

object Transform {
  val none = AffineTransform(1, 0, 0, 0, 1, 0)
  def translate(x:Double, y:Double) = AffineTransform(1,0,x,0,1,y)

  def scale(x:Double, y:Double) = AffineTransform(x,0,0,0,y,0)

  def scale(s:Double) = AffineTransform(s,0,0,0,s,0)

  /**
   * convert vector functions to transforms at will
   */
  implicit def apply(vf: Vector2 => Vector2) = new Transform {
    override def apply(in:Vector2) = vf(in)
  }

  def rotate(angle:Double) = {
		val rads = toRadians(angle)
		new AffineTransform(cos(rads), -sin(rads), 0, sin(rads), cos(rads), 0);
	}
  def inPolarSpace(f: PolarVector => PolarVector) = new Transform {
    override def apply(in:Vector2) : Vector2 = {
      Vector.polarToCartesian(f(Vector.cartesianToPolar(in)))
    }
  }
  val polar =  Transform(
    (in:Vector2) => {
   	  val r = sqrt(in.x * in.x + in.y * in.y)
			val t = atan2(in.y, in.x)
			Vector(r, t)
    }
  )
  val cartesian = Transform(
    (in:Vector2) => {
   	  val (r, t) = (in.x, in.y)
			Vector(r*cos(t), r*sin(t))
    }
  )
  val invertRadius =  Transform(
    (in:Vector2) => {
   	  val r2 = in.x * in.x + in.y * in.y + 1
			Vector(in.x/r2, in.y/r2)
    }
  )

  val sine =  Transform(
    (in:Vector2) => Vector(sin(in.x), sin(in.y))
  )

}
