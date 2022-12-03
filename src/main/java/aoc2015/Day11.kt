package aoc2015

import java.lang.StringBuilder

fun main() = Day11().run()

class Day11 : Day(11) {
    override fun compute(input: List<String>): Pair<Any?, Any?> {
        val passwords = sequence {
            var pwd = input[0]
            while(true){
                pwd = pwd.increment()
                //println(pwd)
                yield(pwd)
            }
        }
        return passwords.filter(::check).iterator().let{ it.next() to it.next()}
    }

    private fun check(string: String) : Boolean{
        if(string.contains("[iol]".toRegex()))
            return false
        var pairs = 0
        var paired = '\u0000'
        var tripleRepetition = false
        for((i, c) in string.withIndex()){
            if(i < string.length - 1){
                if(c == string[i + 1] && c != paired){
                    paired = c
                    pairs++
                }
                if(i < string.length - 2 && c + 1 == string[i + 1] && c + 2 == string[i + 2]){
                    tripleRepetition = true
                }
            }
        }
        return pairs >= 2 && tripleRepetition
    }
    private fun String.increment(): String {
        val sb = StringBuilder()
        for(c in reversed()){
            if(c == 'z'){
                sb.append('a')
            }else{
               sb.insert(0,c + 1 )
                break
            }
        }
        sb.insert(0, substring(0, length - sb.length))
        return sb.toString()
    }
}


