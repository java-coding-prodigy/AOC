package aoc2021

import kotlin.math.abs

fun main() = Day5().run()
class Day5 : Day(5) {
    override fun part1(input: List<String>): Any {
        val vents: MutableSet<Vent> = HashSet()
        for (line in input) {
            val (x1,y1,x2,y2) = line.split(" -> ").flatMap { it.split(",") }.map { it.toInt() }
            if (x1 == x2) {
                for (i in if(y2 > y1) y1..y2 else y2..y1) {
                    val vent = Vent(x1, i)
                    try {
                        vents.filter { it == vent }[0].incLine()
                    } catch (e: IndexOutOfBoundsException) {
                        vents.add(vent)
                        vent.incLine()
                    }
                }
            } else if (y1 == y2) {
                for (i in if(x2 > x1) x1..x2 else x2..x1) {
                    val vent = Vent(i, y1)
                    try {
                        vents.filter { it == vent }[0].incLine()
                    } catch (e: IndexOutOfBoundsException) {
                        vents.add(vent)
                        vent.incLine()
                    }
                }
            }
        }
        return vents.filter { it.lines >= 2 }.size
    }


    class Vent(val x: Int, val y: Int) {
        var lines = 0

        fun incLine() = lines++


        override fun equals(other: Any?): Boolean {
            if (this === other)
                return true
            if (other !is Vent)
                return false
            return x == other.x && y == other.y
        }

        override fun hashCode(): Int {
            var result = x
            result = 31 * result + y
            return result
        }

        override fun toString() : String = "$x,$y lines $lines"
    }

    override fun part2(input: List<String>): Any {
        val vents: MutableSet<Vent> = HashSet()
        for (line in input) {
            val (x1,y1,x2,y2) = line.split(" -> ").flatMap { it.split(",") }.map { it.toInt() }
            if (x1 == x2) {
                for (i in if(y2 > y1) y1..y2 else y2..y1) {
                    val vent = Vent(x1, i)
                    try {
                        vents.filter { it == vent }[0].incLine()
                    } catch (e: IndexOutOfBoundsException) {
                        vents.add(vent)
                        vent.incLine()
                    }
                }
            } else if (y1 == y2) {
                for (i in if(x2 > x1) x1..x2 else x2..x1) {
                    val vent = Vent(i, y1)
                    try {
                        vents.filter { it == vent }[0].incLine()
                    } catch (e: IndexOutOfBoundsException) {
                        vents.add(vent)
                        vent.incLine()
                    }
                }
            } else if(abs(x1 - x2) == abs(y1 - y2)){
                //have to do this because kotlin does not allow multiple counter variables in a loop
                val x = if(x1 > x2) x1 else x2
                val y = if(x1 > x2) y1 else y2
                var i = if(x1 < x2) x1 else x2
                var j = if(x1 < x2) y1 else y2
                while(i <= x){
                    val vent = Vent(i,j)
                    try {
                        vents.filter { it == vent }[0].incLine()
                    } catch (e: IndexOutOfBoundsException) {
                        vents.add(vent)
                        vent.incLine()
                    }
                    i++
                    j += if(j <= y) 1 else -1
                }
            }
        }
        return vents.filter { it.lines >= 2 }.size
    }
}
