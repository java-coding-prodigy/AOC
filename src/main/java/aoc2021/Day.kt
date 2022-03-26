package aoc2021


import java.io.File

fun main(input: Array<String>){
    val clazz = Class.forName("aoc2021/Day${input[0]}")
    val obj = clazz.getConstructor().newInstance()
    clazz.getMethod("part${input[1]}").invoke(obj)
}

abstract class Day(private val number: Int) {

    private val inputPath : File = File("src/main/resources/Day$number.txt")
    private val input = inputPath.readLines()


    fun run() {
            println(part1(input))
            println(part2(input))
    }

    fun part1() = println(part1(input))

    fun part2() = println(part2(input))

    abstract fun part1(input: List<String>): Any

    abstract fun part2(input: List<String>): Any

    fun part1Test(input: List<String>, answer: Any): Boolean {
        return part1(input) == answer
    }

    fun part2Test(input: List<String>, answer: Any): Boolean {
        val ans = part2(input)
        println(ans)
        return ans == answer
    }

    fun getNumber(): Int {
        return number
    }

    fun getPath(): File {
        return inputPath
    }
}
