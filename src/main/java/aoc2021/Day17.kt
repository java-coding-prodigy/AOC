package aoc2021

import kotlin.math.max
import kotlin.math.sign

fun main() = Day17().part2()

class Day17 : Day(17) {
    override fun part1(input: List<String>): Any {
        val area = TargetArea(input[0])
        var maxY = Int.MIN_VALUE
        for (x in Int.MIN_VALUE..Int.MAX_VALUE) {
            for (y in Int.MIN_VALUE..Int.MAX_VALUE) {
                val probe = Probe(x, y)
                if (probe.willEnter(area)) {
                    maxY = max(maxY, probe.maxY)
                }
            }
        }
        return maxY
    }

    class Probe(var xV: Int, var yV: Int) {
        private var xPos = 0
        private var yPos = 0
        var maxY = 0

        private fun update() {
            xPos += xV
            yPos += yV
            xV -= sign(xV.toFloat()).toInt()
            yV--
            maxY = max(maxY, yPos)
        }

        fun willEnter(targetArea: TargetArea): Boolean {
            do {
                update()
                if (this `in` targetArea) {
                    return true
                }
            } while (xPos <= targetArea.xRange.endInclusive && yPos >= targetArea.yRange.start)
            return false
        }

        infix fun `in`(targetArea: TargetArea): Boolean =
            xPos in targetArea.xRange && yPos in targetArea.yRange

    }

    class TargetArea(line: String) {
        val xRange: ClosedRange<Int>
        val yRange: ClosedRange<Int>

        init {
            val groups =
                line.substring(15).split(", y=").flatMap { it.split("..") }.map { it.toInt() }
            xRange = groups[0]..groups[1]
            yRange = groups[2]..groups[3]
            println(xRange)
            println(yRange)
        }
    }

    override fun part2(input: List<String>): Any {
        val area = TargetArea(input[0])
        val minX = 1
        val maxX = area.xRange.endInclusive
        val minY = area.yRange.start
        val maxY = -area.yRange.start - 1
        var i = 0L
        for (x in minX..maxX) {
            for (y in minY..maxY) {
                if (Probe(x, y).willEnter(area)) {
                    i++
                }
            }
        }
        return i
    }
}
