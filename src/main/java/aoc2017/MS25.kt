package aoc2017

fun main() = MS25().part1()

class MS25 : MS(25) {
    override fun part1(input: List<String>): Any? {
        data class State(
            val name: Char,
            val valueMapping: (Boolean) -> Boolean,
            val dirMapping: (Int, Boolean) -> Int,
            val moveTo: (Boolean) -> Char
        )

        val states = mutableMapOf<Char, State>()
        val tape = sortedMapOf<Int, Boolean>(Comparator.naturalOrder())
        val checkSum = input[1].substringBeforeLast(' ').substringAfterLast(' ').toInt()
        for (i in 3 until input.size step 10) {
            val name = input[i].let { it[it.length - 2] }
            val write0 = input[i + 2].let { it[it.length - 2] } == '1'
            val move0 = input[i + 3].let { it[it.length - 3] } == 'h'
            val moveTo0 = input[i + 4].let { it[it.length - 2] }
            val write1 = input[i + 6].let { it[it.length - 2] } == '1'
            val move1 = input[i + 7].let { it[it.length - 3] } == 'h'
            val moveTo1 = input[i + 8].let { it[it.length - 2] }
            states[name] = State(name,
                { if (it) write1 else write0 },
<<<<<<< HEAD
                { idx, b -> if (if (b) move1 else move0) idx.inc() else idx.dec() },
=======
                { x, b -> if (if (b) move1 else move0) x.inc() else x.dec() },
>>>>>>> b71aec57a2593973d9835d3558145f9d991e1d9e
                { if (it) moveTo1 else moveTo0 })
        }
        var state = states[input[0].let { it[it.length - 2] }]!!
        var i = 0
        //(-3..2).forEach{ tape[it] = false}
        repeat(checkSum) {
            state.apply {
                val bool = tape[i] ?: false
                tape[i] = valueMapping(bool)
                i = dirMapping(i, bool)
                state = states[moveTo(bool)]!!
            }
            //println(tape.values.joinToString(" ") { if (it) "1" else "0" })
        }

        return tape.count(Map.Entry<Int, Boolean>::value)
    }

    override fun part2(input: List<String>): Any? {
        TODO("Not yet implemented")
    }
}
