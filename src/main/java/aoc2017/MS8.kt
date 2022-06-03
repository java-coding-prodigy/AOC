package aoc2017

import java.util.concurrent.atomic.AtomicInteger
import java.util.stream.Collector

fun main() = MS8().run()

class MS8 : MS(8) {
    override fun part1(input: List<String>): Int {
        return input.stream()
            .collect(
                Collector.of(
                    ::mutableMapOf,
                    ::evaluateLine,
                    { _, _ -> throw UnsupportedOperationException() },
                    { it.values.maxOrNull()!! },
                )
            )
    }

    override fun part2(input: List<String>): Any? {
        return input.stream()
            .collect(
                Collector.of(
                    { Pair(mutableMapOf<String, Int>(), AtomicInteger()) },
                    { (registers, max), line -> evaluateLine(registers, line, max) },
                    { _, _ -> throw UnsupportedOperationException() },
                    { it.second.get() },
                )
            )
    }

    private fun evaluateLine(
        registers: MutableMap<String, Int>,
        line: String,
        max: AtomicInteger = AtomicInteger()
    ) {
        val words = line.split(' ')
        val target = words[0]
        if (check(registers[words[4]] ?: 0, words[5], words[6].toInt())) {
            registers[target] =
                (registers[target] ?: 0) + (if (words[1] == "dec") -1 else 1) * words[2].toInt()
            if (max.get() < registers[target]!!) {
                max.set(registers[target]!!)
            }
        }
    }

    private fun check(reg: Int, op: String, toInt: Int): Boolean = when (op) {
        "<" -> reg < toInt
        "<=" -> reg <= toInt
        ">" -> reg > toInt
        ">=" -> reg >= toInt
        "==" -> reg == toInt
        "!=" -> reg != toInt
        else -> throw IllegalStateException()
    }
}
