package aoc2021

import java.util.*

fun main() = Day4().run() /*println(Day4().part2Test("""7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1

22 13 17 11  0
 8  2 23  4 24
21  9 14 16  7
 6 10  3 18  5
 1 12 20 15 19

 3 15  0  2 22
 9 18 13 17  5
19  8  7 25 23
20 11 10 24  4
14 21 16 12  6

14 21 17 24  4
10 16 15  9 19
18  8 23 26 20
22 11 13  6  5
 2  0 12  3  7""".split("\n"),1924))*/
class Day4 : Day(4) {


    override fun part1(input: List<String>): Any {
        val numbers: List<Int> = input.subList(0, 1).flatMap { it.split(",") }.map { it.toInt() }
        val grids = mutableListOf<Grid>()
        run {
            var grid = mutableListOf<String>()
            for (line in input.subList(2, input.size)) {
                if (line.isEmpty() || line == input.last()) {
                    if (line.isNotEmpty())
                        grid.add(line)
                    grids.add(Grid(grid))
                    grid = mutableListOf()
                } else {
                    grid.add(line)
                }
            }
        }
        for (number in numbers) {
            for (grid in grids) {
                val opt = grid.markNumber(number)
                if (!opt.isEmpty) {
                    println(opt)
                    return opt.get()
                }
            }
        }
        throw UnsupportedOperationException("idk what I did wrong")
    }

    class GridNumber(val number: Int) {
        private var marked = false


        fun isMarked(): Boolean = marked

        fun setMarked(marked: Boolean) {
            this.marked = marked
        }

        override fun toString(): String {
            return number.toString()
        }

    }

    class Row(line: List<Int>) {
        val numbers: List<GridNumber>

        override fun toString(): String {
            return numbers.joinToString(" ")
        }

        init {
            numbers = line.map { GridNumber(it) }
            //println(this)
        }

        constructor(line: String) : this(line.split(" ").filter { it.isNotEmpty() }.map { it.toInt() })


        fun isComplete(): Boolean = numbers.all { it.isMarked() }

        fun size(): Int = numbers.size
    }

    class Column(numbers: List<Int>) {
        val numbers: List<GridNumber>

        override fun toString(): String {
            return numbers.toString()
        }

        init {
            this.numbers = numbers.map { GridNumber(it) }
        }


        fun isComplete(): Boolean = numbers.all { it.isMarked() }


        fun size(): Int = numbers.size
    }

    class Grid(lines: List<String>) {
        private val rows: List<Row>
        private val columns: List<Column>
        private val numbers: Set<GridNumber>

        override fun toString(): String {
            return columns.joinToString("\n")
        }

        init {
            numbers = lines.flatMap { it.split(" ", "\n") }.filter { it.isNotEmpty() }.map { GridNumber(it.toInt()) }.toSet()
            rows = lines.map { Row(it) }
            columns = ArrayList()
            for (i in 0 until rows[0].size()) {
                columns.add(Column(rows.map { it.numbers[i].number }))
            }
        }

        fun markNumber(number: Int): Optional<Int> {
            try {
                numbers.filter { it.number == number }[0].setMarked(true)
                rows.forEach { row ->
                    try {
                        row.numbers.filter { it.number == number }[0].setMarked(true)
                    } catch (ignore: IndexOutOfBoundsException) {
                    }
                }
                columns.forEach { col ->
                    try {
                        col.numbers.filter { it.number == number }[0].setMarked(true)
                    } catch (ignore: IndexOutOfBoundsException) {
                    }
                }
                for (row in rows) {
                    if (row.isComplete()) {
                        println(number)
                        println(numbers)
                        //println(numbers.filter{!it.isMarked() || it.number == 12 || it.number == 3})
                        return Optional.of(numbers.filter { !it.isMarked() }.map { it.number }.reduce { a, b -> a + b } * number)
                    }
                }
                for (col in columns) {
                    if (col.isComplete()) {
                        return Optional.of(numbers.filter { !it.isMarked() }.map { it.number }.reduce { a, b -> a + b } * number)
                    }
                }
            } catch (ignore: IndexOutOfBoundsException) {
            }
            return Optional.empty()
        }

        fun markNumber1(number: Int) {
            try {
                numbers.filter { it.number == number }[0].setMarked(true)
                rows.forEach { row ->
                    try {
                        row.numbers.filter { it.number == number }[0].setMarked(true)
                    } catch (ignore: IndexOutOfBoundsException) {
                    }
                }
                columns.forEach { col ->
                    try {
                        col.numbers.filter { it.number == number }[0].setMarked(true)
                    } catch (ignore: IndexOutOfBoundsException) {
                    }
                }
            } catch (ignore: IndexOutOfBoundsException) {
            }
        }

        fun hasWon(): Boolean = rows.any { it.isComplete() } || columns.any { it.isComplete() }

        fun score(number: Int): Int = numbers.filter { !it.isMarked() }.map { it.number }.reduce { a, b -> a + b } * number
    }

    override fun part2(input: List<String>): Any {
        val numbers: List<Int> = input.subList(0, 1).flatMap { it.split(",") }.map { it.toInt() }
        val grids = mutableListOf<Grid>()
        run {
            var grid = mutableListOf<String>()
            for (line in input.subList(2, input.size)) {
                if (line.isEmpty() || line == input.last()) {
                    if (line.isNotEmpty())
                        grid.add(line)
                    grids.add(Grid(grid))
                    grid = mutableListOf()
                } else {
                    grid.add(line)
                }
            }
        }
        for(number in numbers){
            println(number)
            for(grid in ArrayList(grids)){
                grid.markNumber1(number)
                if(grid.hasWon()){
                    if(grids.size == 1){
                        return grid.score(number)
                    }
                    grids.remove(grid)
                }
            }
        }
        return 0
    }
}
