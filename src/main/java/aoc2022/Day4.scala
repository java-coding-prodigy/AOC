package aoc2022

object Day4 extends Day(4) {

  def main(args: Array[String]): Unit = super.run()


  val cleaningArea: Seq[(Int, Int, Int, Int)] = input.map(line =>
    val list = line.split(',').flatMap(_.split('-')).map(_.toInt).take(4)
    (list(0), list(1), list(2), list(3))
  )
  override def part1(): Any = cleaningArea.count((x1, y1, x2, y2) => (x1 <= x2 && y1 >= y2) || (x2 <= x1 && y2 >= y1))

  override def part2(): Any = cleaningArea.count((x1, y1, x2, y2) => (y1 >= x2 && x2 >= x1) || (y2 >= x1 && x1 >= x2))

}
