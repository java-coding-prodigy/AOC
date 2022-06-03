package aoc2017

fun main() = MS4().run()

class MS4 : MS(4) {
    override fun part1(input: List<String>): Any? =
        input.count { line ->
            val words = line.split(' ')
            words.size == words.distinct().size
        }


    override fun part2(input: List<String>): Any? =
        input.count { line ->
            val words = line.split(' ')
            words.size == words.distinctBy(String::toSet).size
        }
}
