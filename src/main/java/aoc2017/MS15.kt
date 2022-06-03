package aoc2017

import java.lang.Math.multiplyExact

fun main() = MS15().run()

class MS15 : MS(15) {


    private val genAFactor = 16807
    private val genBFactor = 48271
    private val dividend = Int.MAX_VALUE

    override fun part1(input: List<String>): Any? {
        var genA = input[0].substringAfterLast(' ').toLong()
        var genB = input[1].substringAfterLast(' ').toLong()
        var count = 0
        repeat(40_000_000) {
            genA = multiplyExact(genA, genAFactor) % dividend
            genB = multiplyExact(genB, genBFactor) % dividend
            if ((genA and 0xFFFF) == (genB and 0xFFFF)) {
                count++
            }
        }
        return count
    }

    override fun part2(input: List<String>): Any? {
        var genA = input[0].substringAfterLast(' ').toLong()
        var genB = input[1].substringAfterLast(' ').toLong()
        var count = 0
        repeat(5_000_000) {
            do {
                genA = multiplyExact(genA, genAFactor) % dividend
            } while (genA % 4 != 0L)
            do {
                genB = multiplyExact(genB, genBFactor) % dividend
            } while (genB % 8 != 0L)
            if ((genA and 0xFFFF) == (genB and 0xFFFF)) {
                count++
            }
        }
        return count
    }
}
