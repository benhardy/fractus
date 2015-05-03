package net.aethersanctum.fractus.examples

import java.awt.Color._

import net.aethersanctum.fractus.Colors._
import net.aethersanctum.fractus.Rule._
import net.aethersanctum.fractus.Transform._
import net.aethersanctum.fractus.Vector._
import net.aethersanctum.fractus._

import scala.math._

/**
 * Example fractal definitions, which can be looked up by fractalName.
 */
object Examples extends (String => Option[RuleBasedFractal]) {

  implicit def buildRules(builders: Traversable[RuleBuilder]) = builders.map {
    _.build
  }

  implicit def buildRule(builder: RuleBuilder) = builder.build

  implicit def ruleSet(name: String, a: Traversable[Rule]): RuleBasedFractal = RuleBasedFractal(name, a)

  override def apply(name: String): Option[RuleBasedFractal] = {
    items.get(name)
  }

  def weight(w: Double): RuleBuilder = rule.weight(w)

  def getFractalNames = {
    items.keySet.toArray.sorted
  }

  private val random = new java.util.Random()

  def randomSelection = {
    val ordered = items.values.toArray
    val pos = (random.nextInt() & 0xFFFF) % ordered.length
    ordered(pos)
  }

  val examples = List(
    RuleBasedFractal("sierpinski",
      rule weight 1 color RED scale 0.5 translate(-1.0, -1.0),
      rule weight 1 color GREEN scale 0.5 translate(-1.0, 1.0),
      rule weight 1 color WHITE scale 0.5 translate(1.0, 0.0)
    ),
    RuleBasedFractal("spirally",
      rule weight 30 color WHITE colorWeight 0.05 scale 0.99 rotate 70,
      rule.weight(8).color(GREEN).colorWeight(0.1).scale(0.8).rotate(45).translate(0, 1),
      rule.weight(8).color(GREEN).colorWeight(0.1).scale(0.8).rotate(45).scale(-1, 1).translate(0.2, 1)
    ),
    RuleBasedFractal("spirally2",
      rule weight 80 color CREAM colorWeight 0.02 scale 0.99 rotate 70,
      rule weight 1 color RED colorWeight 0.90 scale 0.10 translate(5, 0),
      rule weight 1 color WHITE colorWeight 0.90 scale 0.10 translate(-5, 0)
    ),
    RuleBasedFractal("block-tree",
      rule.weight(1).color(RED).colorWeight(0.8).transform((v: Vector2) =>
        new Vector2((sin(v.x * 31 + v.y * 53 + 1) * 0.5 + 0.5) * 0.2, sin(v.x * 29 + v.y * 47 + 3) * 0.5 + 0.5)
      ),
      rule.weight(8).color(GREEN).colorWeight(0.1).scale(0.8).rotate(45).translate(0, 1),
      rule.weight(8).color(GREEN).colorWeight(0.1).scale(0.8).rotate(45).scale(-1, 1).translate(0.2, 1)
    ),
    RuleBasedFractal("circles",
      rule.weight(4).color(RED).colorWeight(0.8).transform((v: Vector2) =>
        new Vector2(sin(v.x * 131 + v.y * 153 + 1) * 0.5 + 0.6, sin(v.x * 229 + v.y * 147 + 3) * 1000)
      ).cartesian.invertRadius.scale(0.3).translate(-2, 1).rotate(10),
      rule.weight(6).color(GREEN).colorWeight(0.1).scale(0.8).translate(0.05, -0.8),
      rule.weight(6).color(YELLOW).colorWeight(0.1).polar.translate(-1, 2),
      rule.weight(4).color(BLUE).colorWeight(1).transform((v: Vector2) =>
        new Vector2(-0.3, sin(v.x * 229 + v.y * 147 + 3) * 10)
      ).rotate(45),
      rule.weight(50).color(YELLOW).colorWeight(0.0).scale(-1, 1)
    ),
    RuleBasedFractal("gorgon",
      rule.weight(14).color(RED).colorWeight(0.2).polar.translate(0.1, 0),
      rule.weight(4).color(WHITE).colorWeight(0.2).invertRadius.scale(2),
      rule.weight(4).color(GREEN).colorWeight(0.2).scale(0.4).rotate(90).translate(-1.5, 1.5),
      rule.weight(40).color(BLACK).colorWeight(0.0).scale(1, -1)
    ),
    RuleBasedFractal("gorgon2",
      rule.weight(14).color(RED).colorWeight(0.2).polar.translate(0.1, 0).scale(0.9),
      rule.weight(4).color(GREEN).colorWeight(0.2).scale(0.8).rotate(60).translate(-0.9, 0.3)
    ),
    RuleBasedFractal("fern",
      rule.weight(4).color(RED).colorWeight(0.8).transform((v: Vector2) => {
        new Vector2(
          (sin(v.x * 31 + v.y * 53 + 1) * 0.5 + 0.5) * 0.2,
          sin(v.x * 29 + v.y * 47 + 3) * 0.5 + 0.5
        )
      }
      ).translate(0, -1),
      rule.weight(80).color(GREEN).colorWeight(0.1).translate(0, 1).scale(0.9).rotate(-5),
      rule.weight(6).color(GREEN).colorWeight(0.1).translate(0, 1).scale(0.2).rotate(80),
      rule.weight(6).color(GREEN).colorWeight(0.1).translate(0, 1).scale(0.2).rotate(80).scale(-1, 1).translate(0.2, 0)
    ),
    RuleBasedFractal("dual-spiral",
      rule.weight(10).color(CREAM).colorWeight(0.9).transform((v: Vector2) => {
        val y = v.y + 2
        new Vector2(v.x * 1.1, 4 / (y * y + 1) + 1)
      }
      ),
      rule weight 100 color RED colorWeight 0.0 rotate (120),
      rule.weight(10).color(GREEN).colorWeight(0.5).invertRadius.scale(2),
      rule.weight(1).color(YELLOW).colorWeight(0.5).translate(5, 0).invertRadius.scale(10)
    ),
    RuleBasedFractal("sinx",
      rule.weight(1).color(WHITE).colorWeight(0.5).scale(8.0).sine.scale(1.000) translate(-0.5, 0.0),
      rule.weight(1).color(BLACK).colorWeight(1.0).scale(5.0).sine.scale(0.333) translate(-0.5, 0.0),
      rule weight 1 color BURGUNDY colorWeight 0.5 polar
    ),
    RuleBasedFractal("pinkfur",
      rule weight 1 color WHITE colorWeight 0.1 scale 0.8 rotate 72 translate(-0.5, 0),
      rule weight 1 color BURGUNDY colorWeight 0.5 polar
    ),
    RuleBasedFractal("pinkfur2",
      weight(1).color(BLUE).colorWeight(0.5).scale(0.25).translate(3, 0).rotate(36),
      weight(1).color(GREEN).colorWeight(0.5).invertRadius.scale(1),
      weight(3).color(CREAM).colorWeight(0.5).scale(0.15).translate(1, 0),
      weight(100).color(WHITE).colorWeight(0.0).rotate(72),
      weight(15).color(BURGUNDY).colorWeight(0.5).polar.scale(0.8, 0.8).translate(0.5, 0)
    ),
    RuleBasedFractal("pinkfur3",
      weight(3).color(CREAM).colorWeight(0.5).scale(0.15).translate(2, 0),
      weight(50).color(WHITE).colorWeight(0.0).scale(-1, 1),
      weight(50).color(WHITE).colorWeight(0.0).scale(1, -1),
      weight(50).color(WHITE).colorWeight(0.0).rotate(72),
      weight(3).color(BURGUNDY).colorWeight(0.5).polar.scale(0.3, 0.7)
    ),
    RuleBasedFractal("AlienBlueCheeseLotus",
      // THIS ONE IS A FULLY AWESOME DERIVATIVE OF THE ALIEN BLUE CHEESE FRACTAL
      weight(0.15).color(BLUE).colorWeight(0.3).translate(2.0, 1.0),
      weight(1).color(WHITE).colorWeight(0.07).also(
        (in: Vector2) => {
          val x = in.x
          val y = in.y
          var r = sqrt(x * x + y * y)
          var t = atan2(x, -y)
          r = 0.80 * r + 0.15;
          t = t + Pi / 4;
          Vector(r * cos(t), r * sin(t) + 1)
        }
      )
    ),
    RuleBasedFractal("AlienBlueCheeseLotus2", 1.5,
      // THIS ONE IS A FULLY AWESOME DERIVATIVE OF THE ALIEN BLUE CHEESE FRACTAL
      weight(0.15).color(BLUE).colorWeight(0.3).also(_ +(2.0, 1.0)),
      weight(1).color(WHITE).colorWeight(0.07).also(
        (in: Vector2) => {
          //  (_ * (1.0,-1)).polar( _ * (0.8, 1.0) + (0.15,0) ) + (0.0, 1.0)
          val (x, y) = (in.x, -in.y)
          var r = sqrt(x * x + y * y) // go into polar space
          var t = atan2(x, y)
          r = 0.80 * r + 0.15;
          t = t + Pi / 4;
          Vector(r * cos(t), r * sin(t) + 1) // back to cartesian space
        }
      ),
      weight(0.01).color(RED).colorWeight(0.7).scale(0.05)
    ),
    RuleBasedFractal("AlienBlueCheese",
      // THIS ONE IS THE ORIGINAL ALIEN BLUE CHEESE FRACTAL
      weight(0.15).color(BLUE).colorWeight(0.3).translate(2.0, 1.0),
      weight(1).color(WHITE).colorWeight(0.07).inPolarSpace(
        (in: PolarVector) =>
          PolarVector(0.80 * in.r + 0.15, in.t + (2 * 8 * Pi / 13))
      )
    ),
    RuleBasedFractal("Borfo",
      // blorp
      weight(1).color(BLUE).colorWeight(0.3).scale(0.3).translate(3.0, 0.0),
      weight(5).color(RED).colorWeight(0.0).rotate(72),
      weight(5).color(WHITE).colorWeight(0.1).also(
        (in: Vector2) => {
          val (x, y) = (in.x, in.y)
          var r = sqrt(x * x + y * y)
          var t = atan2(x, y)
          r = 0.2 + (0.8 * r);
          t = t + 13 * Pi / 29;
          Vector(r * cos(t), r * sin(t))
        }
      )
    ),
    RuleBasedFractal("taffy-1",
      weight(2).color(ORANGE).translate(0.5, 0.5).colorWeight(0.3).scale(0.9).translate(-0.2, -0.2).translate(-0.5, -0.5),
      weight(2).color(DARK_GREEN).translate(0.5, 0.5).colorWeight(0.1).scale(0.1, 0.7).rotate(-60).translate(-1.3, 1).translate(-0.5, -0.5),
      weight(2).color(Colors.CREAM).translate(0.5, 0.5).colorWeight(0.2).polar.translate(-0.5, -0.5)
    ),
    RuleBasedFractal("hazylines",
      weight(2).color(PURPLE).colorWeight(0.3).scale(-1, 1).translate(-1, -1),
      weight(2).color(DARK_GREEN).colorWeight(0.3).scale(0.01, 0.9),
      weight(2).color(RED).colorWeight(0.3).rotate(5),
      weight(5).color(Colors.CREAM).colorWeight(0.3).polar.translate(-1.0, 0.2)
    ),
    RuleBasedFractal("purplerain",
      weight(2).color(PURPLE).colorWeight(0.3).rotate(10).translate(2.0, 1.0).
        scale(4).sine.scale(5).
        translate(-2.0, -1.0).rotate(-10),
      weight(6.5).color(Colors.CREAM).colorWeight(0.3).rotate(10).
        translate(2.0, 1.0).polar.
        translate(-3.0, -0.8).rotate(-10)
    ),
    RuleBasedFractal("purplerain2",
      0.5, // zoom out a bit
      weight(0.03).color(RED).colorWeight(0).translate(0.000, 0.001),
      weight(1).color(RED).colorWeight(1).scale(2,0.003).translate(0,3),
      weight(2).color(PURPLE).colorWeight(0.3).scale(0.03,0.03).translate(0,-3),
      weight(3).color(Colors.CREAM).colorWeight(0.6).cartesian.rotate(-90).scale(2,0.01).translate(3,0.7),
      weight(80) color WHITE colorWeight 0 rotate 72,
      weight(80) color WHITE colorWeight 0 scale (-1,1)
    ),
    RuleBasedFractal("purple-brain",
      weight(5).color(PURPLE).colorWeight(0.1).scale(0.6, 0.8).rotate(30).translate(-1, 0),
      weight(1).color(Colors.CREAM).colorWeight(1).polar.invertRadius.scale(3),
      weight(20).color(RED).colorWeight(0.0).scale(-1, 1)
    ),
    RuleBasedFractal("mlike-boundary", // makes a vaguely mandelbrot-set-boundary shape
      weight(1).color(RED).colorWeight(0.3).scale(0.333, 0.333).translate(-1.333, 0),
      weight(1).color(Colors.CREAM).colorWeight(0.7).inPolarSpace {
        (p: PolarVector) => PolarVector((p.r + 1) / 2, p.t / 2)
      },
      weight(1).color(Colors.CREAM).colorWeight(0.7).inPolarSpace {
        (p: PolarVector) => PolarVector((p.r + 1) / 2, Pi + p.t / 2)
      }
    ),
    RuleBasedFractal("mlike-boundary-2",
      weight(5).color(YELLOW).colorWeight(0.3).scale(0.1).translate(-1.0, 0),
      weight(5).color(GREEN).colorWeight(0.3).sine,
      weight(5).color(BLUE).colorWeight(0.3).polar,
      weight(1).color(RED).colorWeight(0.3).complexSquared
    ),
    RuleBasedFractal("filament-reactor",
      weight(25).color(RED).colorWeight(0.3).scale(0.001, 0.5).translate(-0.8, 0),
      weight(100).color(Colors.CREAM).colorWeight(0.3).polar,
      weight(70).color(Colors.PURPLE).colorWeight(0.3).also(
        (Z: Vector2) => {
          val (real, imag) = (Z.x, Z.y)
          val (r2, i2) = (real * real, imag * imag)
          if (r2 > 10) {
            (real / 100 - 1.5, imag / 100)
          }
          else {
            Vector(r2 - i2, 2 * real * imag)
          }
        }
      )
    ),
    RuleBasedFractal("filament-reactor-2",
      weight(10).color(CREAM).colorWeight(0.2).polar.scale(0.6).translate(1, -0.5),
      weight(10).color(RED).colorWeight(0.2).invertRadius,
      weight(10).color(GREEN).colorWeight(0.2).scale(0.6).rotate(10).translate(-1, 1)
    ),
    RuleBasedFractal("eggs",
      weight(1).color(WHITE).colorWeight(0.70).invertRadius.scale(2.0, 1).translate(1, 2.5),
      weight(2).color(RED).colorWeight(0.10).also(_ *(0.5, 1.0) +(-1.0, 0.0)),
      weight(1).color(GREEN).colorWeight(0.5).polar.scale(-0.5, 0.25).translate(2, 2),
      weight(4).color(GREEN).colorWeight(0).also(_ + Vector(0, -1))
    ),
    RuleBasedFractal("eggs_mirror",
      weight(30).color(WHITE).colorWeight(0).scale(-1, 1),
      weight(1).color(WHITE).colorWeight(0.70).invertRadius.scale(2.0, 1).translate(1, 2.5),
      weight(2).color(RED).colorWeight(0.10).also(_ *(0.5, 1.0) +(-1.0, 0.0)),
      weight(1).color(GREEN).colorWeight(0.5).polar.scale(-0.5, 0.25).translate(2, 2),
      weight(4).color(GREEN).colorWeight(0).also(_ + Vector(0, -1))
    ),
    RuleBasedFractal("polar1",
      weight(1).color(GREEN).colorWeight(0.5).polar.scale(-0.5, 0.25).translate(2, 2),
      weight(4).color(GREEN).colorWeight(0.5).also(_ + Vector(0, -1))
    ),
    RuleBasedFractal("polar2",
      weight(1).color(RED).colorWeight(1).scale(1, 0.01).translate(0, -3),
      weight(3).color(GREEN).colorWeight(0.3).polar.scale(2).translate(1, -1),
      weight(1).color(ORANGE).colorWeight(0.3).invertRadius.scale(2).translate(-1.5, -0.3),
      weight(5) color CREAM colorWeight 0.3 scale 0.8 rotate 80 translate(0.5, 0.5)
    ),
    RuleBasedFractal("polar3",

      weight(2).color(ORANGE).colorWeight(0.3).rotate(180).invertRadius.scale(3).translate(-1.5, -0.3),
      weight(3) color CREAM colorWeight 0.3 rotate 82 scale 0.7 translate(1.2, 0),
      weight(1).color(BLUE).colorWeight(0.7).polar.scale(2).translate(1, -1)
    ),
    RuleBasedFractal("polar4",
      weight(2).color(ORANGE).colorWeight(0.3).rotate(72).invertRadius.scale(4),
      weight(3) color CREAM colorWeight 0.3 rotate 10 translate(5, 0) scale 0.9,
      weight(1).color(PURPLE).colorWeight(0.3).polar.scale(-2).translate(0, -1)
    ),
    RuleBasedFractal("polar5",
      //   weight(2).color(ORANGE).colorWeight(0.3).rotate(72).invertRadius.scale(4),
      weight(3) color CREAM colorWeight 0.6 scale 0.25 translate(-1, 1),
      weight(2).color(PURPLE).colorWeight(0.6).polar.translate(1, 0.5),
      weight(50).color(PURPLE).colorWeight(0.0).rotate(72)
    ),
    RuleBasedFractal("polar6",
      weight(4).color(ORANGE).colorWeight(0.3).invertRadius.scale(1).translate(-0.25, -2.0),
      weight(1) color RED colorWeight 0.6 scale -0.25 translate(-4, 0),
      weight(2).color(PURPLE).colorWeight(0.5).polar.scale(2, 4.0).translate(1, 0),
      weight(19).color(CREAM).colorWeight(0.1).inPolarSpace(
        (pv: PolarVector) => PolarVector((pv.r + 1) / 2, pv.t + 1)
      )
    ),
    RuleBasedFractal("polar6",
      weight(4).color(ORANGE).colorWeight(0.3).invertRadius.scale(1).translate(-0.25, -2.0),
      weight(1) color RED colorWeight 0.6 scale -0.25 translate(-4, 0),
      weight(2).color(PURPLE).colorWeight(0.5).polar.scale(2, 4.0).translate(1, 0),
      weight(19).color(CREAM).colorWeight(0.1).inPolarSpace(
        (pv: PolarVector) => PolarVector((pv.r + 1) / 2, pv.t + 1)
      )
    ),
    Spongy,
    Starkle,
    SquaresVille,
    HalfSerp
  )

  val items = examples.map(ex => (ex.name, ex)).toMap
}
