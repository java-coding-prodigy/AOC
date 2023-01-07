package aoc2022

object Day16 extends Day(16) {

  val valves: Map[String, (Int, List[String])] = input.map(line => {
    (line.substring(6, 8), (line.dropWhile(!_.isDigit).takeWhile(';'.ne).toInt, line.substring(8).dropWhile(!_.isUpper).split(", ").toList))
  }).toMap

  println(valves.mkString("\n"))

  def main(args: Array[String]): Unit = run()

  def dfs(minutes: Int, current: String, valveClosed: Boolean, openValves: Vector[String]): Int = {
    //println(path)
    if (minutes == 0) {
      0
    } else {
      val minutesLeft = minutes - 1
      val (flowRate, tunnels) = valves(current)
      tunnels.map(valve => dfs(minutesLeft, valve, !openValves.contains(valve), openValves))
        .appended(if (valveClosed && flowRate > 0) flowRate * minutesLeft + dfs(minutesLeft, current, false, openValves :+ current) else 0).max
    }
  }

  override def part1(): Any = dfs(31, "AA", false, Vector("AA"))

  override def part2(): Any = ???
}
