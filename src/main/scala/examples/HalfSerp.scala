package net.aethersanctum.fractus.examples


import java.awt.Color._

import net.aethersanctum.fractus.{PartRandom, Rule, RuleState}
import net.aethersanctum.fractus.Rule._
import net.aethersanctum.fractus.Transform._
import math.random
/**
 * A nice stained glass situation.
 */
object HalfSerp extends PartRandom {
  override val name = "halfserp"

  override def customTransition(state: RuleState) = {
    if (state.current == 0 || state.previous == 1) 2 else NO_CUSTOM
  }

  override val rules = Array[Rule](
    rule weight 1 color GREEN colorWeight 0.5 scale 0.5 translate(1.25, 0),
    rule weight 1 color RED   colorWeight 0.5 scale 0.5 translate(-1.25, 0),
    rule weight 1 color GRAY  colorWeight 0.5 rotate 60
  )
}

/**
 * Another nice stained glass situation.
 */
object FiveSerp extends PartRandom {
  override val name = "fiveserp"

  override def customTransition(state: RuleState) = {
    if (state.current == 0 || state.previous == 1) 2 else NO_CUSTOM
  }

  override val rules = Array[Rule](
    rule weight 1 color WHITE colorWeight 0.5 scale 0.15 translate(0, 1.25),
    rule weight 1 color RED   colorWeight 0.5 also polar,
    rule weight 21 color GRAY  colorWeight 0.0 rotate 72,
    rule weight 10 color GRAY  colorWeight 0.0 scale (-1,1)
  )
}

object DerpDerp extends PartRandom {
  override val name = "derpderp"

  override def customTransition(state: RuleState) = {
    val switch: Boolean = random <= 0.5
    (state.current, switch) match {
      case (0, true) => 1
      case (0, false) => 2
      case (1, true) => 0
      case (1, true) => 2
      case (2, true) => 0
      case (2, true) => 1
      case _ => 0
    }
  }

  override val rules = Array[Rule](
    rule weight 1 color GREEN colorWeight 0.5 scale 0.75 rotate 30 translate(0.5, 0.5),
    rule weight 1 color BLUE  colorWeight 0.5 translate (-1,-1),
    rule weight 1 color WHITE colorWeight 0.5 also polar
  )
}

