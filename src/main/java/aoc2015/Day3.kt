package aoc2015


import java.awt.Point

fun main() = Day3().run()

class Day3 : Day(3) {
    override fun compute(input: List<String>): Pair<Any?, Any?> {
        val pointsVisited = mutableSetOf(Point(0, 0))
        var x = 0
        var y = 0
        for (c in input[0]) {
            when (c) {
                '^' -> y++
                'v' -> y--
                '>' -> x++
                '<' -> x--
            }
            pointsVisited.add(Point(x, y))
        }
        val p1 = pointsVisited.size
        pointsVisited.clear()
        pointsVisited.add(Point(0,0))
        x = 0
        y = 0
        var xr = 0
        var yr = 0
        val itr = input[0].iterator()
        while (itr.hasNext()) {
            when (itr.next()) {
                '^' -> y++
                'v' -> y--
                '>' -> x++
                '<' -> x--
            }
            when (itr.next()) {
                '^' -> yr++
                'v' -> yr--
                '>' -> xr++
                '<' -> xr--
            }
            pointsVisited.add(Point(x, y))
            pointsVisited.add(Point(xr, yr))
        }
        return p1 to pointsVisited.size
    }
}