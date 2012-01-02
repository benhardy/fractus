package net.aethersanctum.fractus

import examples.Examples

/**
 * RuleBasedFractal encapsulates a group of rules which define a
 * the appearance of an IFS/ChaosGame type fractal's appearance.
 */
abstract class RuleBasedFractal {

  /**
   * retrieve all the rules at once
   */
  def getRules : Array[Rule]

  /**
   * override nextIndex if you want to specify more complex rule selection
   * behavior than just random. the default selector ignores the current
   * state of the runner.
   */
  def nextIndex(currentState:RuleState):Int

  /**
   * For any given RuleBasedFractal we should be able to look up a rule by its index number.
   */
  def apply(index:Int): Rule = getRules(index)

  /**
   * by default image scale is 1, but you can override this if you want
   */
  def scale:Double = 1

}

/**
 * default implementation of RuleBasedFractal includes random rule selection
 */
class RandomSelectionRuleBasedFractal(rules:Array[Rule]) extends RuleBasedFractal {

  override def getRules = rules
  /**
   * by default, rules in a ruleset are selected randomly skewed by the weight of each rule.
   */
  val defaultSelector = WeightedRandomIndexSelector(rules) { _.weight }

  /**
   * override nextIndex if you want to specify more complex rule selection
   * behavior than just random. the default selector ignores the current
   * state of the runner.
   */
  override def nextIndex(currentState:RuleState):Int = defaultSelector.next
}

trait PickySelector {
  def next(currentState:RuleState) : Int
}

class SelectiveRuleTransitionFractal(rules:Array[Rule],
                                      pickySelector:PickySelector) extends RuleBasedFractal {
  override def getRules = rules

  val ruleSelector = pickySelector

  override def nextIndex(currentState:RuleState) : Int = pickySelector.next(currentState)
}

object RuleBasedFractal {
  /**
   * Factory method for constructing a RuleBasedFractal from a variable length argument list of rules
   */
  def apply(rules : Rule*) : RuleBasedFractal = {
    new RandomSelectionRuleBasedFractal(rules.toArray[Rule])
  }

  /**
   * Factory method for constructing a RuleBasedFractal from a collection of rules
   */
  def apply(rules : Traversable[Rule]) : RuleBasedFractal = {
    new RandomSelectionRuleBasedFractal(rules.toArray[Rule])
  }
  /**
   * Factory method for constructing a RuleBasedFractal from a variable length argument list of rules
   */
  def apply(selector: PickySelector, rules : Rule*) : RuleBasedFractal = {
    new SelectiveRuleTransitionFractal(rules.toArray[Rule], selector)
  }

  /**
   * Factory method for constructing a RuleBasedFractal from a collection of rules
   */
  def apply(selector: PickySelector, rules : Traversable[Rule]) : RuleBasedFractal = {
    new SelectiveRuleTransitionFractal(rules.toArray[Rule], selector)
  }

  /**
   * Search for a RuleBasedFractal by name in the Examples.
   */
  def findByName(name:String): RuleBasedFractal = {
    Examples(name).getOrElse {
      throw new IllegalArgumentException("couldn't findByName a rule by the name of "+name)
    }
  }
}