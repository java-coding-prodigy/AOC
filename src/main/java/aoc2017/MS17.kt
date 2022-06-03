package aoc2017

fun main() = MS17().run()

class MS17 : MS(17) {

    override fun part1(input: List<String>): Any? {
        val step = input[0].toInt()
        val list = mutableListOf(0)
        var idx = 0
        repeat(2017) {
            idx = (idx + step) % list.size + 1
            list.add(idx, it + 1)
        }

        return list[list.indexOf(2017) + 1]
    }

    override fun part2(input: List<String>): Any? {
        val step = input[0].toInt()
        var size = 1
        var idx = 0
        var nextTo0 = 0
        repeat(50_000_000){
            idx = (idx + step) % size + 1
            if(idx == 1){
                nextTo0 = it + 1
            }
            size++
        }
        return nextTo0
    }
}
