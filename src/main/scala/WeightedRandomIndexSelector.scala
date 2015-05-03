package net.aethersanctum.fractus

import math.{min, random}
import annotation.tailrec

/**
 * Constructs a normalized array of weights, and allows you to select an index
 * into that array. Used for selecting rules by index according to weight, i.e.
 * rule index with higher weights are more likely to be selected.
 * <p>
 * Normalized means that all the weights are added together and each weight is
 * divided by the resulting sum, so that internally all the weights add up to
 * one.
 * <p>
 * To select the next index, a random number between 0 and 1 is selected. We
 * then traverse the array of weights, adding each to a total until the total
 * is greater than the randomly selected number. The array index is returned.
 * <p>
 * The random number source (selector) can be altered for testing purposes.
 */
class WeightedRandomIndexSelector(rawWeights: IndexedSeq[Double]) {

  val tot: Double = rawWeights.sum
  val weights = rawWeights.map { _ / tot }
  println("Weights reset to " + toString())

  @tailrec
  final def measurePosition(position:Double, index: Int, total: Double): Int = {
    if (position <= total || index >= weights.length) {
      index
    } else {
      measurePosition(position, index + 1, total + weights(index + 1))
    }
  }

  def selectPosition : Double = random

  /**
   * get the next index value
   */
  def next: Int = {
    val t = measurePosition(selectPosition, 0, weights(0))
    min(t, weights.length - 1)
  }

  override def toString = weights.mkString(" ")
}

object WeightedRandomIndexSelector {
  /**
   * builder class will extract weights from an array of T, as long as you provide an
   * extractor function that will extract the weight from an individual T
   */

  def apply(items: IndexedSeq[Rule]) = {
    val weights = items.map(_.weight)
    new WeightedRandomIndexSelector(weights)
  }
}
