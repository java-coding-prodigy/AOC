package aoc2022

import scala.collection.mutable
import scala.jdk.CollectionConverters.*
import scala.math.{max, min}

object Day23 extends Day(23) {

  private val elfPositions: mutable.Set[Point] = mutable.Set.from(input.zipWithIndex.flatMap((row, y) => row.zipWithIndex.filter(_._1 == '#').map(_._2).map(x => Point.of(x, y))))
  private val neighbourFunctions: mutable.Queue[(Point => List[Point], Point => Point)] = mutable.Queue(
    (p => List(p.down, p.down.right, p.down.left), _.down)
    , (p => List(p.up, p.up.right, p.up.left), _.up),
    (p => List(p.left, p.left.up, p.left.down), _.left),
    (p => List(p.right, p.right.up, p.right.down), _.right),
  )

  def main(args: Array[String]): Unit = run()

  override def part1(): Any = {
    println(elfPositions)
    for (_ <- 0 until 10) {
      changePositions()
    }
    rectangleEmptyTiles()
  }

  private def changePositions(): Boolean = {
    val newPositions = elfPositions.filter(p => neighbourFunctions.flatMap(_._1(p)).exists(elfPositions.contains)).map(p =>
      neighbourFunctions.find(_._1.apply(p).forall(!elfPositions.contains(_))).map(_._2).map((f: Point => Point) => (p, f.apply(p)))
    ).filter(_.isDefined).map(_.get).toMap
    neighbourFunctions.enqueue(neighbourFunctions.dequeue())
    newPositions.filter((oldPos, newPos) => newPositions.forall((k, v) => k == oldPos || v != newPos)).tapEach((oldPos, newPos) =>
      elfPositions.remove(oldPos)
      elfPositions.add(newPos)
    ).nonEmpty

  }

  private def rectangleEmptyTiles(): Int = {
    val (minX, maxX, minY, maxY) = elfPositions.foldLeft((Int.MaxValue, Int.MinValue, Int.MaxValue, Int.MinValue))((tuple, point)
    => (min(tuple._1, point.x), max(tuple._2, point.x), min(tuple._3, point.y), max(tuple._4, point.y))
    )

    (maxX - minX + 1) * (maxY - minY + 1) - elfPositions.size
  }

  override def part2(): Any = {
    var rounds = 11 // first 10 rounds of part 1, current round is round number 11
    while (changePositions()) {
      rounds += 1
    }
    rounds
  }

}
