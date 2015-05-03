package net.aethersanctum.fractus

/**
 * RuleSetRunStateMachine is a state machine which always returns the rule
 * selected and the next RuleState the machine is in.
 *
 * The current and next fields can optionally be read by subclasses of RuleBasedFractal
 * which happen to override nextIndex in order to effect conditional selection
 * of rules. See examples.Spongy for an example of this. Most examples don't
 * do this.
 */
case class RuleState(

  /**
   * the index of the most recently selected Rule
   */
  current: Int,

  /**
   * the index of the previous most recently selected Rule
   */
  previous: Int
)
object RuleState {
  val INITIAL = RuleState(-1, -1)
}


