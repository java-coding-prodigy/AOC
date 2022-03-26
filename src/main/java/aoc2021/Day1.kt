package aoc2021

fun main() = Day1().run()


class Day1 : Day(1) {
    override fun part1(input: List<String>) : Any{
        var count = 0
        for (i in 1 until input.size) {
            if (input[i].toInt() > input[i - 1].toInt())
                count++
        }
        return count
    }

    override fun part2(input: List<String>) : Any{
        var count = 0
        for (i in 0 until input.size - 3) {
            if (input[i].toInt() < input[i + 3].toInt())
                count++
        }
        return count
    }
}

