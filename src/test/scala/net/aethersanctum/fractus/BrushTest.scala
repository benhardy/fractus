package net.aethersanctum.fractus

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito

/**
  */

@RunWith(classOf[JUnitRunner])
class BrushTest extends FunSuite with ShouldMatchers with MockitoSugar {
  val canvas = Mockito mock classOf[Canvas]
  val brush = new InitiallyBlurryBrush(canvas)
  val tolerance = 0.00001 // for precision errors

  def about(expect: Double) = {
    expect plusOrMinus tolerance
  }

  test("stripeGenerator skinny single pixel stripe") {
    val (range, fatness) = brush.stripeGenerator(55.5, 0.2)
    (range.length) should be === 0
    (range.start) should be === 55
    (range.end) should be === 55
    val expect = 0.0
    fatness(54) should be (about(expect))
    fatness(55) should be (about(0.2))
    fatness(56) should be (about(0.0))
  }


  test("stripeGenerator skinny two pixel stripe") {
    val (range, fatness) = brush.stripeGenerator(88.75, 0.9)
    (range.length) should be === 1
    (range.start) should be === 88
    (range.end) should be === 89
    fatness(87) should be (about(0.0))
    fatness(88) should be (about(0.7))
    fatness(89) should be (about(0.2))
    fatness(90) should be (about(0.0))
  }
  test("stripeGenerator two pixel stripe") {
    val (range, fatness) = brush.stripeGenerator(88.75, 1.2)
    (range.length) should be === 1
    (range.start) should be === 88
    (range.end) should be === 89
    fatness(87) should be (about(0.0))
    fatness(88) should be (about(0.85))
    fatness(89) should be (about(0.35))
    fatness(90) should be (about(0.0))
  }
  test("stripeGenerator three pixel stripe") {
    val (range, fatness) = brush.stripeGenerator(88.75, 1.9)
    (range.length) should be === 2
    (range.start) should be === 87
    (range.end) should be === 89
    fatness(86) should be (about(0.0))
    fatness(87) should be (about(0.2))
    fatness(88) should be (about(1.0))
    fatness(89) should be (about(0.7))
    fatness(90) should be (about(0.0))
  }
}
