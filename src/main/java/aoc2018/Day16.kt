package aoc2018

fun main() = Day16().run()

class Day16 : Day(16) {


    override fun part1(input: List<String>): Any {


        return input.joinToString("\n").split("\n\n\n")[0].split("\n\n").count { sample ->

            val (before, command, after) = getBreakUp(sample)
            val (_, a, b, c) = command

            commands.count {
                val temp = before.clone()
                it(temp, a, b, c)
                temp.contentEquals(after)
            } >= 3
        }
    }

    private fun getBreakUp(sample: String) = sample.split('\n')
        .map {
            it.substringAfter('[').substringBefore(']').split(",? ".toRegex())
                .map(String::toInt).toIntArray()
        }


    override fun part2(input: List<String>): Any {
        val (samples, program) = input.joinToString("\n").split("\n\n\n\n")
        val mapping = getMapping(samples.split("\n\n").map(this::getBreakUp))
        return execute(program.split('\n'), mapping)[0]
    }

    private fun getMapping(samples: List<List<IntArray>>): Array<(IntArray, Int, Int, Int) -> Unit> {

        val possibilities = Array(16) { commands.toMutableSet() }
        val settled = Array<(IntArray, Int, Int, Int) -> Unit>(
            16
        ) {
            { _: IntArray, _: Int, _: Int, _: Int -> throw UnsupportedOperationException() }
        }
        while (possibilities.any(Collection<*>::isNotEmpty)) {
            samples.forEach { (before, command, after) ->
                val (opcode, a, b, c) = command


                val curr = possibilities[opcode]

                curr.filter {
                    val temp = before.clone()
                    it(temp, a, b, c)
                    settled.contains(it) || !temp.contentEquals(after)
                }.forEach { curr.remove(it) }

                if (curr.size == 1) {
                    settled[opcode] = (curr.first())
                }
            }
        }

        return settled
    }

    private fun execute(
        instructions: List<String>,
        instructionMapping: Array<(IntArray, Int, Int, Int) -> Unit>
    ): IntArray {
        val registers = IntArray(4) { 0 }
        instructions.map { it.split(' ').filter(String::isNotEmpty).map(String::toInt) }
            .forEach { (opcode, a, b, c) ->
                instructionMapping[opcode](registers, a, b, c)
            }
        return registers
    }

    private val commands = setOf<(IntArray, Int, Int, Int) -> Unit>(
        { registers, a, b, c -> registers[c] = registers[a] + registers[b] },
        { registers, a, b, c -> registers[c] = registers[a] + b },
        { registers, a, b, c -> registers[c] = registers[a] * registers[b] },
        { registers, a, b, c -> registers[c] = registers[a] * b },
        { registers, a, b, c -> registers[c] = registers[a] and registers[b] },
        { registers, a, b, c -> registers[c] = registers[a] and b },
        { registers, a, b, c -> registers[c] = registers[a] or registers[b] },
        { registers, a, b, c -> registers[c] = registers[a] or b },
        { registers, a, _, c -> registers[c] = registers[a] },
        { registers, a, _, c -> registers[c] = a },
        { registers, a, b, c -> registers[c] = if (a > registers[b]) 1 else 0 },
        { registers, a, b, c -> registers[c] = if (registers[a] > b) 1 else 0 },
        { registers, a, b, c ->
            registers[c] = if (registers[a] > registers[b]) 1 else 0
        },
        { registers, a, b, c -> registers[c] = if (a == registers[b]) 1 else 0 },
        { registers, a, b, c -> registers[c] = if (registers[a] == b) 1 else 0 },
        { registers, a, b, c ->
            registers[c] = if (registers[a] == registers[b]) 1 else 0
        },

        )

}
