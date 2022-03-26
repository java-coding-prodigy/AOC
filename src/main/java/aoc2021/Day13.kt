package aoc2021

import java.awt.Point

fun main() = Day13().part2()/*println(Day13().part1Test("""6,10
0,14
9,10
0,3
10,4
4,11
6,0
6,12
4,1
0,13
10,12
3,4
3,0
8,4
1,10
2,14
8,10
9,0

fold along y=7
fold along x=5""".split("\n"), 17))*/

class Day13 : Day(13) {
    override fun part1(input: List<String>): Any {
        val sheet = Sheet(input)
        sheet.fold(0)
        return sheet.dots.distinct().size
    }


    class Sheet(input: List<String>) {
        val folds: MutableList<Fold>
        val dots: MutableSet<Point>

        init {
            folds = ArrayList()
            dots = HashSet()
            var onFolds = false
            for (line in input) {
                if (line.isEmpty()) {
                    onFolds = true
                    continue
                }
                if (onFolds) {
                    folds.add(Fold(line))
                } else {
                    val split = line.split(",")
                    dots.add(Point(split[0].toInt(), split[1].toInt()))
                }
            }
        }

        fun fold(index: Int) : Sheet{
            val fold = folds[index]
            if (fold.type == FoldType.HORIZONTAL) {
                for (point in HashSet(dots)) {
                    if (point.y >= fold.value) {
                        dots.remove(point)
                        dots.add(Point(point.x, fold.value * 2 - point.y))
                    }
                }
            } else {
                for (point in HashSet(dots)) {
                    if (point.x >= fold.value) {
                        dots.remove(point)
                        dots.add(Point( fold.value * 2  - point.x,point.y))
                    }
                }
            }
            return this
        }

        fun foldAll() : Sheet{
            for(i in folds.indices){
                fold(i)
            }
            return this
        }

        override fun toString(): String {
            val grid: MutableList<MutableList<String>> = ArrayList()
            for (i in 0..dots.maxOf { it.y }) {
                val row: MutableList<String> = ArrayList()
                for (j in 0..dots.maxOf { it.x }) {
                    row.add(if (dots.contains(Point(j, i))) "|" else " ")
                }
                grid.add(row)
            }
            return grid.joinToString("\n", "\n", "\n") { it.joinToString("") }
        }
    }

    class Fold(line: String) {
        val value: Int
        val type: FoldType

        init {
            val split = line.split("=")
            type = if (split[0].last() == 'y') FoldType.HORIZONTAL else FoldType.VERTICAL
            value = split[1].toInt()
        }

        override fun toString(): String = "move $value by $type"
    }

    enum class FoldType { HORIZONTAL, VERTICAL }

    override fun part2(input: List<String>): Any {
        return Sheet(input).foldAll()
    }
}
