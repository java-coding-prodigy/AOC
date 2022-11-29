package aoc2015

import com.google.common.primitives.Ints.min

fun main() = Day2().run()

class Day2 : Day(2) {
    override fun compute(input: List<String>): Pair<Any?, Any?> {
        val dimensions = input.map { it.split('x').map(String::toInt) }
        val p1 =
            dimensions.sumOf { (l, w, h) -> 2 * (l * w + w * h + l * h) + min(l * w, w * h, h * l) }
        val p2 = dimensions.sumOf { (l, w, h) -> 2 * min(l + w, w + h, l + h) + l * w * h }
        return Pair(p1, p2)
    }


}