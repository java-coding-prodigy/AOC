package aoc2022

import scala.collection.mutable
import scala.util.control.NonLocalReturns.{returning, throwReturn}

object Day13 extends Day(13) {

  def main(args: Array[String]): Unit = run()

  val packets: Seq[Packet] = input.filter(_.nonEmpty).map(line => parsePacket(line.drop(1).dropRight(1)))


  class Packet(val elements: List[Packet | Int]) extends Comparable[Packet | Int] {

    def this(elements: Packet | Int) = this(List(elements))

    def compareTo(other: Packet | Int): Int = {
      other match {
        case packet: Packet =>
          returning {
            for (i <- elements.indices) {
              if (i >= packet.elements.size)
                throwReturn(1)
              val left = elements(i)
              val right = packet.elements(i)
              val comparison = if (left.isInstanceOf[Int] && !right.isInstanceOf[Int]) Packet(List(left)).compareTo(right)
              else left.asInstanceOf[Comparable[Packet | Int]].compareTo(right)
              if (comparison != 0)
                throwReturn(comparison)
            }
            elements.size.compareTo(packet.elements.size )
          }
        case number: Int => compareTo(Packet(List(number)))
      }
    }
  }

  def parsePacket(line: String): Packet = {
    val elements = mutable.ListBuffer.empty[Packet | Int]
    val openBrackets = mutable.Stack.empty[Int]
    val number = new mutable.StringBuilder()
    for ((ch: Char, i: Int) <- line.zipWithIndex) {
      ch match {
        case '[' => openBrackets.push(i)
        case ']' => if (openBrackets.size == 1) {
          elements.addOne(parsePacket(line.slice(openBrackets.top + 1, i)))
        }
          openBrackets.pop()
        case ',' => if (number.nonEmpty) {
          elements.addOne(number.toString().toInt)
          number.clear()
        }
        case _ =>
          assert(ch.isDigit, s"$ch does not match any of the expected characters")
          if (openBrackets.isEmpty) number.append(ch)
      }
    }
    if (number.nonEmpty)
      elements.addOne(number.toString().toInt)
    Packet(elements.toList)
  }

  override def part1(): Any = {
    val pairs = packets.indices.filter(_ % 2 == 0).map(idx => (packets(idx), packets(idx + 1)))
    pairs.zipWithIndex.filter((pair: (Packet, Packet), _: Int) => pair._1.compareTo(pair._2) == -1).map(_._2 + 1).sum
  }

  override def part2(): Any = {
    val div1 = Packet(Packet(2))
    val div2 = Packet(Packet(6))
    val sorted = (packets :+ div1 :+ div2).sorted
    (sorted.indexOf(div1) + 1) * (sorted.indexOf(div2) + 1)
  }
}
