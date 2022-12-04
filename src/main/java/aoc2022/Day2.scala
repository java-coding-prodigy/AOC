package aoc2022


import scala.language.postfixOps

object Day2 extends Day(2) {
  def main(args: Array[String]): Unit = super.run()

  val guide: List[(Int, Int)] = input.map(line => (line(0) - 'A' + 1, line(2) - 'X' + 1))

  //1 = Rock, 2 = paper, 3 = scissors
  val moves: Seq[Int] = Seq(1, 2, 3, 1, 2)

  override def part1(): Any = {
    guide.map((oppMove, playerMove) =>
      playerMove + 3 * {
        if (moves(moves.indexOf(oppMove) + 1) == playerMove) 2
        else if (playerMove == oppMove) 1
        else 0
      }).sum
  }

  override def part2(): Any = {
    guide.map((oppMove, result) => (result match {
      case 1 => moves(moves.indexOf(oppMove) + 2)
      case 2 => oppMove
      case 3 => moves(moves.indexOf(oppMove) + 1)
    }) + 3 * (result - 1)).sum
  }
}
