package aoc2018

import java.awt.Point

fun main() = Day18().part2()

class Day18 : Day(18) {
    override fun part1(input: List<String>): Any = getResourceValue(input, 10)

    override fun part2(input: List<String>): Any = getResourceValue(input, 1000000000)

    private fun getResourceValue(input: List<String>, minutes: Long): Int {
        var acres = parse(input)
        val indexedHashes = mutableListOf<String>()

        do {
            acres = acres.nextState()
        } while (!indexedHashes.contains(acres.hash) && indexedHashes.add(acres.hash) && indexedHashes.size < minutes)
        
        if (minutes > indexedHashes.size) {

            var i = indexedHashes.indexOf(acres.hash)
            val period = indexedHashes.size - i
            while ((i + 1) % period < (minutes % period).toInt())
                i += 1
            acres = parse(indexedHashes[i].split('\n'))
        }

        return acres.values.count { it == '|' } * acres.values.count { it == '#' }

    }

    private fun parse(input: List<String>): Map<Point, Char> =
        (0 until 50).flatMap { y -> (0 until 50).map { x -> Point(x, y) } }
            .associateWith { input[it.y][it.x] }

    private val Map<Point, Char>.hash
        get() =
            (0 until 50).joinToString("\n") { y ->
                (0 until 50).joinToString("") { x ->
                    this[Point(
                        x,
                        y
                    )]!!.toString()
                }
            }

    private fun Map<Point, Char>.nextState(): Map<Point, Char> = mapValues { (k, v) ->
        when (v) {
            '.' -> if (k.neighbours.count { this[it] == '|' } >= 3) '|' else v
            '|' -> if (k.neighbours.count { this[it] == '#' } >= 3) '#' else v
            '#' -> if (k.neighbours.any { this[it] == '#' } && k.neighbours.any { this[it] == '|' }) v else '.'
            else -> throw IllegalStateException()
        }
    }

    private val Point.neighbours
        get() : Iterable<Point> = setOf(
            Point(x - 1, y - 1),
            Point(x + 1, y - 1),
            Point(x - 1, y + 1),
            Point(x + 1, y + 1),
            Point(x - 1, y),
            Point(x + 1, y),
            Point(x, y - 1),
            Point(x, y + 1),

            )
}


