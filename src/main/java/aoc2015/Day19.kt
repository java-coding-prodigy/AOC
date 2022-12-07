package aoc2015

fun main() = Day19().run()

class Day19 : Day(19) {
    override fun compute(input: List<String>): Pair<Any?, Any?> {
        val mappings = input.dropLast(2).map {
            val (lhs, rhs) = it.split(" => ")
            lhs to rhs
        }
        val medicine = input.last()
        return mappings.flatMap { (k, v) ->
            molecules(
                medicine,
                k,
                v
            )
        }.toSet().size to medicine.count(Char::isUpperCase) - medicine.zipWithNext()
            .count(('R' to 'n')::equals) - medicine.zipWithNext()
            .count(('A' to 'r')::equals) - 2 * medicine.count('Y'::equals) - 1
    }

    private fun molecules(medicine: String, lhs: String, rhs: String) =
        lhs.toRegex().findAll(medicine).map {
            medicine.replaceRange(it.range, rhs)
        }.toList()
}