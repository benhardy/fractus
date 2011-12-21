package net.aethersanctum.fractus

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class TransformTest extends FunSuite with ShouldMatchers {

  val tolerance = 0.00001

  test("affine transforms should combine in right order") {
    val V1 = new Vector2(3,7)
    val T1 = Transform.scale(10)
    val T2 = Transform.translate(5,3)
    val M = T1 combine T2
    M.isInstanceOf[AffineTransform] should be === true
    val V2 = M(V1)
    V2.x should be (35.0 plusOrMinus tolerance)
    V2.y should be (73.0 plusOrMinus tolerance)
  }

  test("non-affine transforms should combine in right order") {
    val V1 = new Vector2(1, 3)
    val T1 = Transform( (v:Vector2) => v + (0.2, 0.3) )  // won't be recognized as affine
    val T2 = Transform.scale(2)
    val M = T1 combine T2
    M.isInstanceOf[AffineTransform] should be === false
    val V2 = M(V1)
    V2.x should be (2.4 plusOrMinus tolerance)
    V2.y should be (6.6 plusOrMinus tolerance)
  }

  test("rotate should go anticlockwise") {
    val V1 = new Vector2(3,2)
    val T = Transform.rotate(90)
    val V2 = T(V1)
    V2.x should be (-2.0 plusOrMinus tolerance)
    V2.y should be (3.0 plusOrMinus tolerance)
  }

}