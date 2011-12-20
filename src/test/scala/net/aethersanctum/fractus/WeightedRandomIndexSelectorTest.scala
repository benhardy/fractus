package net.aethersanctum.fractus

;

import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class WeightedRandomIndexSelectorTest extends FunSuite with ShouldMatchers {

  /**
   * this can be used in place of a random number generator in tests.
   */
  def numberSource(items: Iterable[Double]): (() => Double) = {
    val iterator = items.iterator
    new Function0[Double] {
      override def apply = iterator.next
    }
  }

  test("indexes are pulled out") {
    val weights = Array(1.0, 4.0, 2.0, 3.0)
    val number_stream = numberSource(List(0.09, 0.49, 0.69, 0.99))
    val selector = new WeightedRandomIndexSelector(weights, number_stream)

    selector.next should be === 0
    selector.next should be === 1
    selector.next should be === 2
    selector.next should be === 3
  }


}