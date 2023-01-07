package aoc2015

import org.paukov.combinatorics.CombinatoricsFactory
import org.paukov.combinatorics.ICombinatoricsVector

fun main() = Day24().run()

class Day24 : Day(24) {
    override fun compute(input: List<String>): Pair<Any?, Any?> {
        val weights = CombinatoricsFactory.createVector(input.map(String::toInt))
        return findMinQE(weights, 3) to findMinQE(weights, 4)
    }

    private fun findMinQE(weights: ICombinatoricsVector<Int>, parts: Int): Long {
        val groupSum = weights.sum() / parts
        return (1..weights.size / parts).asSequence().map { i ->
            CombinatoricsFactory.createSimpleCombinationGenerator(weights, i)
                .filter { it.sum() == groupSum }
                .map { it.map(Int::toLong) }
        }.first(List<List<Long>>::isNotEmpty).minOf { it.reduce(Long::times) }
    }
}