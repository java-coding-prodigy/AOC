package aoc2018

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun main() = Day4().run()

class Day4 : Day(4) {

    override fun part1(input: List<String>): Any {

        val map = mutableMapOf<Int, MutableList<State>>()
        val currentData = mutableListOf<String>()
        var currentGuard = -1
        val pattern = Regex("\\[1518-\\d+-\\d+ \\d\\d:\\d\\d] Guard #(\\d+) begins shift")

        for (line in input.sortedBy { LocalDateTime.parse(it.substring(1, 17), formatter) }) {
            if (pattern.matches(line)) {
                if (currentGuard != -1) {
                    map.computeIfAbsent(currentGuard) { mutableListOf() }
                        .add(State(currentData))
                    currentData.clear()
                }
                currentGuard = pattern.matchEntire(line)!!.groupValues[1].toInt()
            }
            currentData.add(line)
        }
        map.computeIfAbsent(currentGuard) { mutableListOf() }
            .add(State(currentData))

        val (id, states) =
            map.maxByOrNull { (_: Int, v: MutableList<State>) ->
                v.sumOf { it.countAsleep() }
            }!!

        val min = minutes.maxByOrNull { min -> states.count { !it.awakeAt(min) } }!!

        return id * min
    }

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    private class State(input: List<String>) {

        val awakeMinutes = mutableListOf<Int>()

        init {
            var start: Int = -1
            for ((i, line) in input.withIndex()) {
                if (i % 2 == 0) {

                    val (hour, min) = line.substring(12, 17).split(":").map(String::toInt)
                    start = if (hour == 23) min - 60 else min
                    if (i == 0) {
                        awakeMinutes.addAll(0 until start)
                    }
                    if (i == input.size - 1) {
                        awakeMinutes.addAll(start..60)
                    }
                } else {
                    val end = line.substring(12, 17).split(":")[1].toInt()
                    awakeMinutes.addAll(start until end)
                }
            }
        }

        fun awakeAt(min: Int) = awakeMinutes.contains(min)

    }

    override fun part2(input: List<String>): Any {
        val map = mutableMapOf<Int, MutableList<State>>()
        val currentData = mutableListOf<String>()
        var currentGuard = -1
        val pattern = Regex("\\[1518-\\d+-\\d+ \\d\\d:\\d\\d] Guard #(\\d+) begins shift")

        for (line in input.sortedBy { LocalDateTime.parse(it.substring(1, 17), formatter) }) {
            if (pattern.matches(line)) {
                if (currentGuard != -1) {
                    map.computeIfAbsent(currentGuard) { mutableListOf() }
                        .add(State(currentData))
                    currentData.clear()
                }
                currentGuard = pattern.matchEntire(line)!!.groupValues[1].toInt()
            }
            currentData.add(line)
        }
        map.computeIfAbsent(currentGuard) { mutableListOf() }
            .add(State(currentData))

        val (minute, pair) = minutes.associateWith { min ->
            val (id, states) = map.maxByOrNull { (_, states) -> states.asleep(min) }!!
            val asleep = states.asleep(min)
            Pair(id, asleep)
        }.maxByOrNull { (_, pair) -> pair.second }!!

       return minute * pair.first
    }

    private val minutes: Iterable<Int> = 0 until 60

    private fun Iterable<State>.asleep(min: Int): Int = this.count { !it.awakeAt(min) }

    private fun State.countAsleep() = minutes.count { !this.awakeAt(it) }

}
