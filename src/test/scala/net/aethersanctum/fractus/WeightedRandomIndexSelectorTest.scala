package net.aethersanctum.fractus

;

import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class WeightedRandomIndexSelectorTest extends FunSuite with ShouldMatchers {


  test("indexes are pulled out") {
    val weights = Array(1.0, 4.0, 2.0, 3.0)
    val number_stream = NumberSource(List(0.09, 0.49, 0.69, 0.99))
    val selector = new WeightedRandomIndexSelector(weights, number_stream)

    selector.next should be === 0
    selector.next should be === 1
    selector.next should be === 2
    selector.next should be === 3
  }
}

object NumberSource {

  /**
   * this can be used in place of a random number generator in tests.
   */
  def apply(items: Iterable[Double]): (() => Double) = {
    val iterator = items.iterator
    new (() => Double) {
      override def apply() = iterator.next
    }
  }
}