package aoc2018

fun main() = Day1().run()

class Day1 : Day(1) {
    override fun part1(input: List<String>): Any {
        return input.map(String::toInt).sum()
    }

    override fun part2(input: List<String>): Any {

        var frequency = 0
        val seen = mutableSetOf(frequency)
        val frequencies = input.map(String::toInt)
        while(true){
            for(f in frequencies){
                frequency += f
                if(!seen.add(frequency))
                    return frequency
            }
        }
    }
}
