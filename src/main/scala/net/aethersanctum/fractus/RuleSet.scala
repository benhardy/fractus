package net.aethersanctum.fractus

import examples.Examples


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

object RuleSet {
  def apply(rules : Rule*) : RuleSet = {
    val array = rules.toArray[Rule]
    new RuleSet() {
      override def getRules = array
    }
  }
}

class RuleSetFinder {
  def find(name:String): RuleSet = {
    Examples(name).getOrElse {
      throw new IllegalArgumentException("couldn't find a rule by the name of "+name)
    }
  }
}