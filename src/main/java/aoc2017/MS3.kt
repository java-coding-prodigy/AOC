package aoc2017

import java.awt.Point
import java.util.stream.IntStream
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.sqrt

fun main() = MS3().run()

class MS3 : MS(3) {
    override fun part1(input: List<String>): Any {
        val location = location(input[0].toInt())
        return abs(location.x) + abs(location.y)
    }

    override fun part2(input: List<String>): Any {
        val num = input[0].toInt()
        val grid = mutableMapOf(Pair(Point(0, 0), 1))
        return IntStream.iterate(2) { it + 1 }.map { i ->
            val loc = location(i)
            grid.filter { (p, _) -> abs(p.x - loc.x) <= 1 && abs(p.y - loc.y) <= 1 }
                .map { (_, v) -> v }.sum().also { grid[loc] = it }

        }.filter { it > num }.findFirst().orElseThrow()
    }


    private fun location(n: Int): Point {
        val k = ceil((sqrt(n.toDouble()) - 1) / 2).toInt()
        var t = 2 * k + 1
        var m = t * t
        t--
        if (n >= m - t)
            return Point(k - m + n, -k)
        m -= t
        if (n >= m - t)
            return Point(-k, -k + m - n)
        m -= t
        if (n >= m - t)
            return Point(-k + m - n, k)
        return Point(k, k - m + n + t)
    }
}

