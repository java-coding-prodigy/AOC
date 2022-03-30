package aoc2018

import java.awt.Point
import java.util.*
import java.util.stream.Stream


fun main() = Day17().run()

class Day17 : Day(17) {
    override fun part1(input: List<String>): Any {
        val reservoir =
            input.flatMap { line ->
                val (before, after) = line.split(", ")
                val xBefore = before.first() == 'x'
                val bef = before.substringAfter('=').toInt()
                val (minAf, maxAf) = after.substringAfter('=').split("..").map(String::toInt)
                (minAf..maxAf).map { af -> if (xBefore) Point(bef, af) else Point(af, bef) }
            }.associateWith { '#' }.toMutableMap()
        val minY = reservoir.keys.minOf { it.y }
        val spout = Point(500, 0)
        reservoir[spout] = '|'
        stimulateReservoir(reservoir, spout)

        return reservoir.values.count { it == '~' || it == '|' } - minY
    }
    override fun part2(input: List<String>): Any {
        val reservoir =
            input.flatMap { line ->
                val (before, after) = line.split(", ")
                val xBefore = before.first() == 'x'
                val bef = before.substringAfter('=').toInt()
                val (minAf, maxAf) = after.substringAfter('=').split("..").map(String::toInt)
                (minAf..maxAf).map { af -> if (xBefore) Point(bef, af) else Point(af, bef) }
            }.associateWith { '#' }.toMutableMap()
        val spout = Point(500, 0)
        reservoir[spout] = '|'
        stimulateReservoir(reservoir, spout)
        return reservoir.values.count { it == '~' }
    }
    private fun stimulateReservoir(
        reservoir: MutableMap<Point, Char>,
        spout: Point
    ) {
        val maxY = reservoir.keys.maxOf { it.y }

        val q: Queue<Point> = ArrayDeque()
        q.add(spout)
        while (q.isNotEmpty()) {
            val current = q.poll()
            when (reservoir[current]) {

                null, '#' -> {
                    continue
                }
                '|' -> {
                    val down = current.down
                    val downStatus = reservoir[down] ?: '.'
                    if (downStatus == '.') {
                        reservoir.setSquare(down, '|', maxY, q)
                    } else {
                        if (reservoir.bounded(Stream.iterate(current) { it.left }) && reservoir.bounded(
                                Stream.iterate(current) { it.right })
                        ) {
                            reservoir.setSquare(current, '~', maxY, q)
                        }
                        if (downStatus == '~' || downStatus == '#') {
                            reservoir.setSquare(current.left, '|', maxY, q)
                            reservoir.setSquare(current.right, '|', maxY, q)
                        }
                    }
                }
                '~' -> {
                    reservoir.setSquare(current.left, '~', maxY, q)
                    reservoir.setSquare(current.right, '~', maxY, q)
                }
            }
        }
    }

    private fun MutableMap<Point, Char>.bounded(stream: Stream<Point>): Boolean {
        return stream.map<Boolean?> { p ->
            if (this[p] == '#') {
                return@map true
            }
            val down = this[p.down]
            if (down != '#' && down != '~')
                return@map false
            null
        }.filter { it != null }.findAny().orElseThrow()
    }

    private fun MutableMap<Point, Char>.setSquare(p: Point, v: Char, maxY: Int, q: Queue<Point>) {
        if (p.y > maxY || this[p] == '#') {
            return
        }
        if (this[p] != v) {
            q.addAll(p.neighbours)
            q.add(p)
            this[p] = v
        }
    }

    val Point.neighbours get() = listOf(up, left, right, down)

    private val Point.up get() = Point(x, y - 1)

    private val Point.left get() = Point(x - 1, y)

    private val Point.right get() = Point(x + 1, y)

    private val Point.down get() = Point(x, y + 1)


}
