package aoc2015

fun main() = Day10().run()

class Day10: Day(10) {
    override fun compute(input: List<String>): Pair<Any?, Any?> {
        var word = input[0]
        repeat(40){
           word = newWord(word)
        }
        val p1 = word.length
        repeat(10){
           word = newWord(word)
        }
        return p1 to word.length
    }

    private fun newWord(word: String) : String{
        val newWord = StringBuilder()
        var lastChar: Char = word[0]
        var count = 0
        for (c in word) {
            if (c == lastChar) {
                count++
            } else {
                newWord.append(count).append(lastChar)
                lastChar = c
                count = 1
            }
        }
       return newWord.append(count).append(lastChar).toString()
    }
}