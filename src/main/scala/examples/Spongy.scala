package net.aethersanctum.fractus.examples

import java.awt.Color._
import math._
import net.aethersanctum.fractus.{Rule, PartRandom, RuleState}
import net.aethersanctum.fractus.Rule._


object Spongy extends PartRandom {

  override val name = "spongy"

  private val third = 1.0 / 3

  override val rules = Array[Rule](
    weight(1) color RED colorWeight 0.5 scale third translate(-1.5, 1.5),
    weight(1) color GREEN colorWeight 0.5 scale third translate(0.0, 1.5),
    weight(1) color RED colorWeight 0.5 scale third translate(1.5, 1.5),

    weight(1) color GREEN colorWeight 0.5 scale third translate(-1.5, 0.0),
    weight(0) color BLACK colorWeight 0.5 scale third translate(0.0, 0.0),
    weight(1) color GREEN colorWeight 0.5 scale third translate(1.5, 0.0),

    weight(1) color RED colorWeight 0.5 scale third translate(-1.5, -1.5),
    weight(1) color GREEN colorWeight 0.5 scale third translate(0.0, -1.5),
    weight(1) color RED colorWeight 0.5 scale third translate(1.5, -1.5)
  )

  val disallowedTransitions: Map[RuleState, Set[Int]] = Map(
    RuleState(1,1) -> Set(3,5,6,7,8),
    RuleState(1,7) -> Set(0,1,2,3,5),
    RuleState(7,7) -> Set(0,1,2,3,5),
    RuleState(7,1) -> Set(3,5,6,7,8),
    RuleState(3,3) -> Set(1,2,5,7,8),
    RuleState(3,5) -> Set(0,1,3,6,7),
    RuleState(5,5) -> Set(0,3,1,6,7),
    RuleState(5,3) -> Set(1,2,5,7,8)
  )

  def disallowed(state:RuleState, next:Int) = {
    disallowedTransitions.get(state).exists(set => set.contains(next))
  }

  val possibleCombinations: Seq[(RuleState,Int)] = for {
    previous <- 0 until rules.length
    current <- 0 until rules.length
    next <- 0 until rules.length
    key <- List(RuleState(previous, current)) if !disallowed(key, next)
  } yield (key, next)

  val emptyMap: Map[RuleState, List[Int]] = Map()

  val allowedTransitions: Map[RuleState, List[Int]] = possibleCombinations.foldLeft(emptyMap) {
    case (result, (state, next)) =>
      result.updated(state, next :: result.getOrElse(state, Nil))
  }

  override def customTransition(state: RuleState): Int = {
    val possible = allowedTransitions(state)
    possible((random * possible.length).toInt)
  }
}
