package aoc2015

fun main() = Day20().run()

class Day20 : Day(20) {
    override fun compute(input: List<String>): Pair<Any?, Any?> {
        val target = input[0].toInt()
        val presents1 = IntArray(target) { 0 }
        val presents2 = IntArray(target) { 0 }
        for (i in presents1.indices) {
            var count = 0
            for (j in (i until target / 10).step(i + 1)) {
                presents1[j] += i * 10
                if(count != 50) {
                    presents2[j] += i * 11
                    count++
                }
            }
        }
        return presents1.indexOfFirst { it >= target } + 1 to presents2.indexOfFirst { it >= target } + 1
    }
}