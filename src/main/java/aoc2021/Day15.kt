package aoc2021

import java.awt.Point
import java.util.*
import kotlin.math.abs

fun main() = Day15().part2()

open class Day15 : Day(15) {
    override fun part1(input: List<String>): Any {
        println(Chiton(input).search())
        return Chiton(input).search()/*.minOrNull()!!*/
    }

    class Chiton(input: List<String>) {
        val map: MutableMap<Point, Int>
        val start: Point
        val end: Point

        init {
            map = HashMap()
            start = Point(0, 0)
            end = Point(input[0].length - 1, input.size - 1)
            for (i in input.indices) {
                for (j in input[i].indices) {
                    map[Point(j, i)] = input[i][j].digitToInt()
                }
            }
        }


        fun neighbours(p: Point): Iterable<Pair<Point, Int>> {
            val x = p.x
            val y = p.y
            return listOf(Point(x + 1, y),
                Point(x - 1, y),
                Point(x, y + 1),
                Point(x, y - 1)).filter(map::containsKey).map { Pair(it, map[it]!!) }
        }

        fun search(): Int {
            val queue: Queue<Point> = LinkedList()
            queue.add(start)
            val map2: MutableMap<Point, Int> =
                HashMap()
            map.keys.forEach { map2[it] = 1_000_000 }
            map2[start] = 0
            do {
                val currentVertex = queue.poll()
                for (neighbour in neighbours(currentVertex)) {
                    val distance = map2[currentVertex]!! + neighbour.second
                    if (distance < map2[neighbour.first]!!) {
                        map2[neighbour.first] = distance
                        queue.add(neighbour.first)
                    }
                }
                //println("crossed node")
            } while (queue.isNotEmpty())
            return map2[end]!!
        }

        private fun areNeighbours(p1: Point, p2: Point): Boolean =
            ((abs(p2.x - p1.x) == 1 && p1.y == p2.y) || (abs(p2.y - p1.y) == 1 && p1.x == p2.x)) && p1 != p2
    }

    override fun part2(input: List<String>): Any {
        val repeat = repeat(input)
        println("input made")
        return Chiton(repeat).search()
    }

    fun repeat(input: List<String>): List<String> {
        return verticalRepeat(horizontalRepeat(input))
    }

    private fun horizontalRepeat(input: List<String>): List<String> {
        var result: List<String> = ArrayList(input)
        var current: List<String> = ArrayList(result)
        for (i in 1 until 5) {
            current = current.map { line -> line.map { inc(it) }.joinToString("") }
            result = result.mapIndexed { ind, e -> e + current[ind] }
        }
        return result
    }

    private fun verticalRepeat(input: List<String>): List<String> {
        val result: MutableList<String> = ArrayList(input)
        var current: List<String> = ArrayList(result)
        for (i in 1 until 5) {
            current = current.map { line -> line.map { inc(it) }.joinToString("") }
            result.addAll(current)
        }
        return result
    }

    private fun inc(ch: Char): Int = if (ch == '9') 1 else ch.digitToInt() + 1
}
