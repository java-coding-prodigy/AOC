package aoc2021

fun main() = Day16().part2()

class Day16 : Day(16) {
    override fun part1(input: List<String>): Any {
        val builder = StringBuilder(input[0].toBigInteger(16).toString(2))
        val diff = input[0].length * 4 - builder.length
        for (i in 0 until diff) {
            builder.insert(0, "0")
        }
        return Packets(builder.toString()).versionSum
    }

    class Packets(val input: String, values: MutableList<Long> = ArrayList()) {

        var idx = 0
        var versionSum = 0


        init {
            compute(values)
        }

        private fun compute(values: MutableList<Long> = ArrayList()) {
            val version = input.substring(idx, idx + 3).toInt(2)
            val type = input.substring(idx + 3, idx + 6).toInt(2)
            idx += 6
            versionSum += version


            val subValues: MutableList<Long> = ArrayList()

            if (type == 4) {
                var literal = ""
                var prefix: Int
                do {
                    prefix = input[idx].digitToInt()
                    literal += input.substring(idx + 1, idx + 5)
                    idx += 5
                } while (prefix == 1)
                subValues.add(literal.toLong(2))
            } else {
                if (input[idx++] == '1') {
                    val subCount = input.substring(idx, idx + 11).toInt(2)
                    idx += 11
                    for (i in 0 until subCount) {
                        compute(subValues)
                    }
                } else {

                    val endIndex = idx + 15 + input.substring(idx, idx + 15).toInt(2)
                    idx += 15
                    while (idx < endIndex) {
                        compute(subValues)
                    }
                }

            }
            values.add(when (type) {
                0 -> subValues.sum()
                1 -> subValues.reduce { a, b -> a * b }
                2 -> subValues.minOrNull()!!
                3 -> subValues.maxOrNull()!!
                4 -> subValues[0]
                5 -> if (subValues[0] > subValues[1]) 1L else 0L
                6 -> if (subValues[0] < subValues[1]) 1L else 0L
                7 -> if (subValues[0] == subValues[1]) 1L else 0L
                else -> throw IllegalStateException()
            })
        }
    }

    override fun part2(input: List<String>): Any {
        val builder = StringBuilder(input[0].toBigInteger(16).toString(2))
        val diff = input[0].length * 4 - builder.length
        for (i in 0 until diff) {
            builder.insert(0, "0")
        }
        val values: MutableList<Long> = ArrayList()
        Packets(builder.toString(), values)
        return values[0]
    }
}
