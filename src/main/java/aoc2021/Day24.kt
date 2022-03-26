package aoc2021

import java.util.*

fun main() = Day24().part2()

class Day24 : Day(24) {

    data class Int3(val idx: Int, val x: Int, val y: Int)

    override fun part1(input: List<String>): Any {

        val list: MutableList<Int3> = ArrayList()
        run {
            var idx = 0
            var i = 5
            var j = 15
            while (i < input.size) {
                list.add(Int3(idx, input[i].split(" ")[2].toInt(), input[j].split(" ")[2].toInt()))
                idx += 1
                i += 18
                j += 18
            }
        }
        val monad: Array<Int> = Array(14) { 0 }
        val stack = Stack<Int3>()
        for (triple in list) {
            if (triple.x >= 10) {
                stack.push(triple)
            } else {
                val prev = stack.pop()
                val something = prev.y + triple.x
                if (something >= 0) {
                    monad[triple.idx] = 9
                    monad[prev.idx] = 9 - something
                } else {
                    monad[prev.idx] = 9
                    monad[triple.idx] = 9 + something
                }
            }
        }
        return monad.joinToString("", transform = Int::toString)
    }

    override fun part2(input: List<String>): Any {
        val list: MutableList<Int3> = ArrayList()
        run {
            var idx = 0
            var i = 5
            var j = 15
            while (i < input.size) {
                list.add(Int3(idx, input[i].split(" ")[2].toInt(), input[j].split(" ")[2].toInt()))
                idx += 1
                i += 18
                j += 18
            }
        }
        val monad: Array<Int> = Array(14) { 0 }
        val stack = Stack<Int3>()
        for (curr in list) {
            if (curr.x >= 10) {
                stack.push(curr)
            } else {
                val prev = stack.pop()
                val something = prev.y + curr.x
                if (something >= 0) {
                    monad[prev.idx] = 1
                    monad[curr.idx] = 1 + something
                } else {
                    monad[curr.idx] = 1
                    monad[prev.idx] = 1 - something
                }
            }
        }
        return monad.joinToString("", transform = Int::toString)
    }
}
