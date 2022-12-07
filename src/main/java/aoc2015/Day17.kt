package aoc2015

fun main() = Day17().run()

class Day17 : Day(17) {
    override fun compute(input: List<String>): Pair<Any?, Any?> {
        val containers = input.map(String::toInt)
        val countsByLength = IntArray(containers.size) { 0 }
        for (mask in 0 until (1).shl(containers.size)) {
            val combination = containers.filterIndexed { i, _ -> mask and (1 shl i) > 0 }
            if (combination.sum() == 150) {
                countsByLength[combination.size]++
            }
        }
        return countsByLength.sum() to countsByLength.find { it != 0 }
    }

    fun combinations(containers: List<Int>): List<List<Int>> {
        return containers.map { container ->
            (combinations(containers - container) + listOf(emptyList())).map { it + container }
                .filter { it.sum() <= 150 }
        }
            .reduceOrNull(List<List<Int>>::plus) ?: emptyList()
    }

}