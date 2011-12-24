package net.aethersanctum.fractus.examples

import java.awt.Color._
import net.aethersanctum.fractus.Colors._
import math._
import net.aethersanctum.fractus.Vector._
import net.aethersanctum.fractus.Rule._
import net.aethersanctum.fractus.Transform._
import net.aethersanctum.fractus._

/**
 * Example fractal RuleSets, held in a Map by name.
 */
object Examples extends (String=>Option[RuleBasedFractal]) {

  implicit def buildRules(builders:Traversable[RuleBuilder]) = builders.map { _.build }

  implicit def buildRule(builder:RuleBuilder) = builder.build

  implicit def ruleSet(a:Traversable[Rule]):RuleBasedFractal = RuleBasedFractal(a)

  def ruleSetScaled(a:Traversable[Rule], mscale:Double):RuleBasedFractal = new RandomSelectionRuleBasedFractal(a.toArray[Rule]) {
    override def scale = mscale
  }

  override def apply(name:String): Option[RuleBasedFractal] = {
    items.get(name)
  }

  def weight(w:Double) : RuleBuilder = rule.weight(w)

  val items:Map[String,RuleBasedFractal] = Map(
    "sierpinski" -> RuleBasedFractal(
      rule weight 1 color RED   also { _ * 0.5 + ( 1.0,  0.0) },
      rule weight 1 color GREEN also { _ * 0.5 + (-1.0, -1.0) },
      rule weight 1 color BLUE  also { _ * 0.5 + (-1.0,  1.0) }
    ),
    "spirally" -> RuleBasedFractal(
      rule weight 30  color WHITE colorWeight 0.05 scale 0.99 rotate 70,
      rule weight 1.0 color BLUE  colorWeight 0.50 scale 0.10 translate(2,0)
    ),
    "spirally2" -> RuleBasedFractal(
      rule weight 80  color CREAM colorWeight 0.02 scale 0.99 rotate 70,
      rule weight 1   color RED   colorWeight 0.90 scale 0.10 translate(5,0),
      rule weight 1   color WHITE colorWeight 0.90 scale 0.10 translate(-5,0)
    ),
    
    "block-tree" -> RuleBasedFractal(
      rule.weight(1).color(RED).colorWeight(0.8).transform( (v:Vector2) => 
          new Vector2( (sin(v.x *31 + v.y*53 +1)*0.5+0.5) * 0.2, sin(v.x *29 + v.y*47 +3)*0.5+0.5)
      ),
      rule.weight(8).color(GREEN).colorWeight(0.1).scale(0.8).rotate(45).translate(0,1),
      rule.weight(8).color(GREEN).colorWeight(0.1).scale(0.8).rotate(45).scale(-1,1).translate(0.2,1)
    ),
    "circles" -> RuleBasedFractal(
      rule.weight(4).color(RED).colorWeight(0.8).transform( (v:Vector2) => 
          new Vector2(sin(v.x *131 + v.y*153 +1)*0.5+0.6, sin(v.x *229 + v.y*147 +3)*1000)
      ).cartesian.invertRadius.scale(0.3).translate(-2,1).rotate(10),
      rule.weight(6).color(GREEN).colorWeight(0.1).scale(0.8).translate(0.05,-0.8),
      rule.weight(6).color(YELLOW).colorWeight(0.1).polar.translate(-1,2),
      rule.weight(4).color(BLUE).colorWeight(1).transform( (v:Vector2) => 
          new Vector2( -0.3, sin(v.x *229 + v.y*147 +3)*10)
      ).rotate(45),
      rule.weight(50).color(YELLOW).colorWeight(0.0).scale(-1,1)
    ),
    "gorgon" -> RuleBasedFractal(
      rule.weight(14).color(RED).colorWeight(0.2).polar.translate(0.1,0),
      rule.weight(4).color(WHITE).colorWeight(0.2).invertRadius.scale(2),
      rule.weight(4).color(GREEN).colorWeight(0.2).scale(0.4).rotate(90).translate(-1.5, 1.5),
      rule.weight(40).color(BLACK).colorWeight(0.0).scale(1,-1)
    ),
    "gorgon2" -> RuleBasedFractal(
      rule.weight(14).color(RED).colorWeight(0.2).polar.translate(0.1,0).scale(0.9),
      rule.weight(4).color(GREEN).colorWeight(0.2).scale(0.8).rotate(60).translate(-0.9,0.3)
    ),
    "fern" -> RuleBasedFractal(
      rule.weight(4).color(RED).colorWeight(0.8).transform( (v:Vector2) => {
          new Vector2(
            (sin(v.x *31 + v.y*53 +1)*0.5+0.5) * 0.2,
             sin(v.x *29 + v.y*47 +3)*0.5+0.5
          )
        }
      ).translate(0,-1),
      rule.weight(80).color(GREEN).colorWeight(0.1).translate(0,1).scale(0.9).rotate(-5),
      rule.weight(6).color(GREEN).colorWeight(0.1).translate(0,1).scale(0.2).rotate(80),
      rule.weight(6).color(GREEN).colorWeight(0.1).translate(0,1).scale(0.2).rotate(80).scale(-1,1).translate(0.2,0)
    ),
    "dual-spiral" -> RuleBasedFractal(
      rule.weight(10).color(CREAM).colorWeight(0.9).transform( (v:Vector2) => {
          val y = v.y + 2
          new Vector2(v.x *1.1, 4/ (y*y +1) +1)
        }
      ),
      rule weight 100  color RED   colorWeight 0.0 rotate(120),
      rule.weight(10).color(GREEN).colorWeight(0.5).invertRadius.scale(2),
      rule.weight(1).color(YELLOW).colorWeight(0.5).translate(5,0).invertRadius.scale(10)
    ),
    "sinx" -> RuleBasedFractal(
      rule.weight(1). color(WHITE).    colorWeight(0.5).scale(8.0).sine.scale(1.000) translate(-0.5,0.0),
      rule.weight(1).color(BLACK).    colorWeight(1.0).scale(5.0).sine.scale(0.333) translate(-0.5,0.0),
      rule weight 1 color BURGUNDY colorWeight 0.5 polar
    ),
    "pinkfur" -> RuleBasedFractal(
      rule weight 1 color WHITE    colorWeight 0.1 scale 0.8 rotate 72 translate(-0.5,0),
      rule weight 1 color BURGUNDY colorWeight 0.5 polar
    ),
    "pinkfur2" -> RuleBasedFractal(
      weight(1)  .color(BLUE).colorWeight(0.5).scale(0.25).translate(3,0).rotate(36),
      weight(1)  .color(GREEN).colorWeight(0.5).invertRadius.scale(1),
      weight(3)  .color(CREAM).colorWeight(0.5).scale(0.15).translate(1,0),
      weight(100).color(WHITE).colorWeight(0.0).rotate(72),
      weight(15 ).color(BURGUNDY).colorWeight(0.5).polar.scale(0.8,0.8).translate(0.5,0)
    ),
    "pinkfur3" -> RuleBasedFractal(
      weight(3).color(CREAM).colorWeight(0.5).scale(0.15).translate(2,0),
      weight(50).color(WHITE).colorWeight(0.0).scale(-1,1),
      weight(50).color(WHITE).colorWeight(0.0).scale(1,-1),
      weight(50).color(WHITE).colorWeight(0.0).rotate(72),
      weight(3).color(BURGUNDY).colorWeight(0.5).polar.scale(0.3,0.7)
    ),
    "AlienBlueCheeseLotus" -> RuleBasedFractal(
      // THIS ONE IS A FULLY AWESOME DERIVATIVE OF THE ALIEN BLUE CHEESE FRACTAL
      weight(0.15).color(BLUE).colorWeight(0.3).translate(2.0, 1.0),
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
    "AlienBlueCheeseLotus2" -> ruleSetScaled(List(
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
    "AlienBlueCheese" -> RuleBasedFractal(
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
    "Borfo" -> RuleBasedFractal(
      // blorp
      weight(1).color(BLUE).colorWeight(0.3).scale(0.3).translate(3.0,0.0),
      weight(5).color(RED).colorWeight(0.0).rotate(72),
      weight(5).color(WHITE).colorWeight(0.1).also(
        (in:Vector2) => {
          val (x,y) = (in.x, in.y)
          var r = sqrt(x*x + y*y)
          var t = atan2(x, y)
          r = 0.2 + (0.8*r);
          t = t + 13*Pi/29;
          Vector(r*cos(t), r*sin(t))
        }
      )
    ),
    "Squaresville" ->  new SquaresVille,
    "eggs" -> RuleBasedFractal(
          weight(1).color(WHITE).colorWeight(0.70).invertRadius.scale(2.0,1).translate(1,2.5),
          weight(2).color(RED).colorWeight(0.10).also( _ * (0.5,1.0) + (-1.0,0.0)),
          weight(1).color(GREEN).colorWeight(0.5).polar.scale(-0.5,0.25).translate(2,2),
          weight(4).color(GREEN).colorWeight(0).also( _ + Vector(0,-1) )
        ),
    "eggs_mirror" -> RuleBasedFractal(
          weight(30).color(WHITE).colorWeight(0).scale(-1,1),
          weight(1).color(WHITE).colorWeight(0.70).invertRadius.scale(2.0,1).translate(1,2.5),
          weight(2).color(RED).colorWeight(0.10).also( _ * (0.5,1.0) + (-1.0,0.0)),
          weight(1).color(GREEN).colorWeight(0.5).polar.scale(-0.5,0.25).translate(2,2),
          weight(4).color(GREEN).colorWeight(0).also( _ + Vector(0,-1) )
        ),
    "polar1" -> RuleBasedFractal(
          weight(1).color(GREEN).colorWeight(0.5).polar.scale(-0.5,0.25).translate(2,2),
          weight(4).color(GREEN).colorWeight(0.5).also( _ + Vector(0,-1) )
        ),
    "starkle" -> new Starkle,
    "spongy2" -> new Spongy

  )

}
