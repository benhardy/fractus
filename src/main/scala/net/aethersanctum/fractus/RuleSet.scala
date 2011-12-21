package net.aethersanctum.fractus

import examples.Examples

/**
 * RuleSet encapsulates a group of rules which define a fractal's appearance.
 */
abstract class RuleSet {

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
   * For any given RuleSet we should be able to look up a rule by its index number.
   */
  def apply(index:Int): Rule = getRules(index)

  /**
   * by default image scale is 1, but you can override this if you want
   */
  def scale:Double = 1

}

/**
 * default implementation of RuleSet includes random rule selection
 */
class RandomSelectionRuleSet(rules:Array[Rule]) extends RuleSet {

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

object RuleSet {
  /**
   * Factory method for constructing a RuleSet from a variable length argument list of rules
   */
  def apply(rules : Rule*) : RuleSet = {
    new RandomSelectionRuleSet(rules.toArray[Rule])
  }

  /**
   * Factory method for constructing a RuleSet from a collection of rules
   */
  def apply(rules : Traversable[Rule]) : RuleSet = {
    new RandomSelectionRuleSet(rules.toArray[Rule])
  }

  /**
   * Search for a RuleSet by name in the Examples.
   */
  def find(name:String): RuleSet = {
    Examples(name).getOrElse {
      throw new IllegalArgumentException("couldn't find a rule by the name of "+name)
    }
  }
}