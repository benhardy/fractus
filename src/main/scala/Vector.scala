package net.aethersanctum.fractus

import math._

/**
 * Provides support for both 2 and 3 dimensional vectors in Cartesian space
 */
trait Vector[V <: Vector[V]] {
  /**vector addition/translation */
  def +(v: V): V

  /**vector subtraction */
  def -(v: V): V

  /**multiply this vector by a scalar (scaling) */
  def *(k: Double): V

  /**multiply this vector by the inverse of a scalar (scaling) */
  def /(k: Double): V

  /**dot product between this vector and another */
  def dot(v: V): Double

  /**the scalar length of this vector */
  def length: Double

  /**the highest of all this vector's component values */
  def max: Double

  /**the lowest of all this vector's component values */
  def min: Double

  /**this vector scaled to have a length of 1 */
  def unit = {
    val len = length
    if (len > 0)
      self / len
    else
      self
  }

  /**this vector itself */
  protected def self: V
}

/**
 * 2D vectors in Cartesian space
 */
final class Vector2(a: Double, b: Double) extends Vector[Vector2] {
  def x = a

  def y = b

  def +(v: Vector2) = new Vector2(x + v.x, y + v.y)

  def -(v: Vector2) = new Vector2(x - v.x, y - v.y)

  def *(k: Double) = new Vector2(x * k, y * k)

  def *(v: Vector2) = new Vector2(x * v.x, y * v.y)

  def /(k: Double) = new Vector2(x / k, y / k)

  def dot(v: Vector2) = x * v.x + y * v.y

  def length = sqrt(x * x + y * y)

  def self = this

  def max = if (x > y) x else y

  def min = if (x < y) x else y

  override def equals(other: Any): Boolean = {
    if (other == null) {
      false
    }
    else {
      other match {
        case o: Vector2 => x == o.x && y == o.y
        case _ => false
      }
    }
  }

  override def toString = "<%.3f,%.3f>".format(a, b)
}

object Vector2 {
  val ORIGIN = new Vector2(0, 0)
  val X = new Vector2(1, 0)
  val Y = new Vector2(0, 1)
}

/**
 * 2D vectors in Polar space
 */
final class PolarVector(radius: Double, angle: Double) {
  def r = radius

  def t = angle

  def +(v: PolarVector) = new PolarVector(r + v.r, t + v.t)

  def -(v: PolarVector) = new PolarVector(r - v.r, t - v.t)

  def *(k: Double) = new PolarVector(r * k, t) // scaling only affects radius in polar space
  def /(k: Double) = new PolarVector(r / k, t)

  def spin(dt: Double) = new PolarVector(r, t + dt)

  def length = r
}

object PolarVector {
  def apply(r: Double, t: Double) = new PolarVector(r, t)
}

/**
 * 3D vectors in Cartesian space
 */
final class Vector3(a: Double, b: Double, c: Double) extends Vector[Vector3] {
  def x = a

  def y = b

  def z = c

  def +(v: Vector3) = new Vector3(x + v.x, y + v.y, z + v.z)

  def -(v: Vector3) = new Vector3(x - v.x, y - v.y, z - v.z)

  def *(k: Double) = new Vector3(x * k, y * k, z * k)

  def /(k: Double) = new Vector3(x / k, y / k, z / k)

  def dot(v: Vector3) = x * v.x + y * v.y + z * v.z

  def length = sqrt(x * x + y * y + z * z)

  def self = this

  def max = if (x > y) x else if (y > z) y else z

  def min = if (x < y) x else if (y < z) y else z

  override def equals(other: Any): Boolean = {
    if (other == null) {
      false
    }
    else {
      other match {
        case o: Vector3 => x == o.x && y == o.y && z == o.z
        case _ => false
      }
    }
  }
  override def toString = "<%.3f,%.3f,%.3f>".format(a, b, c)
}

object Vector3 {
  val ORIGIN = new Vector3(0, 0, 0)
  val X = new Vector3(1, 0, 0)
  val Y = new Vector3(0, 1, 0)
  val Z = new Vector3(0, 0, 1)
}

/**
 * Handy ways to create vectors and convert them to/from tuples implicitly.
 */
object Vector {
  def apply(a: Double, b: Double) = new Vector2(a, b)

  def apply(a: Double, b: Double, c: Double) = new Vector3(a, b, c)

  def toPolar(r: Double, t: Double) = new PolarVector(r, t)

  implicit def tupleToVector2(t: (Double, Double)) = Vector(t._1, t._2)

  implicit def tupleToVector3(t: (Double, Double, Double)) = Vector(t._1, t._2, t._3)

  implicit def vector2ToTuple(v: Vector2) = (v.x, v.y)

  implicit def vector3ToTuple(v: Vector3) = (v.x, v.y, v.z)

  implicit def cartesianToPolar(c: Vector2) = {
    PolarVector(c.length, atan2(c.y, c.x))
  }

  implicit def polarToCartesian(p: PolarVector) = {
    Vector(p.r * cos(p.t), p.r * sin(p.t))
  }

  implicit def tupleToPolar(t: (Double, Double)) = toPolar(t._1, t._2)
}
