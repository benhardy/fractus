package net.aethersanctum.fractus.examples


import java.awt.Color._

import net.aethersanctum.fractus.Rule._
import net.aethersanctum.fractus.{PartRandom, Rule, RuleState}

/**
 * A nice stained glass situation.
 */


object HalfSerp extends PartRandom {
  override val name = "halfserp"

  override def customTransition(state: RuleState) = {
    if (state.current == 0 || state.previous == 1) 2 else NO_CUSTOM
  }

  override val rules = Array[Rule](
    rule.weight(1).color(GREEN).colorWeight(0.5).scale(0.5).translate(1.25, 0),
    rule.weight(1).color(RED).colorWeight(0.5).scale(0.5).translate(-1.25, 0),
    rule.weight(1).color(GRAY).colorWeight(0.5).rotate(60)
  )
}
