package aoc2018

fun main() = Day19().run()


fun runAndGetFirst(input: List<String>, registers: IntArray): Int {

    return runAndGetFirst(parse(input),
        registers,
        input[0].last().digitToInt())
}

private fun parse(input: List<String>): List<(IntArray) -> Unit> =
    input.subList(1, input.size).map { line ->
        val (a, b, c) = line.substringAfter(' ').split(' ').map(String::toInt)
        val opcode = line.substringBefore(' ')
        val command = commands[opcode]!!
        { command(it, a, b, c) }
    }

fun runAndGetFirst(
    instructions: List<(IntArray) -> Unit>,
    registers: IntArray,
    ipCom: Int,
): Int {
    var ip = 0
    val instructionCount = instructions.size
    var t = 0

    while ((0 <= ip) && (ip < instructionCount)) {

        instructions[ip](registers)
        registers[ipCom]++
        ip = registers[ipCom]
        t++

    }


    return registers[0]
}

class Day19 : Day(19) {

    override fun part1(input: List<String>): Any = runAndGetFirst(input, IntArray(6) { 0 })

    override fun part2(input: List<String>): Any {
        val registers = IntArray(6)
        registers[0] = 1
        val r1 = input[3].last().digitToInt()
        val (r3, r2) = input[4].split(' ').mapNotNull(String::toIntOrNull).filterNot(r1::equals)
        val r5 = input[5].split(' ').mapNotNull(String::toIntOrNull).filterNot(r2::equals).first()
        val ipCom = input[0].last().digitToInt()
        var ip = 0
        val instructions = parse(input)
        while (ip < instructions.size) {

            if (ip == 2 && registers[r3] != 0) {
                if (registers[r5] % registers[r3] == 0) {
                    registers[0] += registers[r3]
                }
                registers[r2] = 0
                registers[r1] = registers[r5]
                ip = 12
                registers[ipCom] = ip

            } else {
                instructions[ip](registers)
                ip = (++registers[ipCom])
            }
        }
        return registers[0]
    }


}

val commands = mapOf<String, (IntArray, Int, Int, Int) -> Unit>(
    Pair("addr") { registers, a, b, c -> registers[c] = registers[a] + registers[b] },
    Pair("addi") { registers, a, b, c -> registers[c] = registers[a] + b },
    Pair("mulr") { registers, a, b, c -> registers[c] = registers[a] * registers[b] },
    Pair("muli") { registers, a, b, c -> registers[c] = registers[a] * b },
    Pair("banr") { registers, a, b, c -> registers[c] = registers[a] and registers[b] },
    Pair("bani") { registers, a, b, c -> registers[c] = registers[a] and b },
    Pair("borr") { registers, a, b, c -> registers[c] = registers[a] or registers[b] },
    Pair("bori") { registers, a, b, c -> registers[c] = registers[a] or b },
    Pair("setr") { registers, a, _, c -> registers[c] = registers[a] },
    Pair("seti") { registers, a, _, c -> registers[c] = a },
    Pair("gtir") { registers, a, b, c -> registers[c] = if (a > registers[b]) 1 else 0 },
    Pair("gtri") { registers, a, b, c -> registers[c] = if (registers[a] > b) 1 else 0 },
    Pair("gtrr") { registers, a, b, c ->
        registers[c] = if (registers[a] > registers[b]) 1 else 0
    },
    Pair("eqir") { registers, a, b, c -> registers[c] = if (a == registers[b]) 1 else 0 },
    Pair("eqri") { registers, a, b, c -> registers[c] = if (registers[a] == b) 1 else 0 },
    Pair("eqrr") { registers, a, b, c ->
        registers[c] = if (registers[a] == registers[b]) 1 else 0
    },

    )
