package aoc2022

import scala.collection.mutable

object Day18 extends Day(18) {

  def main(args: Array[String]): Unit = run()

  private val cubes: Set[(Int, Int, Int)] = input.map { line =>
    val xyz = line.split(',').map(_.toInt)
    (xyz.head, xyz(1), xyz.last)
  }.toSet

  private val uncoveredSides: List[(Int, Int, Int)] = cubes.toList.flatMap((x, y, z) => neighbours(x, y, z)).filterNot(cubes.contains)

  def neighbours(x: Int, y: Int, z: Int): Seq[(Int, Int, Int)] = {
    Seq((x + 1, y, z), (x, y + 1, z), (x, y, z + 1), (x - 1, y, z), (x, y - 1, z), (x, y, z - 1))
  }

  private val xMax: Int = cubes.map(_._1).max
  private val yMax: Int = cubes.map(_._2).max
  private val zMax: Int = cubes.map(_._3).max

  private def outside(cube: (Int, Int, Int)): Boolean = {
    val seen = mutable.HashSet(cube)
    val q = mutable.Queue(cube)
    while (q.nonEmpty) {
      val (x, y, z) = q.dequeue()
      if ((x == 0 && y == 0 && z == 0) || x < 0 || y < 0 || z < 0 || x > xMax || y > yMax || z > zMax)
        return true
      q.enqueueAll(neighbours(x, y, z).filterNot(seen).filterNot(cubes.contains).tapEach(seen.add))
    }
    return false
  }

  override def part1(): Any = uncoveredSides.size

  override def part2(): Any = uncoveredSides.count(outside)


}
