package aoc2022

import scala.collection.mutable
import scala.math.abs

object Day10 extends Day(10) {

  def main(args: Array[String]): Unit = run()

  private def computeRegisters: List[Int] = {
    var x = 1
    val xs = mutable.ListBuffer.empty[Int]
    xs.addOne(x)
    for (line <- input) {
      if (!line.startsWith("noop")) {
        xs.addOne(x)
        x += line.substring(5).toInt
      }
      xs.addOne(x)
    }
    xs.toList
  }

  override def part1(): Any = {
    computeRegisters.zipWithIndex.filter((_: Int, idx: Int) => (idx - 20 + 1) % 40 == 0).map((x: Int, idx: Int) => x * (idx + 1)).sum
  }

  override def part2(): Any = {
    val crt = Array.fill(6, 40)(false)
    computeRegisters.zipWithIndex.foreach((x: Int, idx: Int) => {
      if (abs(x - idx % 40) <= 1) {
        crt(idx / 40).update(idx % 40, true)
      }
    })
    crt.map(_.map(if (_) "ππ" else "  ").mkString("")).mkString("\n", "\n", "\n")
  }
}
