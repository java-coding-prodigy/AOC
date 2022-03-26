package aoc2021

import java.awt.Point
import kotlin.math.abs

fun main() = Day20().part2()
    /*println(Day20().part1Test("""..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..###..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###.######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#..#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#......#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#.....####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.......##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#

#..#.
#....
##..#
..#..
..###""".split("\n"), 35))*/

open class Day20 : Day(20) {
    override fun part1(input: List<String>): Any {
        val ans = Image(input).next().next().pixels.values.count { it }
        println(ans)
        return ans
    }

    override fun part2(input: List<String>): Any {
        var image = Image(input)
        for (i in 1..50) {
            image = image.next()
            println("$i enhancements done")
        }
        val ans = image.pixels.values.count { it }
        println(ans)
        return ans
    }

    class Image(
        private val algo: List<Boolean>,
        val pixels: MutableMap<Point, Boolean>,
        private val flag: Boolean,
    ) {
        private var minX: Int = 0
        private var maxX: Int = 0
        private var minY: Int = 0
        private var maxY: Int = 0

        init {
            minX = pixels.keys.minOf { it.x } - 1
            maxX = pixels.keys.maxOf { it.x } + 1
            minY = pixels.keys.minOf { it.y } - 1
            maxY = pixels.keys.maxOf { it.y } + 1

            for (i in minX..maxX) {
                pixels[Point(i, minY)] = flag
                pixels[Point(i, maxY)] = flag
                //pixels[Point(i, minY + 1)] = flag
                // pixels[Point(i, maxY - 1)] = flag

            }

            for (i in minY..maxY) {
                pixels[Point(minX, i)] = flag
                pixels[Point(maxX, i)] = flag
                //pixels[Point(minX + 1, i)] = flag
                //pixels[Point(maxX - 1, i)] = flag
            }
            pixels[Point(minX, minY)] = flag
            pixels[Point(minX, maxY)] = flag
            pixels[Point(maxX, minY)] = flag
            pixels[Point(maxX, maxY)] = flag

        }

        constructor(input: List<String>) : this(
            input[0].map { it == '#' },
            input.subList(2, input.size).convert(),
        )

        constructor(algo: List<Boolean>, pixels: MutableMap<Point, Boolean>) : this(algo,
            pixels,
            false)

        override fun toString(): String {
            val list: MutableList<String> = ArrayList()
            /*println(minX)
            println(maxX)
            println(minY)
            println(maxY)*/
            for (i in minY..maxY) {
                var line = ""
                for (j in minX..maxX) {
                    try {
                        line += if (pixels[Point(j, i)]!!) "#" else "."
                    } catch (e: NullPointerException) {
                        println("$j,$i$pixels")
                        throw e
                    }
                }
                list.add(line)
            }
            return list.joinToString("\n", "\n", "\n")
        }

        fun next(): Image {
            val newPixels: MutableMap<Point, Boolean> = HashMap()
            for (entry in pixels.entries) {

                newPixels[entry.key] = algo[getIndex(entry.key)]
            }
            return Image(algo, newPixels, if (algo[0]) !flag else false)
        }

        private fun getIndex(key: Point): Int {
            val x = key.x
            val y = key.y
            val bool = pixels[key]!!
            val booler: (Boolean) -> String = { if (it) "1" else "0" }
            val bin = if (bool) 1 else 0
            val neighbours = getNeighbourValues(key).map(booler)
            val flagBin = if (flag) "1" else "0"
            when (x) {
                minX -> {
                    return when (y) {
                        minY -> {
                            if (flag) 511 - (if (bool) 0 else 1) else (bin)

                        }
                        maxY -> {
                            (flagBin + flagBin + bin + flagBin.repeat(6)).toInt(2)
                        }
                        minY + 1 -> {
                            (flagBin.repeat(5) + neighbours[3] + flagBin.repeat(2) +
                                    neighbours[5]).toInt(2)
                        }
                        maxY - 1 -> {
                            (flagBin.repeat(2) + neighbours[1] + flagBin.repeat(2) + neighbours[3] + flagBin.repeat(
                                3)).toInt(2)
                        }
                        else -> (flagBin.repeat(2) + neighbours[1] + flagBin.repeat(2) + neighbours[3] + flagBin.repeat(
                            2) + neighbours[5]).toInt(2)
                    }
                }
                maxX -> {
                    return when (y) {
                        minY -> flagBin.repeat(6) + neighbours[3] + flagBin.repeat(2)
                        maxY -> neighbours[0] + flagBin.repeat(8)
                        minY + 1 -> flagBin.repeat(3) + neighbours[2] + flagBin.repeat(2) + neighbours[4] + flagBin.repeat(
                            2)
                        maxY - 1 -> neighbours[0] + flagBin.repeat(2) + neighbours[2] + flagBin.repeat(
                            5)
                        else -> neighbours[0] + flagBin.repeat(2) + neighbours[2] + flagBin.repeat(2) + neighbours[4] + flagBin.repeat(
                            2)
                    }.toInt(2)
                }
                else -> {
                    return when (y) {
                        minY -> flagBin.repeat(3) + neighbours.joinToString("")
                        maxY -> neighbours.joinToString("") + flagBin.repeat(3)
                        else -> neighbours.joinToString("")
                    }.toInt(2)
                }
            }
        }

        private fun getNeighbourValues(key: Point): List<Boolean> {

            val neighbours = HashSet(pixels.entries.filter { areNeighbours(it.key, key) })


            return neighbours.stream()
                .sorted { ent1, ent2 ->
                    val v = ent1.key.y.compareTo(ent2.key.y)

                    if (v == 0) {
                        ent1.key.x.compareTo(ent2.key.x)
                    } else {
                        v
                    }
                }.map { it.value }.toList()

        }

        private fun areNeighbours(p1: Point, p2: Point) =
            ((abs(p1.x - p2.x) == 1 && p1.y == p2.y) || (abs(p1.y - p2.y) == 1 && p1.x == p2.x) || (abs(
                p1.x - p2.x) == 1 && abs(p1.y - p2.y) == 1)) || p1 == p2
    }
}

private fun List<String>.convert(): MutableMap<Point, Boolean> {
    val map: MutableMap<Point, Boolean> = HashMap()
    for (i in indices) {
        for (j in this[i].indices)
            map[Point(j, i)] = this[i][j] == '#'
    }
    return map
}
