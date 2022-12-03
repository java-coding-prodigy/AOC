package aoc2015

import java.lang.IllegalStateException

fun main() = Day8().run()

class Day8 : Day(8) {
    override fun compute(input: List<String>): Pair<Any?, Any?> {
        return input.sumOf {
            var i = 1
            var j = -1
            while (i < it.length - 1) {
                if (it[i++] == '\\') {
                    when (it[i]) {
                        '\"', '\\' -> i++
                        'x' -> i += 3
                        else -> throw IllegalStateException(it[i].toString())
                    }
                }
                j++
            }
            i - j
        } to input.sumOf { s -> s.count('\\'::equals) + s.count('\"'::equals) + 2 }
    }

}