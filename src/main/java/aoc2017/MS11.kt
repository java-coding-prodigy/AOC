package aoc2017

import java.lang.Integer.max
import kotlin.math.abs

fun main() = MS11().run()

class MS11 : MS(11) {


    override fun part1(input: List<String>): Any? {
        var q = 0
        var r = 0
        input[0].split(',').forEach {
            val pair = move(q, r, it)
            q = pair.first
            r = pair.second
        }
        return dist(q, r)
    }

    private fun move(q: Int, r: Int, dir: String): Pair<Int, Int> {
        return when (dir) {
            "n" -> {
                Pair(q, r - 1)
            }
            "ne" -> {
                Pair(q + 1, r - 1)
            }
            "se" -> {
                Pair(q + 1, r)
            }
            "s" -> {
                Pair(q, r + 1)
            }
            "sw" -> {
                Pair(q - 1, r + 1)
            }
            "nw" -> {
                Pair(q - 1, r)
            }
            else -> throw IllegalStateException()
        }
    }

    override fun part2(input: List<String>): Any? {
        var q = 0
        var r = 0
        var max = 0
        input[0].split(',').forEach {
            val pair = move(q, r, it)
            q = pair.first
            r = pair.second
            max = max(max,dist(q,r))
        }
        return max
    }

    private fun dist(q: Int, r: Int) = (abs(q) + abs(r) + abs(q + r)) / 2
}
