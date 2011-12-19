package net.aethersanctum.fractus;

import org.scalatest.FunSuite

class WeightedRandomIndexSelectorTestSuite extends FunSuite {

  /**
   * this can be used in place of a random number generator in tests.
   */
  def numberSource(items: Iterable[Double]) : (() => Double) = {
    val iterator = items.iterator
    new Function0[Double] {
      override def apply = iterator.next
    }
  }
  
   test("indexes are pulled out") {

    val numbers = numberSource(List(0.09, 0.49, 0.69, 0.99))
    val selector = new WeightedRandomIndexSelector(Array(1.0, 4.0, 2.0, 3.0), numbers)

    assert(0 === selector.next)
    assert(1 === selector.next)
    assert(2 === selector.next)
    assert(3 === selector.next)
  }


}