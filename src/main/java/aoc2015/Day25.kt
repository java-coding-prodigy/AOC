package aoc2015

fun main() = Day25().run()

class Day25 : Day(25) {
    override fun compute(input: List<String>): Pair<Any?, Any?> {
        val (m, n) = input[0].split(' ', ',', '.').mapNotNull(String::toIntOrNull)
        val count = n * (n + 1) / 2 + (m - 1) * (m - 2) / 2 + n * (m - 1)
        return generateSequence(20151125L) { it * 252533 % 33554393 }.drop(
            count - 1
        ).first() to null
    }
}