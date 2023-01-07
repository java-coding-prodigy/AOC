package aoc2022

import scala.math.min

object Day8 extends Day(8) {

  def main(args: Array[String]): Unit = run()

  val trees: Seq[Seq[Int]] = input.map(_.split("").map(_.toInt))

  def treesAround(x: Int, y: Int): List[Seq[Int]] = {
    List(trees.take(y).map(_ (x)).reverse,
      trees.drop(y + 1).map(_ (x)),
      trees(y).take(x).reverse,
      trees(y).drop(x + 1),
    )
  }

  def visible(x: Int, y: Int, height: Int): Boolean = treesAround(x, y).exists(_.forall(_ < height))

  def scenicScore(x: Int, y: Int, tree: Int): Int = {
    treesAround(x, y).map(line => min(line.size, line.takeWhile(_ < tree).size + 1)).product
  }

  override def part1(): Any = {
    trees.zipWithIndex.map((row: Seq[Int], y: Int) => row.zipWithIndex.count((height: Int, x: Int) => visible(x, y, height))).sum
  }

  override def part2(): Any = {
    trees.zipWithIndex.map((row: Seq[Int], y: Int) => row.zipWithIndex.map((height: Int, x: Int) => scenicScore(x, y, height)).max).max
  }
}
