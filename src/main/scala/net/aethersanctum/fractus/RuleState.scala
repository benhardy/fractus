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
trait RuleState {
  /**
   * the index of the most recently selected Rule
   */
  def current: Int

  /**
   * the index of the previous most recently selected Rule
   */
  def previous: Int

  /**
   * fetch the next selected Rule and subsequent RuleState.
   * you should then use the subsequent RuleState for the next call to next() (and so on)
   */
  def next: (Rule, RuleState)
}

/**
 * This state machine generates state sequences for the given RuleBasedFractal only.
 *
 * To get the party started, call start(). This will return an initial RuleState.
 * You can then call next() on that RuleState to get the first selected rule and
 * the next RuleState, which in turn should be used (and so on).
 */
class RuleSetRunStateMachine(val fractal: RuleBasedFractal) {

  /**
   * get the initial state
   */
  def start: RuleState = new StateImpl

  class StateImpl(val current: Int = -1, val previous: Int = -1) extends RuleState {
    /**
     * the current fractal's RuleBasedFractal determines what rule is to be used next by
     * supplying the index number of the next one.
     */
    def next: (Rule, RuleState) = {
      val next = fractal.nextIndex(this)
      val new_previous = current
      val new_current = next
      val rule = fractal(new_current)
      val new_state = new StateImpl(new_current, new_previous)
      (rule, new_state)
    }
  }

}

