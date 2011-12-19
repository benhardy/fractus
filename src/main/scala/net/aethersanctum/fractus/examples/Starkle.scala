package net.aethersanctum.fractus.examples

import net.aethersanctum.fractus.Rule._
import net.aethersanctum.fractus.{Vector2, Rule, RuleSet}
import net.aethersanctum.fractus.Vector
import java.awt.Color._
import net.aethersanctum.fractus.Colors._
import Math._

class Starkle extends RuleSet() {

  override def getRules: Array[Rule] = {
    Array[Rule](
      weight(1).color(BURGUNDY).colorWeight(0.70).scale(0.06).translate(0, 2),
      weight(1).color(CREAM).colorWeight(0.60).scale(0.06).translate(2, 0),
      weight(3).color(GREEN).colorWeight(0.1).scale(0.75),
      weight(2).color(ORANGE).colorWeight(0.5).also(
        (v: Vector2) => Vector(2 - 1 / (1 + v.x * v.x), v.y)
      ).translate(1, 1).polar.scale(0.7),
      weight(10).color(GREEN).colorWeight(0).rotate(60),
      weight(10).color(GREEN).colorWeight(0).scale(1, -1),
      weight(10).color(GREEN).colorWeight(0).scale(-1, 1)
    )
  }

  override def nextIndex: Int = {
    current match {
      //case 2 => if (random > 0.5) 1 else 3
      //case 3 => if (random > 0.1) 3 else super.nextIndex
      case _ => super.nextIndex
    }
  }
}
