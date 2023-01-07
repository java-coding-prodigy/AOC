package aoc2022

import scala.math.{abs, signum}
import scala.collection.mutable

object Day9 extends Day(9) {

  def main(args: Array[String]): Unit = run()


  def connected(head: (Int, Int), tail: (Int, Int)): Boolean = abs(head._1 - tail._1) <= 1 && abs(head._2 - tail._2) <= 1

  def connect(head: (Int, Int), tail: (Int, Int)): (Int, Int) = {
    (tail._1 + signum(head._1 - tail._1), tail._2 + signum(head._2 - tail._2))
  }

  def findTailMovements(knotsSize: Int): Int = {
    val rope = Array.fill[(Int, Int)](knotsSize)((0, 0))
    val tailVisited = mutable.HashSet.empty[(Int, Int)]
    tailVisited.add((0, 0))
    for (line <- input) {
      val direction = line(0)
      val distance = line.substring(2).toInt
      for (_ <- 0 until distance) {
        val head = rope.head
        rope(0) = direction match {
          case 'R' => (head._1 + 1, head._2)
          case 'L' => (head._1 - 1, head._2)
          case 'U' => (head._1, head._2 + 1)
          case 'D' => (head._1, head._2 - 1)
        }
        for (i <- rope.tail.indices) {
          val parentKnot = rope(i)
          val currentKnot = rope(i + 1)
          if (!connected(parentKnot, currentKnot)) {
            rope(i + 1) = connect(parentKnot, currentKnot)
          }
        }
        tailVisited.add(rope.last)
      }
    }
    tailVisited.size
  }

  override def part1(): Any = findTailMovements(2)

  override def part2(): Any = findTailMovements(10)

}
