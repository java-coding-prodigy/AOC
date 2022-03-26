package aoc2018

import java.awt.Point

fun main() = Day10().run()

class Day10 : Day(10) {


    data class Star(var x: Int, var y: Int, val xV: Int, val yV: Int) {


        fun move(count: Int = 1) {
            x += count * xV
            y += count * yV
        }

        fun position() = Point(x, y)

    }

    private val regex =
        "position=<\\s*(-?\\d+),\\s*(-?\\d+)>\\svelocity=<\\s*(-?\\d+),\\s*(-?\\d+)>".toRegex()

    private fun star(input: String): Star {
        val (x, y, xV, yV) = regex.matchEntire(input)!!.groupValues
            .subList(1, 5).map(String::toInt)
        return Star(x, y, xV, yV)
    }


    private fun Collection<Star>.toSky(): CharSequence {
        val positions = this.map(Star::position).toSet()
        val minX = positions.minOf { it.x }
        val maxX = positions.maxOf { it.x }
        val minY = positions.minOf { it.y }
        val maxY = positions.maxOf { it.y }
        val sb = StringBuilder((maxX - minX) * (maxY - minY))
        sb.append('\n')
        for (y in minY..maxY) {
            for (x in minX..maxX) {
                sb.append(if (positions.contains(Point(x, y))) '▌' else ' ')
            }
            sb.append('\n')
        }
        return sb
    }

    private fun getTime(stars: Iterable<Star>): Int {
        return (0 until 20_000).minByOrNull { i ->
            val minX = stars.minOf { (x, _, xV, _) -> x + xV * i }
            val maxX = stars.maxOf { (x, _, xV, _) -> x + xV * i }
            val minY = stars.minOf { (_, y, _, yV) -> y + yV * i }
            val maxY = stars.maxOf { (_, y, _, yV) -> y + yV * i }
            return@minByOrNull maxX - minX + maxY - minY
        }!!
    }

    override fun part1(input: List<String>): Any {
        val stars = input.map(::star)
        val count = getTime(stars)
        stars.forEach { it.move(count) }

        return stars.toSky()
    }

    override fun part2(input: List<String>): Any {
        return getTime(input.map(::star))
    }
}
