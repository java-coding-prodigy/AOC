package aoc2017

import java.util.*


fun main() = MS18().run()

class MS18 : MS(18) {
    override fun part1(input: List<String>): Any? {
        val registers = mutableMapOf<Char, Long>()
        var idx = 0
        var lastSound = 0L
        fun getVal(par: String, registers: MutableMap<Char, Long>): Long =
            if (par[0].isLetter()) registers[par[0]] ?: 0 else par.toLong()
        while (idx < input.size && idx >= 0) {
            val instruction = input[idx].split(' ')
            val command = instruction[0]
            val reg = instruction[1][0]
            val regVal = registers[reg] ?: 0
            when (command) {
                "snd" -> lastSound = regVal
                "rcv" -> if (regVal != 0L) {
                    return lastSound
                }
                "jgz" ->
                    if (regVal > 0) {
                        idx += getVal(instruction[2], registers).toInt()
                        continue
                    }
                else -> {
                    val param = getVal(instruction[2], registers)
                    registers[reg] = when (command) {
                        "set" -> param
                        "add" -> regVal + param
                        "mul" -> regVal * param
                        "mod" -> (regVal % param + param) % param
                        else -> throw IllegalArgumentException()
                    }
                }
            }
            idx++
        }
        throw IllegalStateException()
    }



    enum class Status {
        RUNNING, TERMINATED, WAITING
    }

    override fun part2(input: List<String>): Any? {


        data class Program(val programId: Int) {
            val registers = mutableMapOf(Pair("p", programId.toLong()))
            val queue: Queue<Long> = ArrayDeque()
            var status = Status.RUNNING
            var idx = 0

            fun getVal(par: String) = if (par[0].isLetter()) registers[par] ?: 0 else par.toLong()

            fun queue(num: Long) = queue.add(num)
        }

        var current = Program(0)
        var other = Program(1)
        var sentBy1 = 0
        val instructions: List<List<String>> = input.map { it.split(' ') }
        var run = true
        while (run) {
            current.apply {
                val currInstr = instructions[idx]
                val regId = currInstr[1]
                val regVal = getVal(regId)
                when (currInstr[0]) {
                    "snd" -> {
                        if (programId == 1) sentBy1++
                        other.queue(regVal)
                    }
                    "rcv" -> {
                        if (queue.isNotEmpty()) {
                            status = Status.RUNNING
                            registers[regId] = queue.poll()
                        } else {
                            if (other.status == Status.TERMINATED || (other.queue.isEmpty() && other.status == Status.WAITING)) {
                                run = false
                                return@apply
                            }
                            status = Status.WAITING
                            current = other
                            other = this
                            idx--
                        }
                    }
                    "jgz" -> {
                        if (regVal > 0) {
                            idx += getVal(currInstr[2]).toInt() - 1
                        }
                    }
                    else -> {
                        val param = getVal(currInstr[2])
                        registers[regId] = when (currInstr[0]) {
                            "set" -> param
                            "add" -> regVal + param
                            "mul" -> regVal * param
                            "mod" -> (regVal % param + param) % param
                            else -> throw IllegalArgumentException(currInstr[0])
                        }
                    }
                }
                idx++
                if (!(idx < instructions.size && idx >= 0)) {
                    if (other.status == Status.TERMINATED) {
                        run = false
                        return@apply
                    }
                    status = Status.TERMINATED
                    current = other
                    other = this
                }
            }
        }

        return sentBy1
    }
}
