package aoc2018

import kotlin.math.abs
import kotlin.math.max

fun main() = Day23().run()

class Day23 : Day(23) {

    private open class Coord(open val x: Int, open val y: Int, open val z: Int) {
        infix fun manhattamDist(other: Coord) =
            abs(x - other.x) + abs(y - other.y) + abs(z - other.z)
    }

    private data class Nanobot(
        override val x: Int,
        override val y: Int,
        override val z: Int,
        val r: Int
    ) : Coord(x, y, z) {


        infix fun inRange(other: Nanobot) = this manhattamDist other <= r

        override fun toString(): String = "pos=<$x,$y,$z>, r=$r"
    }


    override fun part1(input: List<String>): Any {
        val nanobots = input.map(::parseLine)
        val max = nanobots.maxByOrNull(Nanobot::r)!!
        return nanobots.count(max::inRange)
    }

    override fun part2(input: List<String>): Any {
        val nanobots = input.map(::parseLine)
        val origin = Coord(0, 0, 0)
        val ranges = nanobots.flatMap {
            val distFromOrigin = it manhattamDist origin
            val radius = it.r
            listOf(Pair(max(0, distFromOrigin - radius), 1), Pair(distFromOrigin + radius + 1, -1))
        }.associate { it }.toSortedMap()

        var count = 0
        var maxCount = 0
        var result = 0
        ranges.forEach { (d, e) ->
            count += e
            if (count > maxCount) {
                maxCount = count
                result = d
            }
        }
        return result
    }

    private fun parseLine(it: String): Nanobot {
        val (x, y, z, r) = it.split("[^-\\d+]".toRegex()).filter(String::isNotEmpty)
            .map(String::toInt)

        return Nanobot(x, y, z, r)
    }


}
