package aoc2015

fun main() = Day14().run()

class Day14 : Day(14) {

    class Reindeer(private val speed: Int, private val duration: Int, private val waitTime: Int) {
        var distance = 0
        var firsts = 0
        private var timeRunning = 0
        private var timeToWait = 0
        fun move() {
            if (timeToWait == 0) {
                if (timeRunning != duration) {
                    distance += speed
                    timeRunning++
                } else {
                    timeRunning = 0
                    timeToWait = waitTime - 1
                }
            } else {
                timeToWait--
            }
        }
    }

    override fun compute(input: List<String>): Pair<Any?, Any?> {
        val reindeer = input.map {
            val (speed, duration, waitTime) = it.split(" ").mapNotNull(String::toIntOrNull)
            Reindeer(speed, duration, waitTime)
        }
        repeat(2503) {
            reindeer.forEach(Reindeer::move)
            val sorted = reindeer.sortedByDescending(Reindeer::distance)
            val max = sorted.first().distance
            sorted.takeWhile { it.distance == max }.forEach { it.firsts++ }
        }
        return reindeer.maxOf(Reindeer::distance) to reindeer.maxOf(Reindeer::firsts)
    }
}