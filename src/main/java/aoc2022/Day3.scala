package aoc2022

import scala.collection.mutable.ListBuffer

object Day3 extends Day(3) {
  def main(args: Array[String]): Unit = super.run()

  private def priority(c: Char) = if (c >= 'a') c - 'a' + 1 else c - 'A' + 27

  override def part1(): Any = {
    input.map(line => {
      val (a, b) = line.splitAt(line.length / 2)
      (a.toSet & b.toSet).head
    }).map(priority).sum
  }
  override def part2(): Any = {
    val groups = new ListBuffer[Seq[String]]
    for (i <- Range(0, input.length - 1, 3)) {
      groups.addOne(input.slice(i, i + 3))
    }
    groups.map(_.map(_.toSet)
      .foldLeft(('A' to 'z').toSet)(_ & _).head).map(priority).sum
  }
}
