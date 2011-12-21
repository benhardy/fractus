package net.aethersanctum.fractus.examples

import net.aethersanctum.fractus.Rule._
import java.awt.Color._
import net.aethersanctum.fractus.Colors._
import math._
import net.aethersanctum.fractus.{RandomSelectionRuleSet, RuleState, Rule, RuleSet}

class Spongy extends RandomSelectionRuleSet(Array(
      weight(1) color (RED) colorWeight (0.5) scale (1.0 / 3) translate(-1.5, 1.5),
      weight(1) color (GREEN) colorWeight (0.5) scale (1.0 / 3) translate(0.0, 1.5),
      weight(1) color (RED) colorWeight (0.5) scale (1.0 / 3) translate(1.5, 1.5),

      weight(1) color (GREEN) colorWeight (0.5) scale (1.0 / 3) translate(-1.5, 0.0),
      weight(0) color (BLACK) colorWeight (0.5) scale (1.0 / 3) translate(0.0, 0.0),
      weight(1) color (GREEN) colorWeight (0.5) scale (1.0 / 3) translate(1.5, 0.0),

      weight(1) color (RED) colorWeight (0.5) scale (1.0 / 3) translate(-1.5, -1.5),
      weight(1) color (GREEN) colorWeight (0.5) scale (1.0 / 3) translate(0.0, -1.5),
      weight(1) color (RED) colorWeight (0.5) scale (1.0 / 3) translate(1.5, -1.5)
    ) ) {


  /**randomly choose something from the array of rule indices */
  def allow(n: Array[Int]) = {
    val p = (random * n.length).toInt
    n(p)
  }

  override def nextIndex(state:RuleState): Int = {
    var ok = false
    var default = -1
    while (!ok) {
      default = super.nextIndex(state)
      // figure out which combinations of previous, current and next rules we don't want
      ok = (state.previous, state.current, default) match {
        case (1, 7, 0) => false
        case (7, 7, 0) => false
        case (3, 5, 0) => false
        case (5, 5, 0) => false

        case (1, 7, 1) => false
        case (7, 7, 1) => false
        case (3, 3, 1) => false
        case (5, 3, 1) => false
        case (3, 5, 1) => false
        case (5, 5, 1) => false

        case (1, 7, 2) => false
        case (7, 7, 2) => false
        case (3, 3, 2) => false
        case (5, 3, 2) => false

        case (1, 1, 3) => false
        case (1, 7, 3) => false
        case (7, 1, 3) => false
        case (7, 7, 3) => false
        case (3, 5, 3) => false
        case (5, 5, 3) => false

        case (1, 1, 5) => false
        case (1, 7, 5) => false
        case (7, 1, 5) => false
        case (7, 7, 5) => false
        case (5, 3, 5) => false
        case (3, 3, 5) => false

        case (1, 1, 6) => false
        case (7, 1, 6) => false
        case (3, 5, 6) => false
        case (5, 5, 6) => false

        case (1, 1, 7) => false
        case (7, 1, 7) => false
        case (3, 3, 7) => false
        case (5, 3, 7) => false
        case (3, 5, 7) => false
        case (5, 5, 7) => false

        case (1, 1, 8) => false
        case (7, 1, 8) => false
        case (3, 3, 8) => false
        case (5, 3, 8) => false
        case _ => true
      }
    }
    default
  }
}