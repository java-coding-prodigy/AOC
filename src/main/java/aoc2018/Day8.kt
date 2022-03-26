package aoc2018

fun main() = Day8().run()

class Day8 : Day(8) {
    override fun part1(input: List<String>): Any {
        return Tree(input[0]).metaDataSum()
    }

    override fun part2(input: List<String>): Any {
        return Tree(input[0]).values()
    }


    private class Tree(input: String) {

        val numbers: List<Int>
        var idx = 0

        init {
            numbers = input.split(' ').map(String::toInt)
        }

        fun metaDataSum(): Int {
            var metaDataSum = 0
            val children = numbers[idx++]
            val metaDataCount = numbers[idx++]
            for (i in 0 until children) {
                metaDataSum += metaDataSum()
            }
            for (i in 0 until metaDataCount) {
                metaDataSum += numbers[idx++]
            }
            return metaDataSum
        }

        fun values(): Int {
            val children = numbers[idx++]
            val metaDataCount = numbers[idx++]
            val childValues = mutableMapOf<Int, Int>()
            for (i in 1..children) {
                childValues[i] = values()
            }
            var values = 0
            for (i in 1..metaDataCount) {
                val metaData = numbers[idx++]
                values += if (children == 0) {
                    metaData
                } else {
                    childValues[metaData] ?: 0
                }
            }
            return values
        }
    }
}
