package aoc2022

import scala.collection.mutable

object Day5 extends Day(5) {

  def main(args: Array[String]): Unit = run()

  val (ogStacks, steps) = parse()

  def parse(): (Seq[Seq[Char]], Seq[(Int, Int, Int)]) = {
    val (stacksAsString, steps) = input.splitAt(input.indexOf(""))
    ((0 until stacksAsString.last.last - '0').map(_ * 4 + 1).map(idx => {
      stacksAsString.dropRight(1).filter(_.length > idx).map(_ (idx)).filter(_.isUpper)
    })
    , steps.tail.map(step => {
      val ints = "\\d+".r.findAllMatchIn(step).map(_.matched.toInt)
      (ints.next(), ints.next() - 1, ints.next() - 1)
    })
    )
  }

  override def part1(): Any = {
    val stacks = ogStacks.map(mutable.ArrayDeque.from)
    steps.foreach((amount, from, to) => {
      for (_ <- 0 until amount) {
        stacks(to).insert(0, stacks(from).removeHead(false))
      }
    }
    )
    stacks.filter(_.nonEmpty).map(_.head).mkString
  }

  override def part2(): Any = {
    val stacks = ogStacks.map(mutable.ArrayDeque.from)
    steps.foreach((amount, from, to) => {
      for (i <- 0 until amount) {
        stacks(to).insert(i, stacks(from).removeHead(false))
      }
    })
    stacks.filter(_.nonEmpty).map(_.head).mkString
  }
}
