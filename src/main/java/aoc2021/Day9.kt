package aoc2021

import java.awt.Point

fun main() = Day9().part2()

class Day9 : Day(9) {
    override fun part1(input: List<String>): Any {
        var riskSum: Long = 0
        for (i in input.indices) {
            val line = input[i]
            for (j in line.indices) {
                if ((j == 0 || line[j].digitToInt() < line[j - 1].digitToInt()) && (j == line.length - 1 || line[j].digitToInt() < line[j + 1].digitToInt())
                    && (i == 0 || line[j].digitToInt() < input[i - 1][j].digitToInt()) && (i == input.size - 1 || line[j].digitToInt() < input[i + 1][j].digitToInt())
                ) {
                    riskSum += line[j].digitToInt() + 1L
                }
            }
        }
        return riskSum
    }

    override fun part2(input: List<String>): Any {
        val list = Cells(input).computeBasins()
            .sortedDescending()
        return list[0] * list[1] * list[2]
    }

    class Cells(input: List<String>) {
        private val map: MutableMap<Point, Int> = HashMap()
        private val lowPoints: MutableMap<Point, Int> = HashMap()

        init {
            for (i in input.indices) {
                for (j in input[i].indices) {
                    val line = input[i]
                    val num = line[j].digitToInt()
                    map[Point(j, i)] = num
                    if ((j == 0 || num < line[j - 1].digitToInt()) && (j == line.length - 1 || num < line[j + 1].digitToInt())
                        && (i == 0 || num < input[i - 1][j].digitToInt()) && (i == input.size - 1 || num < input[i + 1][j].digitToInt())
                    ) {
                        lowPoints[Point(j, i)] = num
                    }
                }
            }
        }

        private fun areNeighbors(p1: Point, p2: Point): Boolean =
            (p1.x == p2.x - 1 && p1.y == p2.y) || (p1.x == p2.x + 1 && p1.y == p2.y) || (p1.y == p2.y - 1 && p1.x == p2.x) || (p1.y == p2.y + 1 && p1.x == p2.x)


        fun computeBasins(): List<Int> {
            return lowPoints.entries.map {
                if (iterated.contains(it.key)) 0 else {
                    computeBasin(it)
                }
            }
        }

        private val iterated: MutableSet<Point> = HashSet()

        private fun getHigherNeighbors(p: Point, value: Int): Map<Point, Int> =
            map.entries.filter { areNeighbors(it.key, p) && it.value > value && it.value != 9 }
                .associateBy({ it.key }) { it.value }

        private fun getHigherNeighbors(entry: Map.Entry<Point, Int>) =
            getHigherNeighbors(entry.key, entry.value)

        private fun computeBasin(entry: Map.Entry<Point, Int>): Int {
            val neighbours = getHigherNeighbors(entry)
            iterated.add(entry.key)
            //base case
            if (neighbours.isEmpty()) {
                return 1
            }
            return neighbours.entries.sumOf {
                if (iterated.contains(it.key)) 0
                else computeBasin(it)
            } + 1
        }
    }
}

