package aoc2018

import kotlin.math.abs

fun main() = Day5().run()

class Day5 : Day(5) {

    override fun part1(input: List<String>): Any {

        return reactedLength(input[0])
    }

    private fun reactedLength(input: CharSequence): Int {
        val polymer = StringBuilder(input)
        var index = 0
        while (index < polymer.length - 1) {
            val curr = polymer[index]
            val next = polymer[index + 1]
            if (matches(curr, next)) {
                polymer.delete(index, index + 2)
                if (index != 0) {
                    index--
                }
            } else {
                index++
            }
        }
        return polymer.length
    }

    private fun matches(c1: Char, c2: Char) = abs(c1 - c2) == 32

    override fun part2(input: List<String>): Any {
        return ('a'..'z').map { c -> Pair("$c", "${c.uppercaseChar()}") }.minOf { (lower, upper) ->
            var polymer = input[0]
            while (polymer.contains(lower)) {
                polymer = polymer.replace(lower, "")
            }
            while (polymer.contains(upper)) {
                polymer = polymer.replace(upper, "")
            }
            reactedLength(polymer)
        }
    }
}
