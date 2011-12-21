package net.aethersanctum.fractus

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import java.awt.Color._
/**
 */
@RunWith(classOf[JUnitRunner])
class RuleStateRunStateMachineTest extends FunSuite with ShouldMatchers {

  test("next state is correctly generated") {
    val ruleSet = new RuleSet() {
      import Rule._
      val rules = Array[Rule](
        weight(1).translate(1,0).color(RED), 
        weight(1).scale(3.0).color(GREEN)
      )

      override def getRules : Array[Rule] = { rules }

      override def nextIndex(x:RuleState) = x.current match {
        case -1 => 0
        case 0 => 1
        case 1 => 2
        case _ => throw new IllegalStateException("we've gone too far this time")
      }
    }
    val sm = new RuleSetRunStateMachine(ruleSet)
    val initState = sm.start
    initState.current should be === -1
    initState.previous should be === -1
    val startPosition = Vector2.ORIGIN
    val (rule1, state1) = initState.next
    state1.current should be === 0
    state1.previous should be === -1
    val (rule2, state2) = state1.next
    state2.current should be === 1
    state2.previous should be === 0
  }
}