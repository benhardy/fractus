package net.aethersanctum.fractus

import examples.Examples


trait RuleSet {
  def getRules: Array[Rule]

  val defaultProbs = getRules.map { _.weight }
  val defaultSelector = new WeightedRandomIndexSelector(defaultProbs)
  def scale:Double = 1

  /**
   * override nextIndex if you want to specify more complex rule selection
   * behavior than just random. the default selector ignores the current
   * state of the runner.
   */
  def nextIndex(currentState:RuleSetRunState):Int = defaultSelector.next

  def state: RuleSetRunState = new RuleSetRunState(this)
}

/**
 * RuleSetRunState is a state machine which always returns the rule selected and the next state
 * the machine is in.
 */

class RuleSetRunState(val ruleSet:RuleSet, val current:Int = -1, val previous:Int = -1) {
  def next:(Rule, RuleSetRunState) = {
    val next = ruleSet.nextIndex(this)
    val new_previous = current
    val new_current = next
    val rule = ruleSet.getRules(new_current)
    val new_state = new RuleSetRunState(ruleSet, new_current, new_previous)
    (rule, new_state)
  }
}


object RuleSet {
  def apply(rules : Rule*) : RuleSet = {
    val array = rules.toArray[Rule]
    new RuleSet() {
      override def getRules = array
    }
  }
  
  def apply(rules : Traversable[Rule]) : RuleSet = {
    val array = rules.toArray[Rule]
    new RuleSet() {
      override def getRules = array
    }
  }

  def find(name:String): RuleSet = {
    Examples(name).getOrElse {
      throw new IllegalArgumentException("couldn't find a rule by the name of "+name)
    }
  }
}