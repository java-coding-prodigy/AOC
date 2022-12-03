package aoc2015

import java.lang.IllegalStateException
import java.lang.NullPointerException

fun main() = Day7().run()

class Day7 : Day(7) {
    override fun compute(input: List<String>): Pair<Any?, Any?> {
        val wires = mutableMapOf<String, Int>()
        fun getVal(s: String): Int =
            if (s.matches("\\d+".toRegex())) s.toInt() else wires[s]!!

        val ops = mutableMapOf(
            "AND" to Int::and,
            "OR" to Int::or,
            "LSHIFT" to { a, b -> a.shl(b) },
            "RSHIFT" to { a, b -> a.shr(b) }
        )
        val relations =
            input.associate {
                val (op, result) = it.split(" -> ")
                val split = op.split(' ')
                result to {
                    when (split.size) {
                        1 -> getVal(split[0])
                        2 -> 65535 - getVal(split[1]);
                        3 -> ops[split[1]]!!(getVal(split[0]), getVal(split[2]))
                        else -> throw IllegalStateException()
                    }
                }
            }.toMutableMap()
        computeSignals(relations, wires)
        val p1 = wires["a"]!!
        wires.clear()
        relations["b"] = { p1 }
        computeSignals(relations, wires)
        return p1.toUShort() to wires["a"]?.toUShort()
    }

    private fun computeSignals(
        relations: Map<String, () -> Int>,
        wires: MutableMap<String, Int>
    ) {
        while (relations.size != wires.size) {
            relations.forEach { (k, v) ->
                if (!wires.contains(k)) {
                    try {
                        wires[k] = v.invoke()
                    } catch (_: NullPointerException) {

                    }
                }
            }
        }
    }
}