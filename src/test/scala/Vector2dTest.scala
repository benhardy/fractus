package net.aethersanctum.fractus

import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.matchers.ShouldMatchers


@RunWith(classOf[JUnitRunner])
class Vector2dTest extends FunSuite with ShouldMatchers  {
  val tolerance = 0.0000001

  test("2d vector addition") {
    val A = new Vector2(1.0, 3.0)
    val B = new Vector2(50, 70)
    val C = A + B
    C.x should be (51.0 plusOrMinus tolerance)
    C.y should be (73.0 plusOrMinus tolerance)
  }
  test("2d vector subtraction") {
    val A = new Vector2(1.0, 3.0)
    val B = new Vector2(50, 70)
    val C = B - A
    C.x should be (49.0 plusOrMinus tolerance)
    C.y should be (67.0 plusOrMinus tolerance)
  }
  test("2d vector scalar multiplication") {
    val A = new Vector2(2.0, 3.0)
    val k = 6.0
    val C = A * k
    C.x should be (12.0 plusOrMinus tolerance)
    C.y should be (18.0 plusOrMinus tolerance)
  }
  test("2d vector scalar division") {
    val A = new Vector2(27.0, 15.0)
    val k = 3.0
    val C = A / k
    C.x should be (9.0 plusOrMinus tolerance)
    C.y should be (5.0 plusOrMinus tolerance)
  }
  test("2d vector dot product colinear") {
    val A = new Vector2(2,1).unit
    val d = A dot A
    d should be (1.0 plusOrMinus tolerance)
  }
  test("2d vector dot product perpendicular") {
    val A = new Vector2(2,1).unit
    val B = new Vector2(-1,2).unit
    val d = A dot B
    d should be (0.0 plusOrMinus tolerance)
  }
  test("2d vector dot product opposites") {
    val A = new Vector2(2,1).unit
    val B = new Vector2(-2,-1).unit
    val d = A dot B
    d should be (-1.0 plusOrMinus tolerance)
  }
  test("2d vector length") {
    val A = new Vector2(3,4)
    val len = A.length     // by Pythagoras, this should be 5.
    len should be (5.0 plusOrMinus tolerance)
  }
  test("2d vector unit") {
    val A = new Vector2(3,4)
    val B = A.unit
    B.x should be (0.6 plusOrMinus tolerance)
    B.y should be (0.8 plusOrMinus tolerance)
    B.length should be (1.0 plusOrMinus tolerance)
  }
  test("2d vector min") {
    val A = new Vector2(3,4)
    A.min should be (3.0 plusOrMinus tolerance)
  }
  test("2d vector max") {
    val A = new Vector2(3,4)
    A.max should be (4.0 plusOrMinus tolerance)
  }
  test("2d vector equals") {
    val A = new Vector2(3,4)
    val B = new Vector2(3,4)
    val C = new Vector2(1,2)
    val D = "Hello"
    A.equals(A) should be (true)
    A.equals(B) should be (true)
    A.equals(C) should be (false)
    A.equals(D) should be (false)
    A.equals(null) should be (false)
    
    // and in Scala...
    (A == B) should be (true)
  }
}