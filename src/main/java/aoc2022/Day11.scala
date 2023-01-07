package aoc2022

import scala.collection.mutable

object Day11 extends Day(11) {

  def main(args: Array[String]): Unit = run()

  val monkeys: List[Monkey] = parse()
  val divisorLCM: Int = monkeys.map(_.testNo).product

  class Monkey(val startingItems: Seq[Int], val operation: Int => Long, val testNo: Int, val throwToTrue: Int, val throwToFalse: Int) {

    val items: mutable.ArrayDeque[Int] = mutable.ArrayDeque.from(startingItems)

    var inspectedItems = 0

    def resetItems(): Unit = {
      inspectedItems = 0
      items.clear()
      items.addAll(startingItems)
    }

    def inspectItems(part1: Boolean): Unit = {
      while (items.nonEmpty) {
        inspectedItems += 1
        val worryLevel = items.removeHead()
        val newWorryLevel = (if (part1) operation(worryLevel) / 3 else operation(worryLevel) % divisorLCM).toInt
        monkeys(if (newWorryLevel % testNo == 0) {
          throwToTrue
        } else {
          throwToFalse
        }).items.addOne(newWorryLevel)
      }
    }
  }

  def parse(): List[Monkey] = {
    val monkeys = mutable.ListBuffer.empty[Monkey]
    var input = this.input :+ ""
    while (input.nonEmpty) {
      val (monkeyData, others) = input.splitAt(input.indexOf(""))
      val startingItems = monkeyData(1).dropWhile(!_.isDigit).trim.split(", ").map(_.toInt)
      val operation: Int => Long = getOperation(monkeyData(2))
      val toThrowData = monkeyData.takeRight(3).map(_.dropWhile(!_.isDigit).toInt)
      monkeys.addOne(new Monkey(startingItems, operation, toThrowData.head, toThrowData(1), toThrowData(2)))
      input = others.tail
    }
    monkeys.toList
  }

  private def getOperation(opData: String): Int => Long = {
    val (op, operand) = opData.substring(23).splitAt(1)
    if (operand.equals(" old")) {
      old => old.toLong * old
    }
    else {
      val num = operand.trim.toInt
      op.head match {
        case '+' => _.toLong + num
        case '*' => _.toLong * num
      }
    }
  }

  override def part1(): Any = {
    for (_ <- 0 until 20) {
      monkeys.foreach(_.inspectItems(true))
    }
    monkeys.map(_.inspectedItems).sorted.takeRight(2).product
  }

  override def part2(): Any = {
    monkeys.foreach(_.resetItems())
    for (_ <- 0 until 10_000) {
      monkeys.foreach(_.inspectItems(false))
    }
    monkeys.map(_.inspectedItems.toLong).sorted.takeRight(2).product
  }
}
