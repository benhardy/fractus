package net.aethersanctum.fractus

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers


class Vector3dTest extends FunSuite with ShouldMatchers{
  val tolerance = 0.0000001

  test("3d vector addition") {
    val A = new Vector3(1.0, 3.0, 7.0)
    val B = new Vector3(50, 70, 90)
    val C = A + B
    C.x should be (51.0 plusOrMinus tolerance)
    C.y should be (73.0 plusOrMinus tolerance)
    C.z should be (97.0 plusOrMinus tolerance)
  }
  test("3d vector subtraction") {
    val A = new Vector3(1.0, 3.0, 7.0)
    val B = new Vector3(50, 70, 90)
    val C = B - A
    C.x should be (49.0 plusOrMinus tolerance)
    C.y should be (67.0 plusOrMinus tolerance)
    C.z should be (83.0 plusOrMinus tolerance)
  }
  test("3d vector scalar multiplication") {
    val A = new Vector3(2.0, 3.0, 7.0)
    val k = 6.0
    val C = A * k
    C.x should be (12.0 plusOrMinus tolerance)
    C.y should be (18.0 plusOrMinus tolerance)
    C.z should be (42.0 plusOrMinus tolerance)
  }
  test("3d vector scalar division") {
    val A = new Vector3(27.0, 15.0, 12.0)
    val k = 3.0
    val C = A / k
    C.x should be (9.0 plusOrMinus tolerance)
    C.y should be (5.0 plusOrMinus tolerance)
    C.z should be (4.0 plusOrMinus tolerance)
  }
  test("3d vector dot product colinear") {
    val A = new Vector3(2,1,0).unit
    val d = A dot A
    d should be (1.0 plusOrMinus tolerance)
  }
  test("3d vector dot product perpendicular") {
    val A = new Vector3(2,1,0).unit
    val B = new Vector3(-1,2,0).unit
    val d = A dot B
    d should be (0.0 plusOrMinus tolerance)
  }
  test("3d vector dot product opposites") {
    val A = new Vector3(2,1,0).unit
    val B = new Vector3(-2,-1,0).unit
    val d = A dot B
    d should be (-1.0 plusOrMinus tolerance)
  }
  test("3d vector length") {
    val A = new Vector3(3,4,12)
    val len = A.length     // by Pythagoras, this should be 13.
    len should be (13.0 plusOrMinus tolerance)
  }
  test("3d vector unit") {
    val A = new Vector3(3,4,12)
    val B = A.unit
    B.x should be ((3.0/13.0) plusOrMinus tolerance)
    B.y should be ((4.0/13.0) plusOrMinus tolerance)
    B.z should be ((12.0/13.0) plusOrMinus tolerance)
    B.length should be (1.0 plusOrMinus tolerance)
  }
  test("3d vector min") {
    val A = new Vector3(3,4,5)
    A.min should be (3.0 plusOrMinus tolerance)
  }
  test("3d vector max") {
    val A = new Vector3(3,4,5)
    A.max should be (5.0 plusOrMinus tolerance)
  }



}