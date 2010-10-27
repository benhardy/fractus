package net.aethersanctum.fractus.util

class PointGridFlags(val width:Int, val height:Int) {
  val INT_BITS = 32
  val POS_MASK = (INT_BITS-1)
  val SHIFT = 5                 // how many bit shifts to do to divide by INT_BITS
  val row_length = (width + INT_BITS - 1) >> SHIFT
  val rows:Array[Array[Long]] = new Array(height, width)

  def apply(x:Int, y:Int): Boolean = {
    val pos = x >> SHIFT
    val bitPos = x & POS_MASK
    val mask = 1 << bitPos
    (rows(y)(pos) & mask) != 0
  }

  def update(x:Int, y:Int, flag:Boolean) = {
    val pos = x >> SHIFT
    val bitPos = x & POS_MASK
    val mask = 1 << bitPos
    val oldval = rows(y)(pos)
    val newval = if (flag) {
      oldval | mask
    } else {
      oldval & ~mask
    }
    rows(y)(pos)= newval
    this
  }
}