package net.aethersanctum.fractus

import ruleset.RuleMap


trait RuleSet {
  def getRules: Array[Rule]

  val defaultProbs = getRules.map { _ weight }
  val defaultSelector = new WeightedRandomIndexSelector(defaultProbs)
  var current = -1
  var previous = -1
  def scale:Double = 1

  /**
   * override nextIndex if you want to specify more complex rule selection
   * behavior than just random
   */
  def nextIndex:Int = defaultSelector.next

  def next:Rule = {
    val next = nextIndex
    previous = current
    current = next
    getRules(current)
  }

}

class RuleSetFinder {
  def find(name:String): RuleSet = {
    RuleMap(name) match {
      case Some(v) => v
      case _ => throw new IllegalArgumentException
    }

  }
}