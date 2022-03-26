package aoc2021


fun main() = Day2().run()
class Day2 : Day(2) {
    override fun part1(input: List<String>) : Any {
        var x = 0
        var y = 0
        for(line : String in input){
            val groups = line.split("\\s")
            val n = groups[1].toInt()
            when(groups[0]){
                "forward" -> x += n
                "up" -> y -= n
                "down" -> y += n
                else -> throw IllegalArgumentException()
            }
        }
        return x * y
    }

    override fun part2(input: List<String>) : Any{
        var x = 0
        var y = 0
        var aim = 0
        for(line in input){
            val groups = line.split("\\s")
            val n = groups[1].toInt()
            when(groups[0]){
                "forward" -> {x += n
                    y += aim * n}
                "up" -> aim -= n
                "down" -> aim += n
                else -> throw IllegalArgumentException()
            }
        }
        return x * y
    }
}
