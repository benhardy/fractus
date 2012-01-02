package net.aethersanctum.fractus


import math._

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

  def combine(secondary: Transform): Transform = {
    Transform((p: Vector2) => secondary(this.apply(p)))
  }

  // builder methods for creating combined transforms
  def translate(x: Double, y: Double) = combine(Transform.translate(x, y))

  def scale(x: Double, y: Double) = combine(Transform.scale(x, y))

  def scale(s: Double) = combine(Transform.scale(s))

  def rotate(a: Double) = combine(Transform.rotate(a))

  def polar = combine(Transform.polar)

  def invertRadius = combine(Transform.invertRadius)

  def sine = combine(Transform.sine)
}

/**
 * AffineTransforms can be combined in a more efficient way than simply
 * chaining by using matrix multiplication instead.
 */
case class AffineTransform(a: Double, b: Double, c: Double,
                           d: Double, e: Double, f: Double) extends Transform {
  def combine(o: AffineTransform) = {
    new AffineTransform(a * o.a + d * o.b,
      b * o.a + e * o.b, c * o.a
        + f * o.b + o.c, a * o.d
        + d * o.e, b * o.d + e
        * o.e, c * o.d + f * o.e
        + o.f
    )
  }

  override def translate(x: Double, y: Double) = combine(Transform.translate(x, y))

  override def scale(x: Double, y: Double) = combine(Transform.scale(x, y))

  override def scale(s: Double) = combine(Transform.scale(s))

  override def rotate(a: Double) = combine(Transform.rotate(a))

  override def apply(p: Vector2): Vector2 = {
    Vector(a * p.x + b * p.y + c, d * p.x + e * p.y + f)
  }
}

object Transform {
  /**
   * Identity transform. Has no effect.
   */
  val none = AffineTransform(1, 0, 0, 0, 1, 0)

  /**
   * Factory method for translation transformations.
   */
  def translate(x: Double, y: Double) = AffineTransform(1, 0, x, 0, 1, y)

  /**
   * Factory method for scaling transforms where x and y values can be scaled independently.
   */
  def scale(x: Double, y: Double) = AffineTransform(x, 0, 0, 0, y, 0)

  /**
   * Factory method for scaling transforms where x and y values are scaled by same factor.
   */
  def scale(s: Double) = AffineTransform(s, 0, 0, 0, s, 0)

  /**
   * Factory method for rotation transforms about the origin
   */
  def rotate(angleDegrees: Double) = {
    val radians = toRadians(angleDegrees)
    val c = cos(radians)
    val s = sin(radians)
    new AffineTransform(c, -s, 0, s, c, 0);
  }

  /**
   * convert arbitrary vector functions to transforms at will, so that they can be combined, etc.
   */
  implicit def apply(vf: Vector2 => Vector2) = new Transform {
    override def apply(in: Vector2) = vf(in)
  }

  def inPolarSpace(f: PolarVector => PolarVector) = new Transform {
    override def apply(in: Vector2): Vector2 = {
      Vector.polarToCartesian(f(Vector.cartesianToPolar(in)))
    }
  }

  val polar = Transform(
    (in: Vector2) => {
      val r = sqrt(in.x * in.x + in.y * in.y)
      val t = atan2(in.y, in.x)
      Vector(r, t)
    }
  )
  val cartesian = Transform(
    (in: Vector2) => {
      val (r, t) = (in.x, in.y)
      Vector(r * cos(t), r * sin(t))
    }
  )
  val invertRadius = Transform(
    (in: Vector2) => {
      val r2 = in.x * in.x + in.y * in.y + 1
      Vector(in.x / r2, in.y / r2)
    }
  )
  val complexSquared = Transform(
    (Z: Vector2) => {
      val (real, imag) = (Z.x, Z.y)
      Vector(real * real - imag * imag, 2 * real * imag)
    }
  )

  val sine = Transform(
    (in: Vector2) => Vector(sin(in.x), sin(in.y))
  )

}
