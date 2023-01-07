package aoc2022


import scala.collection.mutable
import scala.util.control.NonLocalReturns.{returning, throwReturn}

object Day15 extends Day(15) {

  def main(args: Array[String]): Unit = run()

  val scannersToBeacons: Map[Point, Point] = input.map(line => {
    val points = line.split(':').map(p => Point.fromString(p.dropWhile('x'.ne)))
    (points(0), points(1))
  }).toMap
  val beacons: List[Point] = scannersToBeacons.values.toList

  val distances: Map[Point, Int] = scannersToBeacons.map((scanner: Point, beacon: Point) => (scanner, scanner.manhattanDist(beacon)))
  val minX: Int = distances.map((k: Point, v: Int) => k.x - v).min
  val maxX: Int = distances.map((k: Point, v: Int) => k.x + v).max


  override def part1(): Any = {
    (minX to maxX).map(Point.of(_, 2_000_000)).count(p => !beacons.contains(p) && distances.exists(e => e._1.manhattanDist(p) <= e._2))
  }

  override def part2(): Any = {
    (0 to 4_000_000).map(y => LazyList.iterate(Some(Point.of(0, y)): Option[Point])(nextPoint).takeWhile(_.isDefined).last.filter(_.x <= 4_000_000)).find(_.isDefined).flatten.map(p => p.x * 4_000_000L + p.y)

  }

  private def nextPoint(opt: Option[Point]): Option[Point] = {
    opt.flatMap { point =>
      distances.map((scanner: Point, manhattan: Int) => {
        val distY = scanner.distY(point)
        if (distY > manhattan)
          None
        else {
          val manX = manhattan - distY
          val distX = scanner.distX(point)
          if (distX > manX)
            None
          else
            Some(Point.of(scanner.x + manX + 1, point.y))
        }
      }
      ).find(_.isDefined).flatten
    }
  }

  def rangesByY(scanner: Point, distX: Int, distY: Int): Map[Int, Range] = {
    val ranges = mutable.HashMap.empty[Int, Range]
    val x = scanner.x
    ranges(scanner.y) = (x - distX to x + distX)
    for (yOff <- 1 to distY) {
      val currRange = x - distX + yOff to x + distX - yOff
      ranges(scanner.y - yOff) = currRange
      ranges(scanner.y + yOff) = currRange
    }
    ranges.toMap
  }
}
