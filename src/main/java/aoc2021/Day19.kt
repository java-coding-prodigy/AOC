package aoc2021

import java.util.*
import kotlin.math.abs

fun main() {
    Day19().run()
}

class Day19 : Day(19) {
    override fun part1(input: List<String>): Any {
        val scanners =
            ArrayList(input.joinToString("\n").split(Regex("-{3} scanner \\d+ -{3}"))
                .filter(String::isNotEmpty).map(::Scanner))
        val allBeacons = HashSet(scanners.removeFirst().relativeBeacons)
        while (scanners.isNotEmpty()) {
            scanners.removeIf { attemptAlign(allBeacons, it) != null }
        }
        return allBeacons.size
    }

    override fun part2(input: List<String>): Any {
        val scanners =
            ArrayList(input.joinToString("\n").split(Regex("-{3} scanner \\d+ -{3}"))
                .filter(String::isNotEmpty).map(::Scanner))
        val allBeacons = HashSet(scanners.removeFirst().relativeBeacons)
        val points = HashSet<Point>()
        points.add(Point(0, 0, 0))
        while (scanners.isNotEmpty()) {
            scanners.removeIf {
                val point = attemptAlign(allBeacons, it)
                if (point != null)
                    points.add(point)
                point != null
            }
        }
        return points.flatMap { points.map(it::manhattan) }.maxOrNull()!!
    }

    private fun attemptAlign(allBeacons: MutableCollection<Point>, scanner: Scanner): Point? {
        val commonDist =
            allBeacons.pairDist().filter { (_, v) -> scanner.pairDist.containsValue(v) }
                .mapValues { (_, v) ->
                    scanner.pairDist.filter { it.value == v }.map(
                        Map.Entry<Pair<Point, Point>, Point>::key)[0]

                }
        if (commonDist.isEmpty())
            return null
        val (pair1, pair2) = commonDist.entries.random()
        try {
            val absolute = pair1.first
            val relative = pair2.first
            val (mapping, position) =
                rotation.mapValues { absolute - it.value(relative) }.entries
                    .filter { (mapping, position) ->
                        commonDist.all { (k, v) ->
                            val mapped = mapping(k.first - position)
                            mapped == v.first || mapped == v.second
                        }
                    }[0]
            allBeacons.addAll(scanner.relativeBeacons.map { rotation[mapping]!!(it) + position })
            return position

        } catch (e: IndexOutOfBoundsException) {
            val absolute = pair1.first
            val relative = pair2.second
            val (mapping, position) =
                rotation.mapValues { absolute - it.value(relative) }.entries
                    .filter { (mapping, position) ->
                        commonDist.all { (k, v) ->
                            val mapped = mapping(k.first - position)
                            mapped == v.first || mapped == v.second
                        }
                    }[0]
            allBeacons.addAll(scanner.relativeBeacons.map { rotation[mapping]!!(it) + position })
            return position
        }
    }

    private fun Iterable<Point>.pairDist(): Map<Pair<Point, Point>, Point> {
        val pairDist: MutableMap<Pair<Point, Point>, Point> = HashMap()
        for (b1 in this) {
            for (b2 in this) {
                if (!pairDist.contains(Pair(b2, b1)) && b1 != b2)
                    pairDist[Pair(b1, b2)] = (b1 - b2).hash()
            }
        }
        return pairDist
    }


   private class Scanner(val relativeBeacons: List<Point>) {

        val pairDist: MutableMap<Pair<Point, Point>, Point> = HashMap()

        init {
            for (b1 in relativeBeacons) {
                for (b2 in relativeBeacons) {
                    if (!pairDist.contains(Pair(b2, b1)) && b1 != b2)
                        pairDist[Pair(b1, b2)] = (b1 - b2).hash()
                }
            }
        }

        constructor(input: String) : this(input.split("\n").filter(String::isNotEmpty)
            .map(::Point))
    }

    @SuppressWarnings("unused")
    private data class Point(val x: Int, val y: Int, val z: Int) {

        constructor(list: List<Int>) : this(list[0], list[1], list[2])

        constructor(line: String) : this(line.split(',').filter(String::isNotEmpty)
            .map(String::toInt))


        operator fun plus(p: Point) = Point(x + p.x, y + p.y, z + p.z)

        operator fun minus(p: Point) = Point(x - p.x, y - p.y, z - p.z)

        infix fun manhattan(p: Point) = abs(x - p.x) + abs(y - p.y) + abs(z - p.z)

        fun hash() = Point(listOf(x, y, z).map(::abs).sorted())

    }

    private val rotation: Map<(Point) -> Point, (Point) -> Point> = mapOf(
        Pair({ (x, y, z) -> Point(x, y, z) }) { (x, y, z) -> Point(x, y, z) },
        Pair({ (x, y, z) -> Point(y, z, x) }) { (x, y, z) -> Point(z, x, y) },
        Pair({ (x, y, z) -> Point(z, x, y) }) { (x, y, z) -> Point(y, z, x) },
        Pair({ (x, y, z) -> Point(-x, z, y) }) { (x, y, z) -> Point(-x, z, y) },
        Pair({ (x, y, z) -> Point(z, y, -x) }) { (x, y, z) -> Point(-z, y, x) },
        Pair({ (x, y, z) -> Point(y, -x, z) }) { (x, y, z) -> Point(-y, x, z) },
        Pair({ (x, y, z) -> Point(x, z, -y) }) { (x, y, z) -> Point(x, -z, y) },
        Pair({ (x, y, z) -> Point(z, -y, x) }) { (x, y, z) -> Point(z, -y, x) },
        Pair({ (x, y, z) -> Point(-y, x, z) }) { (x, y, z) -> Point(y, -x, z) },
        Pair({ (x, y, z) -> Point(x, -z, y) }) { (x, y, z) -> Point(x, z, -y) },
        Pair({ (x, y, z) -> Point(-z, y, x) }) { (x, y, z) -> Point(z, y, -x) },
        Pair({ (x, y, z) -> Point(y, x, -z) }) { (x, y, z) -> Point(y, x, -z) },
        Pair({ (x, y, z) -> Point(-x, -y, z) }) { (x, y, z) -> Point(-x, -y, z) },
        Pair({ (x, y, z) -> Point(-y, z, -x) }) { (x, y, z) -> Point(-z, -x, y) },
        Pair({ (x, y, z) -> Point(z, -x, -y) }) { (x, y, z) -> Point(-y, -z, x) },
        Pair({ (x, y, z) -> Point(-x, y, -z) }) { (x, y, z) -> Point(-x, y, -z) },
        Pair({ (x, y, z) -> Point(y, -z, -x) }) { (x, y, z) -> Point(-z, x, -y) },
        Pair({ (x, y, z) -> Point(-z, -x, y) }) { (x, y, z) -> Point(-y, z, -x) },
        Pair({ (x, y, z) -> Point(x, -y, -z) }) { (x, y, z) -> Point(x, -y, -z) },
        Pair({ (x, y, z) -> Point(-y, -z, x) }) { (x, y, z) -> Point(z, -x, -y) },
        Pair({ (x, y, z) -> Point(-z, x, -y) }) { (x, y, z) -> Point(y, -z, -x) },
        Pair({ (x, y, z) -> Point(-x, -z, -y) }) { (x, y, z) -> Point(-x, -z, -y) },
        Pair({ (x, y, z) -> Point(-z, -y, -x) }) { (x, y, z) -> Point(-z, -y, -x) },
        Pair({ (x, y, z) -> Point(-y, -x, -z) }) { (x, y, z) -> Point(-y, -x, -z) }
    )


}




