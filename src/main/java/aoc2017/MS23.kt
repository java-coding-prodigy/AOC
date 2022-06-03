package aoc2017

fun main() = MS23().run()

class MS23 : MS(23) {


    override fun part1(input: List<String>): Any {
        val registers = ('a'..'h').associateWith { 0 }.toMutableMap()
        var i = 0
        val instructions = input.map {
            val (command, register, param) = it.split(' ')
            Triple(command, register[0]) { getVal(param, registers) }

        }
        var muls = 0
        while (0 <= i && i < instructions.size) {
            val (command, register, parameter) = instructions[i]
            when (command) {
                "sub" -> registers[register] = registers[register]!! - parameter()
                "set" -> registers[register] = parameter()
                "mul" -> registers[register] = registers[register]!! * parameter().also { muls++ }
                "jnz" -> if (register.isDigit() || 0 != registers[register]) i += parameter() - 1
            }
            i++
        }

        return muls
    }


    override fun part2(input: List<String>): Any {
        val registers = ('a'..'h').associateWith { 0 }.toMutableMap()
        registers['a'] = 1
        var i = 0
        val instructions = input.map {
            val (command, register, param) = it.split(' ')
            Triple(command, register[0]) { getVal(param, registers) }

        }
        while (0 == registers['f']!!) {
            val (command, register, parameter) = instructions[i]
            when (command) {
                "sub" -> registers[register] = registers[register]!! - parameter()
                "set" -> registers[register] = parameter()
                "mul" -> registers[register] = registers[register]!! * parameter()
                "jnz" -> if (0 != registers[register]) i += parameter() - 1
            }
            i++
        }
        val b = registers['b']!!

        return (b .. b + 17_000 step 17).count { (2L until it).any { i -> it % i == 0L } }
    }

    private fun getVal(par: String, registers: MutableMap<Char, Int>): Int =
        if (par[0].isLetter()) registers[par[0]] ?: 0 else par.toInt()
}
