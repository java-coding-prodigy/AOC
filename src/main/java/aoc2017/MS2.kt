package aoc2017

fun main() = MS2().run()

class MS2 : MS(2) {
    override fun part1(input: List<String>): Any = evaluateOnLine(input) {        it.maxOrNull()!! - it.minOrNull()!!
    }

    override fun part2(input: List<String>): Any =
        evaluateOnLine(input) { it.forEach { i -> it.forEach { j -> if (i != j && i % j == 0)
                return@evaluateOnLine i / j
            }
        }
        throw IllegalStateException()
        }

    private fun evaluateOnLine(input: List<String>, mapper: (List<Int>) -> Int) =
        input.sumOf { mapper(it.split("\\s+".toRegex()).map(String::toInt)) }
}
