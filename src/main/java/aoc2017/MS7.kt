package aoc2017

fun main() = MS7().run()

class MS7 : MS(7) {
    override fun part1(input: List<String>): Any? = root(parse(input))

    override fun part2(input: List<String>): Any? {
        val map = parse(input)
        val root = root(map)
        val weights = weights(map, root)
        map.forEach { (_, pair) ->
            val (_, children) = pair
            val occ =
                children.groupBy { weights[it]!! }.mapValues { it.value.size }
            if (occ.size > 1) {
                val changedWt = occ.filter { (_, v) -> v == 1 }.keys.first()
                val changed = weights.filter { (_, v) -> v == changedWt }.keys.first()
                if (map[changed]!!.second.mapNotNull(weights::get).distinct().size == 1) {
                    val unchangedWt = occ.filter { (_, v) -> v != 1 }.keys.first()
                    val unchanged = weights.filter { (_, v) -> v == unchangedWt }.keys.first()
                    return (map[changed]!!.first - (weights[changed]!! - weights[unchanged]!!))
                }
            }
        }
        TODO("Not yet implemented")
    }

    private fun root(map: Map<String, Pair<Int, List<String>>>) =
        map.keys.filter { map.values.none { (_, children) -> children.contains(it) } }[0]

    private fun weights(map: Map<String, Pair<Int, List<String>>>, root: String): Map<String, Int> {
        val weights = mutableMapOf<String, Int>()
        fun weight(program: String): Int =
            (map[program]!!.first + map[program]!!.second.sumOf(::weight)).also {
                weights[program] = it
            }
        weight(root)
        return weights
    }


    fun parse(input: List<String>): Map<String, Pair<Int, List<String>>> =
        input.associate {
            val (before, after) = it.split(") -> ", ")")
            val (name, wt) = before.split(" (")
            val children = after.split(", ").filter(String::isNotBlank)
            Pair(name, Pair(wt.toInt(), children))
        }

}
