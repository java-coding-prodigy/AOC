package aoc2017

import kotlin.streams.toList

fun main() = MS10().run()

class MS10 : MS(10) {
    override fun part1(input: List<String>): Any {
        val list = (0 until 256).toMutableList()
        var currPos = 0
        var skipSize = 0
        val lengths = input[0].split(",").map(String::toInt)
        lengths.forEach { length ->
            hash(list, length, currPos)
            currPos += (length + skipSize++) % list.size
        }
        return list[0] * list[1]
    }

    override fun part2(input: List<String>): Any {
        val list = (0 until 256).toMutableList()
        val lengths = listOf(input[0].chars().toList(), listOf(17, 31, 73, 47, 23)).flatten()
        var currPos = 0
        var skipSize = 0
        repeat(64) {
            lengths.forEach { length ->
                hash(list, length, currPos)
                currPos += (length + skipSize++) % list.size
            }
        }
        return (0 until list.size / 16).map {
            list.subList(it * 16, (it + 1) * 16).reduce(Int::xor)
        }.joinToString("") { (if (it < 16) "0" else "") + it.toString(16) }
    }

    private fun hash(list: MutableList<Int>, length: Int, currPos: Int) {
        (0 until length / 2).forEach {

            val i = (currPos + it) % list.size
            val j = (length + currPos - it - 1) % list.size
            val temp = list[i]
            list[i] = list[j]
            list[j] = temp
        }
    }


}
