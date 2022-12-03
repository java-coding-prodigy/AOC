package aoc2015

fun main() = Day13().run()

class Day13 : Day(13) {
    override fun compute(input: List<String>): Pair<Any?, Any?> {
        val neighbourResult = input.associate {
            val split = it.split(' ')
            val a = split[0]
            val b = split[10].dropLast(1)
            val p = split[3].toInt() * (if (split[2][0] == 'l') -1 else 1)
            (a to b) to p
        }
        val people = neighbourResult.keys.map(Pair<String, String>::first).distinct()

        return optimalHappiness(people, neighbourResult) to optimalHappiness(people + "You", neighbourResult)
    }

    private fun optimalHappiness(
        people: List<String>,
        neighbourResult: Map<Pair<String, String>, Int>
    ) = people.permutations().map { it + it.first() }
        .map { it + it.first() }.maxOfOrNull {
            (it.zipWithNext() + it.asReversed().zipWithNext()).mapNotNull(neighbourResult::get)
                .sum()
        }

    private fun List<String>.permutations(): Set<List<String>> {
        if (isEmpty()) return setOf(emptyList())

        val result: MutableSet<List<String>> = mutableSetOf()
        for (i in this) {
            (this - i).permutations().forEach { item ->
                result.add(item + i)
            }
        }
        return result
    }
}