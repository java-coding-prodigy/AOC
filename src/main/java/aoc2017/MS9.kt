package aoc2017

fun main() = MS9().run()

class MS9 : MS(9) {


    override fun part1(input: List<String>): Any? {
        val stream = input[0]
        var scoreSum = 0
        var idx = 0
        fun traverse(depth: Int) {
            scoreSum += depth
            idx++
            var garbage = false
            while (true) {
                when (stream[idx]) {
                    '!' -> idx++
                    '<' -> garbage = true
                    '>' -> garbage = false
                    '{' -> if (!garbage) {
                        traverse(depth + 1)
                    }
                    '}' -> if (!garbage)
                        break
                }
                idx++
            }
        }
        traverse(1)
        return scoreSum
    }

    override fun part2(input: List<String>): Any? {
        val stream = input[0]
        var garbageCount = 0
        var idx = 0
        var garbage = false
        while(idx < stream.length){
            when(stream[idx]){
                '!' -> idx++
                '<' -> if(garbage) garbageCount++ else garbage = true
                '>' -> garbage = false
                else -> if(garbage) garbageCount++
            }
            idx++
        }
        return garbageCount
    }



}
