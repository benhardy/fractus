package net.aethersanctum.fractus

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

import org.scalatest.mock.MockitoSugar

import VectorTest._

@RunWith(classOf[JUnitRunner])
class TransformTest extends FunSuite with ShouldMatchers {

  test("affine transforms should combine in right order") {
    val V1 = new Vector2(3,7)
    val T1 = Transform.scale(10)
    val T2 = Transform.translate(5,3)
    val M = T1 combine T2
    M.isInstanceOf[AffineTransform] should be === true
    val V2 = M(V1)
    val expected = Vector(35.0, 73.0)
    V2 should be(closeEnoughTo(expected))
  }

  test("non-affine transforms should combine in right order") {
    val V1 = new Vector2(1, 3)
    val T1 = Transform( (v:Vector2) => v + (0.2, 0.3) )  // won'index be recognized as affine
    val T2 = Transform.scale(2)
    val M = T1 combine T2
    M.isInstanceOf[AffineTransform] should be === false
    val result = M(V1)
    val expected = Vector(2.4, 6.6)
    result should be (closeEnoughTo(expected))
  }

  test("rotate should go anticlockwise") {
    val V1 = new Vector2(3,2)
    val T = Transform.rotate(90)
    val result = T(V1)
    val expected = Vector(-2, 3.0)
    result should be (closeEnoughTo(expected))
  }

  test("translate should be like vector addition") {
    val start = Vector(3, 2)
    val delta = Vector(8, 12)
    val shift = Transform.translate(delta.x, delta.y)
    val result = shift(start)
    val expected = start + delta
    result should be (closeEnoughTo(expected))
  }

  test("scale can multiply components independently") {
    val start = Vector(3, 4)
    val factor = Vector(8, 12)
    val scaler = Transform.scale(factor.x, factor.y)
    val result = scaler(start)
    val expected = Vector(24, 48)
    result should be (closeEnoughTo(expected))
  }

  test("scale by scalar") {
    val start = Vector(3, 4)
    val factor = 5.0
    val shift = Transform scale factor
    val result = shift(start)
    val expected = start * factor
    result should be (closeEnoughTo(expected))
  }


  test("go to polar 1") {
    val start = Vector(10, 0)
    val result = Transform polar start
    val expected = Vector(10, 0)
    result should be (closeEnoughTo(expected))
  }

  test("go to polar 2") {
    val start = Vector(0, 10)
    val result = Transform polar start
    val expected = Vector(10, math.Pi/2)
    result should be (closeEnoughTo(expected))
  }


  test("go to cartesian 1") {
    val start = Vector(10, 0)
    val result = Transform cartesian start
    val expected = Vector(10, 0)
    result should be (closeEnoughTo(expected))
  }

  test("go to cartesian 2") {
    val start = Vector(10, math.Pi/2)
    val result = Transform cartesian start
    val expected = Vector(0, 10)
    result should be (closeEnoughTo(expected))
  }

  test("invert radius") {
    val start = Vector(1,0)
    val result = Transform invertRadius start
    val expected = Vector(0.5,0)
    result should be (closeEnoughTo(expected))
  }

  test("invert radius 2") {
    val start = Vector(1,3)
    val result = Transform invertRadius start
    val expected = Vector(1/11.0, 3/11.0)
    result should be (closeEnoughTo(expected))
  }

  test("complex squared") {
    val start = Vector(2, 5)
    val result = Transform complexSquared start
    val expected = Vector(-21, 20)
    result should be (closeEnoughTo(expected))
  }
  
  val IDENTITY : Transform = { (v:Vector2) => v }
  val DOUBLE : Transform = { (v:Vector2) => v * 2 }
  val SHIFT : Transform = { (v:Vector2) => v + (1.0, 2.0) }

  test("non-affine combined translate should be like vector addition") {
    val start = Vector(3, 2)
    val delta = Vector(5, 4)
    val shift = DOUBLE.translate(delta.x, delta.y)
    val result = shift(start)
    val expected = Vector(11, 8)
    result should be (closeEnoughTo(expected))
  }

  test("non-affine combined scale can multiply components independently") {
    val start = Vector(3, 4)
    val factor = Vector(8, 12)
    val combo = SHIFT.scale(factor.x, factor.y)
    val result = combo(start)
    val expected = Vector(32,72)
    result should be (closeEnoughTo(expected))
  }

  test("non-affine combined scale by scalar") {
    val start = Vector(3, 4)
    val factor = 5.0
    val combo = SHIFT scale factor
    val result = combo(start)
    val expected = Vector(20,30)
    result should be (closeEnoughTo(expected))
  }

  test("non-affine combined rotation") {
    val start = Vector(5, 8)
    val angle = 90
    val combo = SHIFT rotate angle
    val result = combo(start)
    val expected = Vector(-10,6)
    result should be (closeEnoughTo(expected))
  }
  test("non-affine combined polar") {
    val start = Vector(0, Math.Pi-2)
    val combo = SHIFT.polar
    val result = combo(start)
    val expected = Vector(3.2969083, 1.262627)
    result should be (closeEnoughTo(expected))
  }
  test("non-affine combined invert radius") {
    val start = Vector(1,2)
    val combo = SHIFT.invertRadius
    val result = combo(start)
    val expected = Vector(2.0/21, 4.0/21)
    result should be (closeEnoughTo(expected))
  }
  test("non-affine combined sine") {
    import math.sin
    val start = Vector(5, 8)
    val combo = SHIFT.sine
    val result = combo(start)
    val expected = Vector(sin(6),sin(10))
    result should be (closeEnoughTo(expected))
  }

  test("affine combined with translation") {
    val start = Vector(5, 8)
    val expand = Transform.scale(3,4)
    val combo = expand translate (5,7)
    val result = combo(start)
    val expected = Vector(20, 39)
    result should be (closeEnoughTo(expected))
  }

  test("affine combined with rotation") {
    val start = Vector(5, 8)
    val expand = Transform.scale(3,4)
    val combo = expand rotate 90
    val result = combo(start)
    val expected = Vector(-32, 15)
    result should be (closeEnoughTo(expected))
  }

  test("affine combined with scalar scale") {
    val start = Vector(5, 8)
    val expand = Transform.translate(3,4)
    val combo = expand scale 1.5
    val result = combo(start)
    val expected = Vector(12,18)
    result should be (closeEnoughTo(expected))
  }

  test("affine combined with vector scale") {
    val start = Vector(5, 8)
    val expand = Transform.translate(3,4)
    val combo = expand scale (1.5,0.5)
    val result = combo(start)
    val expected = Vector(12,6)
    result should be (closeEnoughTo(expected))
  }

  test("in polar space") {
    val start = Vector(3, 4)
    //val pTrans = Transform.translate
    val trans = Transform inPolarSpace { (v:PolarVector) => v + PolarVector(3, math.Pi/2) }
    val result = trans(start)
    val expected = Vector(-6.4, 4.8)
    result should be (closeEnoughTo(expected))
  }

}