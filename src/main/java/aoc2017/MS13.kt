package aoc2017

import java.util.stream.IntStream

fun main() = MS13().run()

class MS13 : MS(13) {

    data class Scanner(
        val depth: Int,
        val range: Int,
        var position: Int = 1,
        var ascending: Boolean = true
    ) {


        val severity get() = depth * range

        constructor(line: String) : this(
            line.substringBefore(':').toInt(),
            line.substringAfter(' ').toInt()
        )

        fun move() {
            if (ascending) {
                position++
                if (position == range)
                    ascending = false
            } else {
                position--
                if (position == 1)
                    ascending = true
            }
        }

    }

    override fun part1(input: List<String>): Any? {
        val ranges = input.associate {
            Pair(
                it.substringBefore(':').toInt(),
                it.substringAfter(' ').toInt()
            )
        }
        return ranges.filter { (depth, range) -> depth % (2 * (range - 1)) == 0 }
            .map { (depth, range) -> depth * range }.sum()
    }


    override fun part2(input: List<String>): Any? {
        val ranges = input.associate {
            Pair(
                it.substringBefore(':').toInt(),
                it.substringAfter(' ').toInt()
            )
        }
        return IntStream.iterate(0, Int::inc)/*.peek { println(it) }*/.filter {
            ranges.none { (depth, range) -> (it + depth) % (2 * (range - 1)) == 0 }
        }.findFirst()
    }


}
