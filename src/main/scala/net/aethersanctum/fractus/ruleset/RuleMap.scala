package net.aethersanctum.fractus.ruleset

import net.aethersanctum.fractus.Rule._
import java.awt.Color._
import net.aethersanctum.fractus.Colors._
import Math._
import net.aethersanctum.fractus.{Vector, Vector2, RuleSet, Rule}

object RuleMap extends (String=>Option[RuleSet]) {

  implicit def ruleArrayToRuleSet(a:Array[Rule]):RuleSet = new RuleSet() {
    override def getRules = a
  }

  def ruleArrayToSetScaled(a:Array[Rule], mscale:Double):RuleSet = new RuleSet() {
    override def getRules = a
    override def scale = mscale
  }

  override def apply(name:String): Option[RuleSet] = {
    items.get(name)
  }

  val items:Map[String,RuleSet] = Map(
    "sierpinski" -> Array[Rule](
      weight(1) color(RED)   also { _ * 0.5 + ( 1.0,  0.0) },
      weight(1) color(GREEN) also { _ * 0.5 + (-1.0, -1.0) },
      weight(1) color(BLUE)  also { _ * 0.5 + (-1.0,  1.0) }
    ),
    "spirally" -> Array[Rule](
      weight(30) color(WHITE) colorWeight(0.05) scale(0.99) rotate(70),
      weight(1)  color(BLUE)  colorWeight(0.50) scale(0.1) translate(2,0)
    ),
    "spirally2" -> Array[Rule](
      weight(50) color(CREAM) colorWeight(0.02) scale(0.99) rotate(70),
      weight(1)  color(RED)  colorWeight(0.90) scale(0.1) translate(5,0),
      weight(1)  color(WHITE)  colorWeight(0.90) scale(0.1) translate(-5,0)
    ),
    "sinx" -> Array[Rule](
      weight(1).color(WHITE).colorWeight(0.5).scale(8).sine.scale(1.0).translate(-0.5,0.0),
      weight(1).color(BLACK).colorWeight(1).scale(5).sine.scale(0.333).translate(-0.5,0.0),
      weight(1).color(BURGUNDY).colorWeight(0.5).polar
    ),
    "pinkfur" -> Array[Rule](
      weight(20).color(WHITE).colorWeight(0.1).scale(0.8).rotate(72).translate(-0.5,0),
      weight(20 ).color(BURGUNDY).colorWeight(0.5).polar
    ),
    "pinkfur2" -> Array[Rule](
      weight(1)  .color(BLUE).colorWeight(0.5).scale(0.25).translate(3,0).rotate(36),
      weight(1)  .color(GREEN).colorWeight(0.5).invertRadius.scale(1),
      weight(3)  .color(CREAM).colorWeight(0.5).scale(0.15).translate(1,0),
      weight(100).color(WHITE).colorWeight(0.0).rotate(72),
      weight(15 ).color(BURGUNDY).colorWeight(0.5).polar.scale(0.8,0.8).translate(0.5,0)
    ),
    "pinkfur3" -> Array[Rule](
      weight(3).color(CREAM).colorWeight(0.5).scale(0.15).translate(2,0),
      weight(50).color(WHITE).colorWeight(0.0).scale(-1,1),
      weight(50).color(WHITE).colorWeight(0.0).scale(1,-1),
      weight(50).color(WHITE).colorWeight(0.0).rotate(72),
      weight(3).color(BURGUNDY).colorWeight(0.5).polar.scale(0.3,0.7)
    ),
    "AlienBlueCheeseLotus" -> Array[Rule](
      // THIS ONE IS A FULLY AWESOME DERIVATIVE OF THE ALIEN BLUE CHEESE FRACTAL
      weight(0.15).color(BLUE).colorWeight(0.3).also( _ + (2.0, 1.0) ),
      weight(1).color(WHITE).colorWeight(0.07).also(
        (in:Vector2) => {
					val x = in.x
					val y = in.y
					var r = sqrt(x*x + y*y)
					var t = atan2(x, -y)
          r = 0.80 *r + 0.15;
          t = t + Pi/4;
					Vector(r*cos(t), r*sin(t) + 1)
        }
      )
    ),
    "AlienBlueCheeseLotus2" -> ruleArrayToSetScaled(Array[Rule](
      // THIS ONE IS A FULLY AWESOME DERIVATIVE OF THE ALIEN BLUE CHEESE FRACTAL
      weight(0.15).color(BLUE).colorWeight(0.3).also( _ + (2.0, 1.0) ),
      weight(1).color(WHITE).colorWeight(0.07).also(
        (in:Vector2) => {
          //  (_ * (1.0,-1)).polar( _ * (0.8, 1.0) + (0.15,0) ) + (0.0, 1.0)
					val (x,y) = (in.x, -in.y)
					var r = sqrt(x*x + y*y)  // go into polar space
					var t = atan2(x, y)
          r = 0.80 *r + 0.15;
          t = t + Pi/4;
					Vector(r*cos(t), r*sin(t) + 1) // back to cartesian space
        }
      ),
      weight(0.01).color(RED).colorWeight(0.7).scale(0.05)
    ), 1.5),
    "AlienBlueCheese" -> Array[Rule](
      // THIS ONE IS THE ORIGINAL ALIEN BLUE CHEESE FRACTAL
      weight(0.15).color(BLUE).colorWeight(0.3).also( _ + (2.0, 1.0) ),
      weight(1).color(WHITE).colorWeight(0.07).also(
        (in:Vector2) => {
					val (x,y) = (in.x, in.y)
          var r = sqrt(x*x + y*y)
					var t = atan2(x, y)
          r = 0.80 *r + 0.15;
          t = t + Pi/4;
					Vector(r*cos(t), r*sin(t))
        }
      )
    ),
    "eggs" -> Array[Rule](
          weight(1).color(WHITE).colorWeight(0.70).invertRadius.scale(2.0,1).translate(1,2.5),
          weight(2).color(RED).colorWeight(0.10).also( _ * (0.5,1.0) + (-1.0,0.0)),
          weight(1).color(GREEN).colorWeight(0.5).polar.scale(-0.5,0.25).translate(2,2),
          weight(4).color(GREEN).colorWeight(0).also( _ + (0,-1) )
        ),
    "eggs_mirror" -> Array[Rule](
          weight(30).color(WHITE).colorWeight(0).scale(-1,1),
          weight(1).color(WHITE).colorWeight(0.70).invertRadius.scale(2.0,1).translate(1,2.5),
          weight(2).color(RED).colorWeight(0.10).also( _ * (0.5,1.0) + (-1.0,0.0)),
          weight(1).color(GREEN).colorWeight(0.5).polar.scale(-0.5,0.25).translate(2,2),
          weight(4).color(GREEN).colorWeight(0).also( _ + (0,-1) )
        ),
    "polar1" -> Array[Rule](
          weight(1).color(GREEN).colorWeight(0.5).polar.scale(-0.5,0.25).translate(2,2),
          weight(4).color(GREEN).colorWeight(0.5).also( _ + (0,-1) )
        ),
    "starkle" ->  new RuleSet() {

      override def getRules:Array[Rule] = {
        Array[Rule](
          weight(1).color(BURGUNDY).colorWeight(0.70).scale(0.06).translate(0,2),
          weight(1).color(CREAM).colorWeight(0.60).scale(0.06).translate(2,0),
          weight(3).color(GREEN).colorWeight(0.1).scale(0.75),
          weight(2).color(ORANGE).colorWeight(0.5).also(
              (v:Vector2) => Vector(2-1/(1+v.x * v.x), v.y)
            ).translate(1,1).polar.scale(0.7),
          weight(10).color(GREEN).colorWeight(0).rotate(60),
          weight(10).color(GREEN).colorWeight(0).scale(1,-1),
          weight(10).color(GREEN).colorWeight(0).scale(-1,1)
        )
      }

      override def nextIndex:Int = {
        current match {
          //case 2 => if (random > 0.5) 1 else 3
          //case 3 => if (random > 0.1) 3 else super.nextIndex
          case _ => super.nextIndex
        }
      }
    },
    "spongy2" -> new RuleSet() {

      override def getRules:Array[Rule] = {
        Array(
          weight(1) color(RED)   colorWeight(0.5) scale(1.0/3) translate(-1.5,  1.5),
          weight(1) color(GREEN) colorWeight(0.5) scale(1.0/3) translate( 0.0,  1.5),
          weight(1) color(RED)   colorWeight(0.5) scale(1.0/3) translate( 1.5,  1.5),

          weight(1) color(GREEN) colorWeight(0.5) scale(1.0/3) translate(-1.5,  0.0),
          weight(0) color(BLACK) colorWeight(0.5) scale(1.0/3) translate( 0.0,  0.0),
          weight(1) color(GREEN) colorWeight(0.5) scale(1.0/3) translate( 1.5,  0.0),

          weight(1) color(RED)   colorWeight(0.5) scale(1.0/3) translate(-1.5, -1.5),
          weight(1) color(GREEN) colorWeight(0.5) scale(1.0/3) translate( 0.0, -1.5),
          weight(1) color(RED)   colorWeight(0.5) scale(1.0/3) translate( 1.5, -1.5)
        )
      }

      /** randomly choose something from the array of rule indices */
      def allow(n: Array[Int]) ={
        val p = (random * n.length).toInt
        n(p)
      }

      override def nextIndex:Int = {
        var ok = false
        var default = -1
        while (!ok) {
          default = super.nextIndex
          // figure out which combinations of previous, current and next rules we don't want
          ok = (previous,current,default) match {
            case (1,7,0) => false
            case (7,7,0) => false
            case (3,5,0) => false
            case (5,5,0) => false

            case (1,7,1) => false
            case (7,7,1) => false
            case (3,3,1) => false
            case (5,3,1) => false
            case (3,5,1) => false
            case (5,5,1) => false

            case (1,7,2) => false
            case (7,7,2) => false
            case (3,3,2) => false
            case (5,3,2) => false

            case (1,1,3) => false
            case (1,7,3) => false
            case (7,1,3) => false
            case (7,7,3) => false
            case (3,5,3) => false
            case (5,5,3) => false

            case (1,1,5) => false
            case (1,7,5) => false
            case (7,1,5) => false
            case (7,7,5) => false
            case (5,3,5) => false
            case (3,3,5) => false

            case (1,1,6) => false
            case (7,1,6) => false
            case (3,5,6) => false
            case (5,5,6) => false

            case (1,1,7) => false
            case (7,1,7) => false
            case (3,3,7) => false
            case (5,3,7) => false
            case (3,5,7) => false
            case (5,5,7) => false

            case (1,1,8) => false
            case (7,1,8) => false
            case (3,3,8) => false
            case (5,3,8) => false
            case _ => true
          }
        }
        default
      }
    }
  )

}
