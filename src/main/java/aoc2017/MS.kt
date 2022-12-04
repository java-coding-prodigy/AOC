package aoc2017

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.readLines

abstract class MS(number: Int) {

    private val inputPath: Path
    private val input: List<String>

    init {
        inputPath =
            Path("src/main/resources/2017/MS$number.txt")
        input = inputPath.readLines()
    }

    fun run(){
        part1()
        part2()
    }

    fun part1() = println("Part 1: ${part1(input)}")

    fun part2() = println("Part 2: ${part2(input)}")

    abstract fun part1(input: List<String>) : Any?

    abstract fun part2(input: List<String>) : Any?
}
