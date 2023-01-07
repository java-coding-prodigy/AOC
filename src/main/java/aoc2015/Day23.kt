package aoc2015

fun main() = Day23().run()

class Day23 : Day(23) {
    override fun compute(input: List<String>): Pair<Any?, Any?> = run(input, 0) to run(input, 1)

    private fun run(input: List<String>, firstStartValue: Int): Int {
        var i = 0
        val registers = IntArray(2) { 0 }
        registers[0] = firstStartValue
        while (0 <= i && i < input.size) {
            val instruction = input[i].split(" ", ", ")
            val command = instruction[0]
            i += if (command.startsWith('j')) {
                if (command == "jmp") {
                    instruction[1].toInt()
                } else {
                    val register = registers[instruction[1][0] - 'a']
                    if (if (command.endsWith('o')) register == 1 else register % 2 == 0)
                        instruction[2].toInt()
                    else
                        1
                }
            } else {
                val registerIdx = instruction[1][0] - 'a'
                when (command) {
                    "hlf" -> registers[registerIdx] /= 2
                    "tpl" -> registers[registerIdx] *= 3
                    "inc" -> registers[registerIdx]++
                }
                1
            }
        }
        return registers[1]
    }
}