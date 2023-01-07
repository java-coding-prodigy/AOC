package aoc2022

import scala.annotation.targetName
import scala.math.abs

class Point(val x: Int, val y: Int) extends Product2[Int, Int] {

  def up: Point = Point.of(x, y + 1)

  def down: Point = Point.of(x, y - 1)

  def left: Point = Point.of(x - 1, y)

  def right: Point = Point.of(x + 1, y)

  def _1: Int = x

  def _2: Int = y

  @targetName("plus")
  infix def +(other: Point): Point = Point.of(x + other.x, y + other.y)

  @targetName("minus")
  infix def -(other: Point): Point = Point.of(x - other.x, y - other.y)

  @targetName("times")
  infix def *(scalar: Int): Point = Point.of(x * scalar, y * scalar)

  def manhattanDist(other: Point): Int = distX(other) + distY(other)

  def distX(other: Point): Int = abs(x - other.x)

  def distY(other: Point): Int = abs(y - other.y)

  override def equals(that: Any): Boolean =
    that match {
      case that: Point => that.canEqual(this) &&
        this.x == that.x && this.y == that.y
      case _ => false
    }

  def canEqual(a: Any): Boolean = a.isInstanceOf[Point]

  override def hashCode(): Int = {
    java.util.Objects.hash(x, y)
  }

  override def toString: String = s"($x,$y)"
}

object Point {
  def fromString(s: String): Point = {
    val nums = s.split(',').map(_.replaceAll("[xy]=", "").trim.toInt)
    of(nums.head, nums(1))
  }

  def of(x: Int, y: Int): Point = new Point(x, y)
}