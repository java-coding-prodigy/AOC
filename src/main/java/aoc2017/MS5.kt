package aoc2017

fun main() = MS5().run()

class MS5 : MS(5) {
    override fun part1(input: List<String>): Any? = untilMazeExit(input) { it + 1 }

    override fun part2(input: List<String>): Any? =
        untilMazeExit(input) { if (it >= 3) it - 1 else it + 1 }

    private fun untilMazeExit(input: List<String>, offsetTransformation: (Int) -> Int): Int {
        val maze = input.map(String::toInt).toMutableList()
        var steps = 0
        var idx = 0
        while (idx < maze.size) {
            val offset = maze[idx]
            maze[idx] = offsetTransformation(offset)
            idx += offset
            steps++
        }
        return steps

    }
}

