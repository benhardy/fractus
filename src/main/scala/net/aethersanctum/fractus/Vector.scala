package net.aethersanctum.fractus

import Math._

trait Vector [V <: Vector[V]] {
  def +(v:V): V
  def -(v:V): V
  def *(k:Double): V
  def /(k:Double): V
  def dot(v:V): Double
  def length: Double
  def max: Double
  def min: Double
  def unit = {
    val len = length
    if (len>0)
      self / len
    else
      self
  }
  protected def self: V
}

class Vector2(a:Double,b:Double) extends Vector[Vector2] {
  def x = a
  def y = b
  def +(v:Vector2) = new Vector2(x + v.x, y + v.y)
  def -(v:Vector2) = new Vector2(x - v.x, y - v.y)
  def *(k:Double) = new Vector2(x * k, y * k)
  def *(v:Vector2) = new Vector2(x * v.x, y * v.y)
  def /(k:Double) = new Vector2(x / k, y / k)
  def dot(v:Vector2) = x * v.x + y * v.y
  def length = sqrt(x*x + y*y)
  def self = this
  def max = if (x>y) x else y
  def min = if (x<y) x else y
}

class PolarVector(radius:Double, angle:Double) {
  def r = radius
  def t = angle
  def +(v:PolarVector) = new PolarVector(r + v.r, t + v.t)
  def -(v:PolarVector) = new PolarVector(r - v.r, t - v.t)
  def *(k:Double) = new PolarVector(r * k, t) // scaling only affects radius in polar space
  def /(k:Double) = new PolarVector(r / k, t)
  def spin(dt:Double) = new PolarVector(r, t + dt)
  def length = r
}

class Vector3(a:Double,b:Double,c:Double) extends Vector[Vector3] {
  def x = a
  def y = b
  def z = c
  def +(v:Vector3) = new Vector3(x + v.x, y + v.y, z+ v.z)
  def -(v:Vector3) = new Vector3(x - v.x, y - v.y, z - v.z)
  def *(k:Double) = new Vector3(x * k, y * k, z * k)
  def /(k:Double) = new Vector3(x / k, y / k, z / k)
  def dot(v:Vector3) = x * v.x + y * v.y + z * v.z
  def length = sqrt(x*x + y*y +z*z)
  def self = this
  def max = if (x>y) x else if (y>z) y else z
  def min = if (x<y) x else if (y<z) y else z
}

object Vector {
  def apply(a:Double, b:Double) = new Vector2(a,b)
  def apply(a:Double, b:Double, c:Double) = new Vector3(a,b,c)
  def polar(r:Double, t:Double) = new PolarVector(r,t)
  implicit def tupleToVector2(t:(Double,Double)) = Vector(t._1, t._2)
  implicit def tupleToVector3(t:(Double,Double,Double)) = Vector(t._1, t._2, t._3)

  implicit def cartesianToPolar(c:Vector2) = {
    PolarVector(c.length, Math.atan2(c.y, c.x))
  }
  implicit def polarToCartesian(p:PolarVector) = {
    Vector2(p.r * cos(p.t), p.r * sin(p.t))
  }
  implicit def tupleToPolar(t:(Double,Double)) = polar(t._1, t._2)
}