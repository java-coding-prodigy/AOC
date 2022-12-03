package aoc2022

import scala.collection.mutable.ListBuffer
object Day1 extends Day(1) {
  def main(args: Array[String]): Unit = Day1.run()

  val meals = new ListBuffer[Int]()

  val currentElf = new ListBuffer[Int]()
  input.foreach(line => if (line.isEmpty) {
    meals.addOne(currentElf.sum)
    currentElf.clear()
  } else currentElf.addOne(line.toInt))


  override def part1(): Any = meals.max

  override def part2(): Any = meals.sorted.takeRightInPlace(3).sum

}
