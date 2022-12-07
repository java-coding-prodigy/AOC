package aoc2015

fun main() = Day18().run()

class Day18 : Day(18) {
    override fun compute(input: List<String>): Pair<Any?, Any?> {
        val grid = input.map { it.map('#'::equals).toBooleanArray() }.toTypedArray()
        var p1 = grid
        var p2 = grid
        repeat(100) {
            p1 = newGrid(p1)
            p2 = newGrid(p2, true)
        }
        return getOns(p1) to getOns(p2)
    }

    private fun getOns(grid: Array<BooleanArray>) = grid.sumOf { row -> row.count { it } }

    private fun newGrid(grid: Array<BooleanArray>, part2: Boolean = false): Array<BooleanArray> {
        val newGrid = Array(grid.size) { BooleanArray(grid[it].size) { false } }
        for ((y, row) in grid.withIndex()) {
            for ((x, b) in row.withIndex()) {
                val onNeighbours = neighbours(x, y, grid).count { it }
                newGrid[y][x] = (if (b) onNeighbours == 2 else false) || onNeighbours == 3
            }
        }
        if (part2) {
            newGrid[0][0] = true
            newGrid[0][grid.lastIndex] = true
            newGrid[grid.lastIndex][0] = true
            newGrid[grid.lastIndex][grid.lastIndex] = true
        }
        return newGrid
    }

    fun neighbours(x: Int, y: Int, grid: Array<BooleanArray>): Iterable<Boolean> {
        val neighbours = mutableListOf<Boolean>()
        if (y != 0) {
            neighbours.add(grid[y - 1][x])
            if (x != 0) {
                neighbours.add(grid[y - 1][x - 1])
            }
            if (x != grid.lastIndex) {
                neighbours.add(grid[y - 1][x + 1])
            }
        }
        if (x != 0) {
            neighbours.add(grid[y][x - 1])
        }
        if (x != grid.lastIndex) {
            neighbours.add(grid[y][x + 1])
        }
        if (y != grid.lastIndex) {
            neighbours.add(grid[y + 1][x])
            if (x != 0) {
                neighbours.add(grid[y + 1][x - 1])
            }
            if (x != grid[y].size - 1) {
                neighbours.add(grid[y + 1][x + 1])
            }
        }
        return neighbours
    }

}