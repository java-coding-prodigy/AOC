package aoc2021

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


fun main() = Day7().part2()
class Day7 : Day(7) {



    private fun fuelNeeded(input: List<String>, fuelTransformation : (Long) ->  Long ) : Long{
        val initialPositions = input.flatMap { it.split(",") }.map { it.toLong() }
        val list : MutableList<Long> = ArrayList()
        for(i in initialPositions.reduce{ a, b -> min(a,b)}..initialPositions.reduce{ a, b -> max(a,b)}){
            list.add(initialPositions.map{ fuelTransformation(abs(it - i)) }.reduce{ a, b -> a + b})
        }
        return list.reduce{ a, b -> min(a,b)}
    }

    override fun part1(input: List<String>): Any {
        return fuelNeeded(input) { it }
    }

    override fun part2(input: List<String>): Any {
        return fuelNeeded(input){it * (it + 1) / 2}
    }
}
