package aoc2015

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.readLines

abstract class Day(number: Int) {

    private val inputPath: Path =
        Path("src/main/resources/2015/Day$number.txt")
    private val input: List<String> = inputPath.readLines()

    fun run() {
        val (p1, p2) = compute(input)
        println("Part 1 $p1")
        println("Part 2 $p2")
    }

    abstract fun compute(input: List<String>) : Pair<Any?, Any?>


}