package aoc2021

fun main() = Day25().part1()

class Day25 : Day(25) {
    override fun part1(input: List<String>): Any {
        var count = 0
        var state = State(input)
        while (!state.noneCanMove()) {
            state = state.nextState()
            count++
        }
        return count
    }

    enum class BoardItem { EMPTY, SOUTH, EAST }

    class State(private val cells: MutableList<MutableList<BoardItem>> = ArrayList()) {


        constructor(input: Iterable<String>) : this() {
            if (input is List)
                for (i in input.indices) {
                    val line: MutableList<BoardItem> = ArrayList()
                    for (j in input[i].indices) {
                        line.add(when (input[i][j]) {
                            '.' -> BoardItem.EMPTY
                            '>' -> BoardItem.EAST
                            'v' -> BoardItem.SOUTH
                            else -> throw UnsupportedOperationException()
                        })
                    }
                    cells.add(line)
                }
            //println(this)
        }

        fun noneCanMove(): Boolean {
            for (i in cells.indices) {
                for (j in cells[i].indices) {
                    if (canMove(i, j))
                        return false
                }
            }
            return true
        }

        override fun toString(): String {
            val sb = StringBuilder()
            for (i in cells) {
                for (j in i) {
                    sb.append(when (j) {
                        BoardItem.EMPTY -> "."
                        BoardItem.EAST -> ">"
                        BoardItem.SOUTH -> "v"
                    })
                }
                sb.append("\n")
            }
            return sb.toString()
        }

        private fun canMove(y: Int, x: Int): Boolean = when (cells[y][x]) {
            BoardItem.EMPTY -> false
            BoardItem.EAST -> {
                val right = if (x == cells[y].size - 1) 0 else x + 1
                cells[y][right] == BoardItem.EMPTY
            }
            BoardItem.SOUTH -> {
                val right = if (x == cells[y].size - 1) 0 else x + 1
                val down = if (y == cells.size - 1) 0 else y + 1
                val left = (if (x == 0) cells[y].size else x) - 1
                (cells[down][x] == BoardItem.EMPTY && cells[down][left] != BoardItem.EAST)
                        || (cells[down][x] == BoardItem.EAST
                        && cells[down][right] == BoardItem.EMPTY)
            }
        }

        fun nextState(): State {
            val resultList: MutableList<MutableList<BoardItem>> =
                ArrayList(cells.map { row -> ArrayList(row.map { BoardItem.EMPTY }) })
            for (y in cells.indices) {
                for (x in cells[y].indices) {
                    /*if (canMove(y, x)) {
                        if (cells[y][x] == BoardItem.EAST) {
                           // resultList[y][x] = BoardItem.EMPTY
                            resultList[y][if (x == cells[y].size - 1) 0 else x + 1] = BoardItem.EAST
                        } else {
                           // resultList[y][x] = BoardItem.EMPTY
                            resultList[if (y == cells.size - 1) 0 else y + 1][x] =
                                BoardItem.SOUTH
                        }
                    }*/


                    when (cells[y][x]) {
                        BoardItem.EAST -> {
                            if (canMove(y,x)) {
                                val right = if (x == cells[y].size - 1) 0 else x + 1
                                resultList[y][right] = BoardItem.EAST
                            } else {
                                resultList[y][x] = BoardItem.EAST
                            }
                        }
                        BoardItem.SOUTH -> {
                            val down = if (y == cells.size - 1) 0 else y + 1
                            if (canMove(y,x)                            )
                                resultList[down][x] = BoardItem.SOUTH
                            else {
                                resultList[y][x] = BoardItem.SOUTH
                            }
                        }
                        BoardItem.EMPTY -> {}
                    }
                }
            }
            println(this)
            return State(resultList)
        }

    }


    override fun part2(input: List<String>): Any {
        TODO("Not yet implemented")
    }
}
