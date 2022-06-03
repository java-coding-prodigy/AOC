package aoc2017

fun main() = MS21().run()

class MS21 : MS(21) {
    override fun part1(input: List<String>): Any? {
        return onsAfterEnhancement(input, 5)
    }

    override fun part2(input: List<String>): Any? {
        return onsAfterEnhancement(input,18)
    }

    private fun onsAfterEnhancement(input: List<String>, iterations: Int): Int {
        val mappings: Map<List<String>, List<String>> = input.let {
            val temp = mutableMapOf<List<String>, List<String>>()
            it.forEach { line ->
                val (before, after) = line.split(" => ").map { grid -> grid.split('/') }
                rotations(before).forEach { rotation -> temp[rotation] = after }
            }
            temp
        }
        var grid = listOf(
            ".#.", "..#",
            "###"
        )
        repeat(iterations) {

            val newGrid = mutableListOf<String>()
            val step =
                if (grid.size % 2 == 0) 2 else if (grid.size % 3 == 0) 3 else throw IllegalStateException()
            for (i in grid.indices step step) {
                var row = (0..step).map { "" }
                for (j in grid.indices step step) {
                    row = row.zip(
                        mappings[(grid.subList(i, i + step)
                            .map { it.substring(j, j + step) })]!!
                    ) { s1, s2 -> s1 + s2 }
                }
                newGrid.addAll(row)
            }
            grid = newGrid
        }

        return grid.sumOf { it.count { ch -> ch == '#' } }
    }


    private fun rotations(data: List<String>): List<List<String>> {
        fun flip(data: List<String>): List<String> {
            val results = mutableListOf<String>()
            for (i in data.indices.reversed()) {
                val builder = StringBuilder()
                for (j in 0 until data[0].length) {
                    builder.append(data[i][j])
                }
                results.add(builder.toString())
            }
            return results
        }

        fun rotate(data: List<String>): List<String> {
            val results = mutableListOf<String>()

            for (i in data.indices) {
                val builder = StringBuilder()
                for (j in data.indices) {
                    builder.append(data[data.size - j - 1][i])
                }
                results.add(builder.toString())
            }
            return results
        }

        var currData = data
        val rotations = mutableListOf<List<String>>()
        rotations.add(currData)
        repeat(3) {
            currData = rotate(currData)
            rotations.add(currData)
        }
        currData = flip(currData)
        rotations.add(currData)
        repeat(3) {
            currData = rotate(currData)
            rotations.add(currData)
        }
        return rotations
    }


}
