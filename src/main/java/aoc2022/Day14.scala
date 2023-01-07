package aoc2022

import scala.collection.mutable
import scala.util.control.NonLocalReturns.{returning, throwReturn}

object Day14 extends Day(14) {

  def main(args: Array[String]): Unit = run()

  val reservoir: mutable.Set[Point] = mutable.HashSet.empty[Point]

  input.foreach(line => {
    val points = line.split(" -> ").map(Point.fromString)
    points.zip(points.tail).foreach((start: Point, end: Point) => {
      reservoir.addAll(if (start.x == end.x) {
        (if (start.y > end.y) end.y to start.y else start.y to end.y).map(y => Point.of(start.x, y))
      } else {
        (if (start.x > end.x) end.x to start.x else start.x to end.x).map(Point.of(_, start.y))
      })
    })
  }
  )

  val rocks: mutable.Set[Point] = reservoir.clone()
  val sandSource: Point = Point.of(500, 0)
  val maxY: Int = reservoir.map(_._2).max

  def dropSand(maxY: Int): Boolean = {
    if (reservoir.contains(sandSource)) {
      return false
    }
    var sand = sandSource
    returning {
      while (true) {
        if (sand.y > maxY) {
          throwReturn(false)
        }
        val down = sand.up
        if (reservoir.contains(down)) {
          if (reservoir.contains(down.left)) {
            if (reservoir.contains(down.right)) {
              reservoir.add(sand)
              throwReturn(true)
            } else {
              sand = down.right
            }
          } else {
            sand = down.left
          }
        } else {
          sand = down
        }
      }
      throw new AssertionError("How did the control get here?")
    }
  }

  override def part1(): Any = {
    var count = 0
    while (dropSand(maxY)) {
      count = count + 1
    }
    count
  }

  override def part2(): Any = {
    val bottom = maxY + 2
    reservoir.filterInPlace(rocks.contains)
    val xS = reservoir.map(_.x)
    val min = xS.min
    val max = xS.max
    for (x <- min - 200 to max + 200) {
      reservoir.add(Point.of(x, bottom))
    }
    var count = 0
    while (dropSand(bottom)) {
      count = count + 1
    }
    count
  }
}
