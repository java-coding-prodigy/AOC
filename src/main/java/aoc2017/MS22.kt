package aoc2017

import java.awt.Point

fun main() = MS22().run()

class MS22 : MS(22) {

    override fun part1(input: List<String>): Any? = stimulate(input, 10_000) {
        when (it) {
            State.CLEAN -> State.INFECTED
            State.INFECTED -> State.CLEAN
            else -> throw IllegalArgumentException()
        }
    }

    override fun part2(input: List<String>): Any? = stimulate(input, 10_000_000) {
        when (it) {
            State.CLEAN -> State.WEAKENED
            State.WEAKENED -> State.INFECTED
            State.INFECTED -> State.FLAGGED
            State.FLAGGED -> State.CLEAN
        }
    }

    enum class State {
        CLEAN, WEAKENED, INFECTED, FLAGGED

    }

    private fun stimulate(input: List<String>, bursts: Int, stateTransform: (State) -> State): Int {
        val states = mutableMapOf<Point, State>()
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, ch ->
                if (ch == '#')
                    states[Point(x, y)] = State.INFECTED
            }
        }
        var y = input.size / 2
        var x = input[0].length / 2
        var dir = 'n'
        var infections = 0
        fun move() {
            when (dir) {
                'n' -> y--
                'e' -> x++
                's' -> y++
                'w' -> x--
            }
        }


        fun turn(state: State) {
            dir = when (state) {
                State.CLEAN ->
                    when (dir) {
                        'n' -> 'w'
                        'e' -> 'n'
                        's' -> 'e'
                        'w' -> 's'
                        else -> throw IllegalStateException()
                    }
                State.WEAKENED -> dir
                State.INFECTED -> when (dir) {
                    'n' -> 'e'
                    'e' -> 's'
                    's' -> 'w'
                    'w' -> 'n'
                    else -> throw IllegalStateException()
                }
                State.FLAGGED -> when (dir) {
                    'n' -> 's'
                    'e' -> 'w'
                    's' -> 'n'
                    'w' -> 'e'
                    else -> throw IllegalStateException()
                }
            }
        }
        repeat(bursts) {
            val point = Point(x, y)
            val state = states[point] ?: State.CLEAN
            turn(state)
            states[point] = stateTransform(state).also { if (it == State.INFECTED) infections++ }
            move()
        }

        return infections
    }


}
