package aoc2015

fun main() {
    Day1().run()
}

class Day1 : Day(1) {

    override fun compute(input: List<String>): Pair<Any?, Any?> {
        var p1 = 0
        var p2 = -1
        input[0].forEachIndexed{ i, ch ->
            p1 += if(ch == '(') 1 else -1
            if(p1 == -1 && p2 == -1)
                p2 = i + 1
        }
        return Pair(p1, p2)
    }
}