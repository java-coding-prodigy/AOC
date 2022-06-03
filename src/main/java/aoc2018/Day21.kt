package aoc2018


fun main() = Day21().run()

class Day21 : Day(21) {
    override fun part1(input: List<String>): Any {
        val ipCom = input[0].substringAfter(' ').toInt()
        val instructions = input.subList(1, input.size).map<String, (IntArray) -> Unit> { line ->
            val (a, b, c) = line.substringAfter(' ').split(' ').map(String::toInt)
            val opcode = line.substringBefore(' ')
            val command = commands[opcode]!!
            return@map { command(it, a, b, c) }
        }
        val registers = IntArray(6)
        var ip = 0

        while ((0 <= ip) && (ip < instructions.size)) {
            instructions[ip](registers)
            ip = ++registers[ipCom]
            if (ip == 28) {
                break
            }
        }
        return registers[3]
    }

    override fun part2(input: List<String>): Any {
        val ipCom = input[0].substringAfter(' ').toInt()
        val instructions = input.subList(1, input.size).map<String, (IntArray) -> Unit> { line ->
            val (a, b, c) = line.substringAfter(' ').split(' ').map(String::toInt)
            val opcode = line.substringBefore(' ')
            val command = commands[opcode]!!
            return@map { command(it, a, b, c) }
        }
        val registers = IntArray(6)
        var ip = 0
        val seen = mutableSetOf<Int>()
        var last = 0

        while ((0 <= ip) && (ip < instructions.size)) {
            instructions[ip](registers)
            ip = ++registers[ipCom]
            if (ip == 28) {
                if (!seen.add(registers[3]))
                    break
                last = registers[3]
            }
        }
        return last
    }
}
