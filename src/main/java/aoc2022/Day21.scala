package aoc2022

import java.math.MathContext
import scala.collection.mutable

object Day21 extends Day(21) {

  private val monkeyOps: Map[String, () => BigDecimal] = input.map(line => (line.take(4), expression(line.drop(6)))).toMap
  private val monkeyAnswers: mutable.Map[String, BigDecimal]
  = mutable.HashMap.empty[String, BigDecimal]

  def main(args: Array[String]): Unit = run()

  override def part1(): Any = {
    findAnswers()
    monkeyAnswers("root").toBigInt
  }

  override def part2(): Any = {
    val equalMonkeys = input.find(_.startsWith("root")).get.drop(6).split(" [+\\-*/] ")
    val left = equalMonkeys(0)
    val right = equalMonkeys(1)
    findAnswers(0)
    val left0 = monkeyAnswers(left)
    val right0 = monkeyAnswers(right)
    findAnswers(1)
    val left1 = monkeyAnswers(left)
    val right1 = monkeyAnswers(right)
    if (left0 == left1) {
      val slope = right1 - right0
      ((left0 - right0) / slope).toBigInt
    } else if(right1 == right0){
      val slope = left1 - left0
      ((right0 - left0) / slope).toBigInt
    }else{
      throw new IllegalStateException()
    }
  }

  private def findAnswers(humn: BigDecimal = monkeyOps("humn").apply()): Unit = {
    monkeyAnswers.clear()
    monkeyAnswers("humn") = humn
    while (monkeyAnswers.size < monkeyOps.size) {
      monkeyOps.view.filterKeys(!monkeyAnswers.contains(_)).foreach((name, exp) => {
        try {
          monkeyAnswers(name) = exp.apply()
        } catch {
          case _: NoSuchElementException =>
          case e: Throwable => throw e
        }
      })
    }
  }

  private def expression(exp: String): () => BigDecimal = {
    if (exp.head.isDigit) {
      () => BigDecimal(exp)
    } else {
      val split = exp.split(' ')
      () => {
        val a = monkeyAnswers(split(0))
        val b = monkeyAnswers(split(2))
        split(1).head match {
          case '+' => a + b
          case '-' => a - b
          case '*' => a * b
          case '/' => a / b
        }
      }
    }
  }
}
