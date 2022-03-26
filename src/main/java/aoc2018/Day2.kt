package aoc2018


fun main() = Day2().run()

class Day2 : Day(2) {
    override fun part1(input: List<String>): Any {
        return input.count { string ->
            string.occurrences().containsValue(3)
        } * input.count { string -> string.occurrences().containsValue(2) }
    }

    override fun part2(input: List<String>): Any {

        for(line in input){
            for(otherLine in input){
                if(line == otherLine){
                    continue
                }
                var b = false
                var idx = 0
                for((i,ch) in line.withIndex()){
                    if(ch != otherLine[i]){
                        if(!b) {
                            b = true
                            idx = i
                        }else {
                            b = false
                            break
                        }
                    }
                }
                if(b){
                    return line.removeRange(idx, idx + 1)
                }

            }
        }

        TODO("")
    }


    private fun String.occurrences(): Map<Char, Int> {
        val map = mutableMapOf<Char, Int>()
        for (ch in this) {
            map.merge(ch, 1, Int::plus)
        }
        return map
    }
}
