package net.aethersanctum.fractus

import java.awt.Color

trait Rule extends (Vector2 => Vector2) {
  def weight: Double
  def transform: Vector2 => Vector2
  def color: Color
  def colorWeight: Double
  def selectNext: Option[(Unit=>Int)]

  override def apply(p:Vector2) = {
    transform(p)
  }
  def apply(p:Vector2, c:Color) : (Vector2, Color) = {
    (transform(p), colorMerge(c, color, colorWeight))
  }
  def apply(c:Color):Color = {
    colorMerge(c, color, colorWeight)
  }
  def colorMerge(from: Color, to:Color, howFar:Double):Color = {
    val r = from.getRed  * (1-howFar) + to.getRed  * howFar
    val g = from.getGreen* (1-howFar) + to.getGreen* howFar
    val b = from.getBlue * (1-howFar) + to.getBlue * howFar
    new Color( r toInt, g toInt, b toInt )
  }
  def weightIsLess(w:Double) = (weight < w)
}

object Rule {
  trait RuleBuilder {
    def weight(w:Double): RuleBuilder
    def transform(t:Transform): RuleBuilder
    def color(c:Color): RuleBuilder
    def colorWeight(w:Double): RuleBuilder
    def color(c:Color, weight:Double): RuleBuilder = {
      color(c).colorWeight(weight)
    }
    def translate(x:Double,y:Double): RuleBuilder
    def scale(x:Double,y:Double): RuleBuilder
    def scale(s:Double): RuleBuilder
    def rotate(a:Double): RuleBuilder
    def polar: RuleBuilder
    def cartesian: RuleBuilder
    def invertRadius: RuleBuilder
    def sine: RuleBuilder
    def also(f: Vector2=>Vector2):RuleBuilder
    def inPolarSpace(f: PolarVector=>PolarVector):RuleBuilder
    def next(picker:(Unit=>Int)): RuleBuilder
    def build:Rule
  }


  private class RuleBuilderImpl extends RuleBuilder {
    var b_weight: Double = 1
    var b_transform: Transform = Transform.none
    var b_color: Color = Color.WHITE
    var b_colorWeight: Double = 1
    var b_allowNext: Option[(Unit=>Int)] = None
    override def weight(w:Double): RuleBuilder = {
      b_weight = w
      this
    }
    override def transform(t:Transform): RuleBuilder = {
      b_transform = t
      this
    }
    override def color(c:Color): RuleBuilder = {
      b_color = c
      this
    }
    override def colorWeight(w:Double): RuleBuilder = {
      b_colorWeight = w
      this
    }
    override def translate(x:Double,y:Double) = {
      b_transform = b_transform.combine(Transform.translate(x,y))
      this
    }
    override def scale(x:Double,y:Double) = {
      b_transform = b_transform.combine(Transform.scale(x,y))
      this
    }
    override def scale(s:Double) = {
      b_transform = b_transform.combine(Transform.scale(s))
      this
    }
    override def rotate(a:Double) = {
      b_transform = b_transform.combine(Transform.rotate(a))
      this
    }
    override def polar = {
      b_transform = b_transform.combine(Transform.polar)
      this
    }
    override def cartesian = {
      b_transform = b_transform.combine(Transform.cartesian)
      this
    }
    override def invertRadius = {
      b_transform = b_transform.combine(Transform.invertRadius)
      this
    }
    override def sine = {
      b_transform = b_transform.combine(Transform.sine)
      this
    }
    override def also(f: Vector2=>Vector2):RuleBuilder =  {
      b_transform = b_transform.combine(new Transform() {
        override def apply(p:Vector2) = f(p)
      })
      this
    }
    override def inPolarSpace(f: PolarVector=>PolarVector):RuleBuilder =  {
      b_transform = b_transform.combine(Transform.inPolarSpace(f))
      this
    }
    override def next(picker: (Unit=>Int)) = {
      b_allowNext = Some(picker)
      this
    }
    override def build: Rule = {
      new Rule() {
        override def weight: Double = { b_weight }
        override def transform: Vector2 => Vector2 = { b_transform }
        override def color: Color = { b_color }
        override def colorWeight: Double = { b_colorWeight }
        override def selectNext: Option[(Unit=>Int)] = { b_allowNext }
      }
    }
  }
  def builder:RuleBuilder = new RuleBuilderImpl
  def rule = builder
  def weight(w:Double):RuleBuilder = builder weight w
  def color(c:Color):RuleBuilder = builder color c
  def transform(t:Transform):RuleBuilder = builder transform t
  implicit def buildRule(rb:RuleBuilder):Rule = rb.build
}
