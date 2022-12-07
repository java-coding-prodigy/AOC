package aoc2022

object Day6 extends Day(6) {

  def main(args: Array[String]): Unit = super.run()

  val dataStream: String = input.head

  private def findMarkerIndex(markerSize: Int) = dataStream.indices.find(i => dataStream.substring(i, i + markerSize).toSet.size == markerSize).get + markerSize

  override def part1(): Any = findMarkerIndex(4)

  override def part2(): Any = findMarkerIndex(14)



}
