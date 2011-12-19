package net.aethersanctum.fractus.examples


import net.aethersanctum.fractus._
import net.aethersanctum.fractus.Rule._
import java.awt.Color._
import net.aethersanctum.fractus.Colors._
import Math._
import net.aethersanctum.fractus.Transform._

class SquaresVille extends RuleSet {
  val GRIDSCALE = 0.2

  override def getRules: Array[Rule] = {
    Array[Rule](
      // draw a box
      rule.weight(1).color(RED).colorWeight(0.8).transform((v: Vector2) =>
        new Vector2(Math.sin(v.x * 371 + v.y * 503 + 1), Math.sin(v.x * 529 + v.y * 447 + 3))
      ).scale(GRIDSCALE * 0.4).translate(-GRIDSCALE, -GRIDSCALE / 2),
      rule.weight(25).color(WHITE).colorWeight(0.1).translate(-GRIDSCALE, 0),
      rule.weight(55).color(WHITE).colorWeight(0.1).translate(0, -GRIDSCALE),

      rule.weight(1).color(GREEN).colorWeight(0.2).scale(1, 1).cartesian
    )
  }

  override def nextIndex(state:RuleSetRunState): Int = {
    state.current match {
      case 3 => 0
      case _ => super.nextIndex(state)
    }
  }
}