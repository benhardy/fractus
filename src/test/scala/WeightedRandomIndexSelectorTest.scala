package net.aethersanctum.fractus

import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class WeightedRandomIndexSelectorTest extends FunSuite with ShouldMatchers {


  test("indexes are pulled out") {
    val weights = Array(1.0, 4.0, 2.0, 3.0)
    val numbers = List(0.09, 0.49, 0.69, 0.99, 0.42).iterator

    val selector = new WeightedRandomIndexSelector(weights) {
      override def selectPosition = numbers.next()
    }

    selector.next should be === 0
    selector.next should be === 1
    selector.next should be === 2
    selector.next should be === 3
    selector.next should be === 1
  }
}
