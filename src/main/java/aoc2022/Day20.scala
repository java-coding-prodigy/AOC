package aoc2022

import scala.collection.mutable.ListBuffer
import scala.math.floorMod

object Day20 extends Day(20) {

  private val file: List[Long] = input.map(_.toLong)

  def main(args: Array[String]): Unit = run()

  override def part1(): Any = mix(1, 1)

  override def part2(): Any = mix(811589153, 10)

  private def mix(decryptionKey: Int, mixCount: Int) = {
    val numbers = file.map(decryptionKey.*).map(new Wrapper(_))
    var mixed = Vector.from(numbers)
    for (_ <- 0 until mixCount) {
      for (wrapped <- numbers) {
        val oldIdx = mixed.indexOf(wrapped)
        val newIdx = floorMod(oldIdx + wrapped.value, mixed.size - 1).toInt
        val (oldFront, oldBack) = mixed.splitAt(oldIdx)
        mixed = if(oldBack != null) oldFront ++ oldBack.tail else oldFront
        val (newFront, newBack) = mixed.splitAt(newIdx)
        mixed = (newFront :+ wrapped) ++ newBack
      }
    }
    val zeroIdx = mixed.indexWhere(_.value == 0)
    (1 to 3).map(zeroIdx + _ * 1000).map(i => mixed(i % mixed.size).value).tapEach(println).sum
  }

  private class Wrapper(val value: Long) {
    override def toString: String = value.toString

  }

}
