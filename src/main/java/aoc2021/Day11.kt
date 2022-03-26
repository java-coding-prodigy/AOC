package aoc2021

import java.awt.Point
import kotlin.math.abs

fun main() = Day11().part2()/*println(
    //Day11().areNeighboursTest(Point(2, 0))
    Day11().part1Test(
        """5483143223
2745854711
5264556173
6141336146
6357385478
4167524645
2176841721
6882881134
4846848554
5283751526""".split("\n"), 1656
    )
)*/

class Day11 : Day(11) {
    override fun part1(input: List<String>): Any {
        var oct = Octopuses(input)
        for (i in 1..100) {
            oct = oct.nextState()
            //println("after $i steps there are ${oct.flashes} flashes")
            //println(oct)
        }
        return oct.flashes
    }


    class Octopuses(private val map: Map<Point, Long>, val flashes: Int) {
        constructor(input: List<String>) : this(input.flatMapIndexed { i, line ->
            line.split("").filter(String::isNotEmpty)
                .mapIndexed { j, s -> Pair(Point(j, i), s.toLong()) }
        }.associateBy({ it.first }) { it.second }, 0)


        private fun areNeighbours(p1: Point, p2: Point): Boolean =
            ((abs(p1.x - p2.x) == 1 && p1.y == p2.y) || (abs(p1.y - p2.y) == 1 && p1.x == p2.x) || (abs(
                p1.x - p2.x
            ) == 1 && abs(p1.y - p2.y) == 1)) && p1 != p2


        fun allFlashed(): Boolean = map.values.all { it == 0L }

        fun nextState(): Octopuses {
            val map: MutableMap<Point, Long> =
                HashMap(this.map.entries.associateBy({ it.key }) { it.value + 1L })
            var flashes = 0
            val flashing: MutableSet<Point> = HashSet()
            //println(Octopuses(map,0))
            do {
                val prevFlashes = flashes
                for (entry in map.entries) {
                    if (entry.value >= 10 && !flashing.contains(entry.key)) {
                        flashes++
                        flashing.add(entry.key)
                        map.putAll(map.entries.filter { areNeighbours(it.key, entry.key) }
                            .map { Pair(it.key, it.value + 1) })
                        /*println(
                            "$entry is flashing\n ${
                                map.entries.filter {
                                    areNeighbours(
                                        it.key,
                                        entry.key
                                    )
                                }
                            } have been updated"
                        )*/
                    }

                }
            } while (prevFlashes < flashes)
            /*println(
                "Flashing points: ${
                    flashing.joinToString(
                        "\n",
                        "\n",
                        "\n"
                    ) { "${it.x},${it.y}" }
                }"
            )*/
            for (entry in map.entries) {
                if (flashing.contains(entry.key)) {
                    map[entry.key] = 0
                }/*else{
                    map[entry.key] = map[entry.key]!! + 1
                }*/
            }
            return Octopuses(map, this.flashes + flashes)
        }

        override fun toString(): String {
            val list: MutableList<MutableList<Long>> = mutableListOf(
                mutableListOf(*Array(10, Int::toLong)),
                mutableListOf(*Array(10, Int::toLong)),
                mutableListOf(*Array(10, Int::toLong)),
                mutableListOf(*Array(10, Int::toLong)),
                mutableListOf(*Array(10, Int::toLong)),
                mutableListOf(*Array(10, Int::toLong)),
                mutableListOf(*Array(10, Int::toLong)),
                mutableListOf(*Array(10, Int::toLong)),
                mutableListOf(*Array(10, Int::toLong)),
                mutableListOf(*Array(10, Int::toLong))
            )
            for (entry in map.entries) {
                list[entry.key.y][entry.key.x] = entry.value
            }
            return list.joinToString("\n", "\n") { it.joinToString("") }
        }

    }

    override fun part2(input: List<String>): Any {
        var step = 0
        var oct = Octopuses(input)
        while (!oct.allFlashed()) {
            oct = oct.nextState()
            step++
        }
        return step
    }
}
