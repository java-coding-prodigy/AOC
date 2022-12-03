package aoc2015

fun main() = Day6().run()

class Day6 : Day(6) {
    override fun compute(input: List<String>): Pair<Any?, Any?> {
        val lights1 = Array(1000) { BooleanArray(1000) { false } }
        val lights2 = Array(1000) { IntArray(1000) { 0 } }
        for(line in input){
            val command = line.substringBefore(',').substringBeforeLast(' ').trim()
            val (x1, y1, x2 ,y2) = line.substring(command.length).trim().split(",", " through ").map(String::toInt)
            for(x in x1..x2){
                for( y in y1..y2)
                    if(command == "toggle") {
                        lights1[y][x] = !lights1[y][x]
                        lights2[y][x] += 2
                    }else if(command.substringAfter(' ').length == 2){
                        lights1[y][x] = true
                        lights2[y][x]++
                    }else{
                        lights1[y][x] = false
                        if(lights2[y][x] > 0) lights2[y][x]--
                    }
            }
        }
        return lights1.sumOf{ it.count(true::and) } to lights2.sumOf(IntArray::sum)
    }
}