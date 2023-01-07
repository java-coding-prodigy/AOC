package aoc2022

import scala.collection.mutable

object Day12 extends Day(12) {

  def main(args: Array[String]): Unit = run()

  def elevation(ch: Char): Char = ch match {
    case 'S' => 'a'
    case 'E' => 'z'
    case _ => ch
  }

  val start: (Int, Int) = input.zipWithIndex.map((line: String, y: Int) => (line.indexOf('S'), y)).find(_._1 != -1).get
  val end: (Int, Int) = input.zipWithIndex.map((line: String, y: Int) => (line.indexOf('E'), y)).find(_._1 != -1).get

  private def smallestDistance(start: (Int, Int)) = {
    val q = mutable.Queue.empty[(Int, Int)]
    val distances = Array.fill(input.length, input.head.length)(Int.MaxValue)
    q.enqueue(start)
    distances(start._2)(start._1) = 0
    while (q.nonEmpty) {
      val (currX, currY) = q.dequeue()
      val steps = distances(currY)(currX)
      val currElevation = elevation(input(currY)(currX))
      q.addAll(List((currX + 1, currY), (currX - 1, currY), (currX, currY + 1), (currX, currY - 1))
        .filter((x: Int, y: Int) => x >= 0 && y >= 0 && y < input.size && x < input(y).length)
        .filter((x: Int, y: Int) => elevation(input(y)(x)) - currElevation <= 1)
        .filter((x: Int, y: Int) => distances(y)(x) > steps + 1)
        .tapEach((x: Int, y: Int) => distances(y)(x) = steps + 1)
      )
    }
    distances(end._2)(end._1)
  }

  override def part1(): Any = smallestDistance(start)

  override def part2(): Any = {
    input.zipWithIndex.flatMap((line: String, y: Int) => line.zipWithIndex.filter(_._1 == 'a').map(_._2).map((_, y))).map(smallestDistance).min
  }
}
