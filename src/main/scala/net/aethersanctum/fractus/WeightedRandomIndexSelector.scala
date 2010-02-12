package net.aethersanctum.fractus

import Math.random


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
 */
class WeightedRandomIndexSelector(rawWeights:Array[Double]) {

	val tot:Double = rawWeights.foldLeft(0.0) { _ + _ }
  val weights = rawWeights.map { _ / tot }
  println("Weights reset to " + weights.map{""+_}.foldLeft(""){_ + " "+ _})

	/**
	 * get the next index value
	 */
	def next:Int = {
		val randy = random
		var tot: Double = weights(0)
    var t: Int = 0
    while(randy > tot && t<weights.length) {
      t = t + 1
      tot += weights(t)
    }
		if (t<weights.length) t else weights.length-1;
	}
}
object WeightedRandomIndexSelector {
	/**
	 * builder class will extract weights from an array of T, as long as you provide an
	 * extractor function that will extract the weight from an individual T
	 */

  def build[T](items:Array[T], f:(T => Double) ) = {
    val weights =  items.map(f)  
    new WeightedRandomIndexSelector(weights);
  }

}
