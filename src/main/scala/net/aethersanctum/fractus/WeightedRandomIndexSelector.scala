package net.aethersanctum.fractus

import math.random
import java.lang.{StringBuilder => jStringBuilder}
import scala.collection.mutable.StringBuilder

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
 * The random number source can be altered for testing purposes.
 */
class WeightedRandomIndexSelector(rawWeights:Array[Double], selector: (()=>Double) = (() => random)) {

	val tot:Double = rawWeights.sum
  val weights = rawWeights.map { _ / tot }
  println("Weights reset to " +  toString())

	/**
	 * get the next index value
	 */
	def next:Int = {
		val position: Double = selector()
		var tot: Double = weights(0)
    var t: Int = 0
    while(position > tot && t<weights.length) {
      t = t + 1
      tot += weights(t)
    }
		if (t<weights.length) t else weights.length-1;
	}
  
  override def toString = {
    weights.foldLeft(new scala.StringBuilder) {
      (s:scala.StringBuilder, x:Double) => s append " " append x
      s
    }.toString()
  }
}

object WeightedRandomIndexSelector {
	/**
	 * builder class will extract weights from an array of T, as long as you provide an
	 * extractor function that will extract the weight from an individual T
	 */

  def apply[T](items:Array[T])( f:(T => Double) ) = {
    val weights =  items.map(f)  
    new WeightedRandomIndexSelector(weights);
  }
}
