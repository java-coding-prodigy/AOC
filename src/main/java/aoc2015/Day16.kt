package aoc2015

fun main() = Day16().run()

class Day16 : Day(16) {

    override fun compute(input: List<String>): Pair<Any?, Any?> {
        val actualSue = getDetails(
            """"children: 3
                cats: 7
                samoyeds: 2
                pomeranians: 3
                akitas: 0
                vizslas: 0
                goldfish: 5
                trees: 3
                cars: 2
                perfumes: 1""".replace("\n", ", ")
        )
        val sues = input.map {
            getDetails(it.substringAfter(": "))
        }.withIndex()
        return sues
            .find { (i, sue) -> sue.all { (k, v) -> actualSue[k] == v } }?.index?.inc() to sues.find { (i, sue) ->
            sue.all { (k, v) ->
                when (k) {
                    "cats", "trees" -> actualSue[k]!! < v
                    "pomeranians", "goldfish" -> actualSue[k]!! > v
                    else -> actualSue[k] == v
                }
            }
        }?.index?.inc()
    }


    private

    fun getDetails(line: String) =
        line.split(", ").associate {
            val (property, value) = it.trim().split(": ")
            property to value.toInt()
        }
}