package aoc2022


import scala.collection.mutable
import scala.util.control.Breaks.{break, breakable}

object Day17 extends Day(17) {

  private val rockTypes: List[Char] = List('-', '+', '⅃', 'I', '☐')
  private val jet: String = input.head
  private val chamber = mutable.HashSet.empty[Point]

  def main(args: Array[String]): Unit = run()

  override def part1(): Any = {
    var jetIdx = 0
    for (i <- 0 until 2022) {
      jetIdx = addRock(rockTypes(i % rockTypes.size), jetIdx)._1
    }
    chamberHeight()
  }

  override def part2(): Any = {
    chamber.clear()
    val hashes = mutable.HashMap.empty[(Int, Int, mutable.HashSet[Point]), (Int, Int)]
    var bottomHeight = -1
    var cycleStartIdx = -1
    var i = 0
    var jetIdx = 0
    var last100Rows = mutable.HashSet.empty[Point]
    breakable {
      while (true) {
        val hash = (i % rockTypes.size, jetIdx % jet.length, last100Rows)
        //println(i)
        println(hash._3.map(_.y).toList.sorted)
        if (hashes.contains(hash) && hash._1 == 0) {
          val hashValue = hashes(hash)
          cycleStartIdx = hashValue._1
          bottomHeight = hashValue._2
          println("found cycle ")
          break
        }
        else {
          val data = addRock(rockTypes(hash._1), jet(hash._2))
          jetIdx = data._1
          last100Rows = data._2
          hashes(hash) = (i, chamberHeight())
        }
        i += 1
      }
    }
    //i += 1
    val repeatingHeight = chamberHeight() - bottomHeight
    val cycleSize = i - cycleStartIdx
    val remainingDrops = 1_000_000_000_000L - i
    val cycleCount = remainingDrops / cycleSize
    val end = (remainingDrops % cycleSize).toInt
    println("Bottom Height " + bottomHeight)
    println("Cycle Height " + repeatingHeight)
    println("Cycle Size " + cycleSize)
    println("Remaining Drops " + remainingDrops)
    println("Cycle Count " + cycleCount)
    println("End Drops Left " + end)
    for (i <- 0 until end) {
      jetIdx = addRock(rockTypes(i % rockTypes.size), jetIdx)._1
    }
    val topAndBottomHeight = chamberHeight() - repeatingHeight
    return topAndBottomHeight + repeatingHeight * cycleCount
  }

  def addRock(rockType: Char, startJetIdx: Int): (Int, mutable.HashSet[Point]) = {

    val offset = Point.of(2, chamberHeight() + 3)
    var rock: Seq[Point] = getRock(rockType).map(offset.+)
    var jetIdx = startJetIdx
    breakable {
      while (true) {
        jet(jetIdx % jet.length) match {
          case '>' =>
            if (rock.forall((p: Point) => !chamber.contains(p.right) && p.x + 1 < 7))
              rock = rock.map(_.right)
          case '<' =>
            if (rock.forall((p: Point) => !chamber.contains(p.left) && p.x > 0))
              rock = rock.map(_.left)
        }
        jetIdx += 1
        if (rock.forall((p: Point) => !chamber.contains(p.down) && p.y > 0))
          rock = rock.map(_.down)
        else
          break
      }
    }
    val maxY = chamberHeight()
    chamber.addAll(rock)
    println(s"Max y: $maxY")
    (jetIdx % jet.length, chamber.filter(_.y > maxY - 100).map(p => Point.of(p.x, maxY - p.y)))
  }

  private def getRock(rockType: Char): Seq[Point] = {
    rockType match {
      case '-' => (0 until 4).map(Point.of(_, 0))
      case '+' => Point.of(1, 0) +: (0 until 3).map(Point.of(_, 1)) :+ Point.of(1, 2)
      case '⅃' => (0 until 3).map(Point.of(2, _)) :+ Point.of(0, 0) :+ Point.of(1, 0)
      case 'I' => (0 until 4).map(Point.of(0, _))
      case '☐' => List(Point.of(0, 0), Point.of(0, 1), Point.of(1, 0), Point.of(1, 1))
    }
  }

  private def chamberHeight(): Int = chamber.map(_.y).maxOption.getOrElse(-1) + 1
}
