package aoc2018

fun main() = Day12().run()

class Day12 : Day(12) {

    override fun part1(input: List<String>): Any {
        var pots: Iterable<Int> =
            input[0].substringAfter(": ").withIndex().filter { (_, ch) -> ch == '#' }
                .map(IndexedValue<Char>::index)

        val rules: Map<List<Boolean>, Boolean> = input.subList(2, input.size).associateBy(
            { it.substringBefore(" => ").map { ch -> ch == '#' } }
        ) { it.last() == '#' }
        for (i in 1..20) {
            pots = rules.nextGen(pots)
        }


        return pots.sum()
    }

    override fun part2(input: List<String>): Any {
        var pots: Iterable<Int> =
            input[0].substringAfter(": ").withIndex().filter { (_, ch) -> ch == '#' }
                .map(IndexedValue<Char>::index)

        val rules: Map<List<Boolean>, Boolean> = input.subList(2, input.size).associateBy(
            { it.substringBefore(" => ").map { ch -> ch == '#' } }
        ) { it.last() == '#' }
        var prevSum = pots.sum()
        val iterations = 1000
        val diffs = mutableListOf<Int>()
        repeat(iterations) {
            pots = rules.nextGen(pots)
            val currSum = pots.sum()
            if (it >= iterations - 100) {
                diffs.add(currSum - prevSum)
            }
            prevSum = currSum
        }

        return (50_000_000_000L - iterations) * diffs.average().toInt() + prevSum
    }


    private fun Map<List<Boolean>, Boolean>.nextGen(pots: Iterable<Int>): Iterable<Int> {
        val newPots = mutableSetOf<Int>()
        val min = pots.minOrNull()!!
        val max = pots.maxOrNull()!!
        for (number in min - 2..max + 2) {
            if (this[(number - 2..number + 2).map(pots::contains)] == true) {
                newPots.add(number)
            }
        }
        return newPots
    }
}
