package aoc2022

import java.time.Duration
import scala.io.Source
import scala.util.Using

abstract class Day(number: Int) {

  protected val input: List[String] = Using(Source.fromFile(s"src/main/resources/2022/Day$number.txt"))(_.getLines().toList).get

  def part1(): Any

  def part2(): Any

  def run(): Unit = {
    val start1 = System.nanoTime()
    println(s"Part 1: ${part1()}  Time: ${(System.nanoTime() - start1) / 1000000} ms")
    val start2 = System.nanoTime()
    println(s"Part 2: ${part2()}  Time: ${(System.nanoTime() - start2) / 1000000} ms")
  }
}