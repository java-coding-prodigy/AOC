package aoc2021

fun main() {
    Day3().run()
}

class Day3 : Day(3) {
    override fun part1(input: List<String>): Any {
        var gamma = ""
        var epsilon = ""
        for (i in input[0].indices) {
            var ones = 0
            for (line in input) {
                if (line[i] == '1')
                    ones++
            }
            gamma += if (ones > input.size / 2) "1" else "0"
            epsilon += if (ones > input.size / 2) "0" else "1"
        }
        return gamma.toInt(2) * epsilon.toInt(2)
    }

    override fun part2(input: List<String>): Any {
        return getOxygen(input) * getCO2(input)
    }

    private fun getOxygen(input: List<String>): Int {
        var list: List<String> = input
        for (i in 0 until input[0].length) {
            var ones = 0
            var zeroes = 0
            for (line in list) {
                if (line[i] == '1')
                    ones++
                else
                    zeroes++
            }
            if (list.size == 1)
                break
            list = list.filter { it[i] == (if (ones >= zeroes) '1' else '0') }
        }
        return list[0].toInt(2)
    }

    private fun getCO2(input: List<String>): Int {
        var list: List<String> = input
        for (i in 0 until input[0].length) {
            var ones = 0
            var zeroes = 0
            for (line in list) {
                if (line[i] == '1')
                    ones++
                else
                    zeroes++
            }
            if (list.size == 1)
                break
            list = list.filter { it[i] == (if (ones >= zeroes) '0' else '1') }
        }
        return list[0].toInt(2)
    }
}
