package aoc2018

import java.awt.Point
import kotlin.math.abs

fun main() = Day6().run()

class Day6 : Day(6) {
    override fun part1(input: List<String>): Any {

        val points = parse(input)
        val finite =
            points.filter { p -> points.any { it.x > p.x } && points.any { it.x < p.x } && points.any { it.y > p.y } && points.any { it.y < p.y } }
                .toSet()
        val counts = getCounts(points)
        return finite.maxOf { counts.getOrDefault(it, 0) }
    }

    override fun part2(input: List<String>): Any {

        val points = parse(input)
        val distanceLimit = 10000
        val (minX, maxX, minY, maxY) = minsAndMaxes(points)

        return (minX..maxX).flatMap { x ->
            (minY..maxY).map { y -> Point(x, y) }
        }.count { p ->
            points.sumOf { manhattam(it, p) } < distanceLimit
        }
    }

    private fun manhattam(p1: Point, p2: Point) = abs(p1.x - p2.x) + abs(p1.y - p2.y)

    private fun minsAndMaxes(points: Collection<Point>) = listOf(points.minOf { it.x },
        points.maxOf { it.x },
        points.minOf { it.y },
        points.maxOf { it.y })

    private fun getCounts(points: Collection<Point>): Map<Point, Int> {
        val (minX, maxX, minY, maxY) = minsAndMaxes(points)
        val counts = mutableMapOf<Point, Int>()
        for (x in minX..maxX) {
            for (y in minY..maxY) {
                val p = Point(x, y)


                val closest = points.minByOrNull { manhattam(p, it) }!!
                val dist = manhattam(p, closest)
                if (points.count { manhattam(p, it) == dist } == 1) counts.merge(
                    closest, 1, Int::plus
                )
            }
        }

        return counts
    }

    private fun parse(input: List<String>): Collection<Point> {
        return input.map {
            Point(
                it.substringBefore(',').toInt(), it.substringAfter(' ').toInt()
            )
        }.toSet()
    }

}
