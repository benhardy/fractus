package net.aethersanctum.fractus

/**
 * RuleBasedFractal encapsulates a group of rules which define a
 * the appearance of an IFS/ChaosGame type fractal's appearance.
 */
trait RuleBasedFractal {
  def name : String

  /**
   * retrieve all the rules at once
   */
  def rules: Array[Rule]

  /**
   * override nextIndex if you want to specify more complex rule selection
   * behavior than just random. the default selector ignores the current
   * state of the runner.
   */
  def nextIndex(currentState: RuleState): Int

  def scale: Double
}

/**
 * by default, rules in a ruleset are selected randomly skewed by the weight of each rule.
 */
abstract class PartRandom extends RuleBasedFractal {

  val NO_CUSTOM: Int = -1

  def customTransition(state: RuleState): Int = NO_CUSTOM

  final override def nextIndex(state: RuleState): Int = {
    val custom = customTransition(state)
    if (custom == NO_CUSTOM)
      randomSelector.next
    else
      custom
  }

  def scale = 1

  /**
   * needs to be lazy because rules might not be initialized before
   */
  private lazy val randomSelector = WeightedRandomIndexSelector(rules)
}

/**
 * default implementation of RuleBasedFractal includes
 * 1. always random rule selection
 * 2. name and rules passed by constructor
 */
case class AlwaysRandom(name: String,
                       rules: Array[Rule],
                       override val scale: Double = 1) extends PartRandom

object RuleBasedFractal {
  /**
   * Factory method for constructing a RuleBasedFractal from a variable length argument list of rules
   */
  def apply(name:String, rules: Rule*): RuleBasedFractal = {
    AlwaysRandom(name, rules.toArray[Rule])
  }
  def apply(name:String, weight:Double, rules: Rule*): RuleBasedFractal = {
    AlwaysRandom(name, rules.toArray[Rule], weight)
  }

  /**
   * Factory method for constructing a RuleBasedFractal from a collection of rules
   */
  def apply(name:String, rules: Traversable[Rule]): RuleBasedFractal = {
    AlwaysRandom(name, rules.toArray[Rule])
  }

  implicit def toNamedPair(r:RuleBasedFractal): (String, RuleBasedFractal) = (r.name, r)
}
