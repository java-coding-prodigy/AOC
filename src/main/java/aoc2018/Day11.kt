package aoc2018

fun main() = Day11().run()

class Day11 : Day(11) {


    override fun part1(input: List<String>): Any {
        val serialNo = input[0].toInt()
        val grid = Array(300) { y -> Array(300) { x -> powerLevel(x, y, serialNo) } }
        return (0..297).flatMap { y -> (0..297).map { x -> Pair(x, y) } }
            .maxByOrNull { (x, y) ->
                (y until y + 3).sumOf { row -> (x until x + 3).sumOf { col -> grid[row][col] } }
            }!!.toList().joinToString(",")
    }

    override fun part2(input: List<String>): Any {
        val serialNo = input[0].toInt()
        val grid = Array(300) { y -> Array(300) { x -> powerLevel(x, y, serialNo) } }
        return (1..300).flatMap { size ->
            (0..300 - size).flatMap { y ->
                (0..300 - size).map { x ->
                    Triple(
                        x,
                        y, size
                    )
                }
            }
        }.maxByOrNull { (x, y, size) ->
            (y until y + size).sumOf { row -> (x until x + size).sumOf { col -> grid[row][col] } }
        }!!.toList().joinToString(",")

    }


    private fun powerLevel(x: Int, y: Int, serialNo: Int): Int {
        val rackId = x + 10
        var powerLevel = rackId * y
        powerLevel += serialNo
        powerLevel *= rackId
        powerLevel = powerLevel / 100 % 10
        powerLevel -= 5
        return powerLevel
    }
}
