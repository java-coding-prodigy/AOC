package aoc2022

import scala.io.Source
import scala.util.Using

abstract class Day(number: Int) {
  def parse(input: Iterator[String]): Unit

  def part1(): Any

  def part2(): Any

  def run(): Unit = {
    parse(Using(Source.fromFile(s"src/main/resources/2022/Day$number.txt"))(_.getLines()).get)
    println(s"Part 1: ${part1()}")
    println(s"Part 2: ${part2()}")
  }
}