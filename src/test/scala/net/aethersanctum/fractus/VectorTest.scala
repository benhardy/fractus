package net.aethersanctum.fractus

import Vector._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class VectorTest extends FunSuite with ShouldMatchers {

  test("implicit 2d vector from tuple") {
    val v: Vector2 = (1.0, 7.0)
    val expected = new Vector2(1.0, 7.0)
    v should be === expected
  }

  test("implicit 3d vector from tuple") {
    val v: Vector3 = (1.0, 5.0, 7.0)
    val expected = new Vector3(1.0, 5.0, 7.0)
    v should be === expected
  }
  
  test("implicit 2d vector to tuple") {
    val vector = new Vector2(1.0, 5.0)
    val tuple: (Double, Double) = vector
    val expected = (1.0, 5.0)
    tuple should be === expected
  }

  test("implicit 3d vector to tuple") {
    val vector = new Vector3(1.0, 5.0, 7.0)
    val tuple: (Double, Double, Double) = vector
    val expected = (1.0, 5.0, 7.0)
    tuple should be === expected
  }

}