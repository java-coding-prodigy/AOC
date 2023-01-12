package aoc2022

import scala.collection.mutable.ListBuffer
import scala.math.floorMod
import scala.util.control.Breaks.{break, breakable}

object Day22 extends Day(22) {

  val grid: List[Array[Char]] = input.dropRight(2).map(_.toCharArray)
  val path: List[Int | Char] = getElements(input.last)

  def main(args: Array[String]): Unit = run()

  def getElements(path: String): List[Int | Char] = {
    val buffer = ListBuffer.empty[Int | Char]
    val number = new StringBuilder()
    for (c <- path.trim) {
      if (c.isLetter) {
        buffer.addOne(number.toString().toInt)
        number.clear()
        buffer.addOne(c)
      } else if (c.isDigit) {
        number.addOne(c)
      } else {
        throw new IllegalStateException(c + " is not a digit or letter")
      }
    }
    buffer.toList
  }

  override def part1(): Any = {
    var dir = 0
    var x = input.head.indexOf('.')
    var y = 0
    val traversed = scala.collection.mutable.ListBuffer.empty[(Int, Int)]
    for (move <- path) {
      move match {
        case 'R' => dir = floorMod(dir + 1, 4)
        case 'L' => dir = floorMod(dir + 3, 4)
        case steps: Int =>
          breakable {
            for (_ <- 0 until steps) {
              val (prevX, prevY) = (x, y)
              dir match {
                case 0 => x += 1
                case 1 => y += 1
                case 2 => x -= 1
                case 3 => y -= 1
              }
              if (grid.applyOrElse(y, _ => Array.emptyCharArray).applyOrElse(x, _ => ' ') == ' ') {
                dir match {
                  case 0 => x = grid(y).indexWhere(ch => ch != ' ')
                  case 1 => y = grid.indexWhere(row => row.length > x && row(x) != ' ')/*map(_.applyOrElse(x, _ => ' ')).indexWhere(ch => ch != ' ')*/
                  case 2 => x = grid(y).length - 1/*.lastIndexWhere(ch => ch != ' ')*/
                  case 3 => y = grid.lastIndexWhere(_.length > x)/*map(_.applyOrElse(x, _ => ' ')).lastIndexWhere(ch => ch != ' ')*/
                }
              }
              println((x, y))
              if (grid(y)(x) == '#') {
                x = prevX
                y = prevY
                println("stopped")
                break
              }
              /*grid(prevY)(prevX) = dir match {
                case 0 => '>'
                case 1 => 'v'
                case 2 => '<'
                case 3 => '^'
              }*/
            }
          }
          traversed.addOne((x, y))
          //println(grid.map(_.mkString).mkString(s"Dir: $dir x: $x y: $y\n", "\n", "\n"))
      }
    }
    println(s"x: $x y: $y dir:$dir")
    println(traversed.mkString("[",", ","]"))
    println(path)
    println(input)
    1000 * (y + 1) + 4 * (x + 1) + dir
  }

  override def part2(): Any = ???
}
