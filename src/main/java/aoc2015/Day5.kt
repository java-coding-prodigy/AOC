package aoc2015

fun main() = Day5().run()

class Day5 : Day(5) {
    override fun compute(input: List<String>): Pair<Any?, Any?> {
        return input.count(::nice1) to input.stream().filter(::nice2).peek(::println).count()
    }

    private fun nice2(string: String): Boolean {
        var pairRepetition = false
        var skipRepetition = false
        val pairs = mutableSetOf<String>()
        var lastPair = ""
        val pairsBeforeLast = mutableSetOf<String>()
        for((i, c) in string.withIndex()){
            if(i != string.length - 1){
                val pair = "" + c + string[i + 1]
                if(!pairs.add(pair) && (lastPair != pair || !pairsBeforeLast.add(lastPair))) {
                    pairRepetition = true
                }
                lastPair = pair
                if(i != string.length - 2 && c == string[i + 2]){
                    skipRepetition = true
                }
            }
        }
        return pairRepetition && skipRepetition
    }

    private fun nice1(string: String) : Boolean {
        var vowels = 0
        var repetition = false
        for((i, c )in string.withIndex()){
            if(c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u')
                vowels++
            if(i != string.length - 1) {
                if(c == string[i + 1])
                    repetition = true
                val sub = "" + c + string[i + 1]
                if(sub == "ab" || sub == "cd" || sub == "pq" || sub == "xy")
                    return false
            }
        }
        return vowels >= 3 && repetition
    }
}