package net.aethersanctum.fractus

import Vector._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.{MatchResult, BeMatcher, ShouldMatchers}
import net.aethersanctum.fractus.VectorTest.closeEnoughTo

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

  test("vector2 unit when length = 0") {
    val start = Vector2.ORIGIN
    val result = start.unit
    result should be (closeEnoughTo(Vector2.ORIGIN))
  }

  test("vector2 unit when length > 0") {
    val start = Vector(3,4)
    val result = start.unit
    val expected = Vector(0.6, 0.8)
    result should be (closeEnoughTo(expected))
  }

}
object VectorTest {

  val tolerance = 0.00001

  import math.abs

  /**
   * custom matcher for comparing vectors with a little wiggle room needed for float comparisons
   */
  case class closeEnoughTo(left:Vector2) extends BeMatcher[Vector2] {
    def apply(right:Vector2) =
      MatchResult(
        abs(left.x - right.x) <= tolerance &&
        abs(left.y - right.y) <= tolerance,

        left + " was not close enough to " + right,
        left + " was close enough to " + right
      )
  }

}