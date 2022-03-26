package aoc2018

import java.awt.Point

fun main() = Day3().run()

class Day3 : Day(3) {
    override fun part1(input: List<String>): Any {

        val rectangles = input.map { it.split(" @ ")[1] }.map { line ->
            val (starts, offset) = line.split(": ")
            val (x1, y1) = starts.split(',').map(String::toInt)
            val (o1, o2) = offset.split('x').map(String::toInt)
            Rectangle(x1, y1, x1 + o1 - 1, y1 + o2 - 1)
        }

        return (0 until 1000).flatMap { x -> (0 until 1000).map { y -> Point(x, y) } }.map { p ->
            rectangles.count { r -> r.contains(p) }
        }.count { i -> i > 1 }
    }

    data class Rectangle(val x1: Int, val y1: Int, val x2: Int, val y2: Int) {

        fun contains(p: Point) = contains(p.x, p.y)

        fun contains(x: Int, y: Int) = x in x1..x2 && (y in y1..y2)

    }

    override fun part2(input: List<String>): Any {
        val map = input.map { it.split(" @ ") }.associateBy({ it[0].substring(1).toInt() }) {
            val (starts, offset) = it[1].split(": ")
            val (x1, y1) = starts.split(',').map(String::toInt)
            val (o1, o2) = offset.split('x').map(String::toInt)
            Rectangle(x1, y1, x1 + o1 - 1, y1 + o2 - 1)
        }
        val overlapping = mutableSetOf<Int>()
        val points = (0 until 1000).flatMap { x -> (0 until 1000).map { y -> Point(x, y) } }
        points.forEach { p ->
            val current = map.filter { it.value.contains(p) }
            if (current.size > 1) {
                overlapping.addAll(current.keys)
            }
        }

        return map.keys.filter { id -> !overlapping.contains(id) }[0]
    }
}
