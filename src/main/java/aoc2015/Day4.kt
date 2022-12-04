package aoc2015

import java.math.BigInteger
import java.security.MessageDigest

fun main() = Day4().run()

class Day4 : Day(4) {
    override fun compute(input: List<String>): Pair<Any?, Any?> {
        val md5 = MessageDigest.getInstance("MD5")
        var p1 = -1
        var p2 = -1
        repeat(10_000_000){
            i ->
            val resultLength = BigInteger(md5.digest((input[0] + i).toByteArray())).toString(16).length
            if(resultLength == 32 - 5 && p1 == -1){
                p1 = i
            }
            if(resultLength == 32 - 6) {
                p2 = i
                return p1 to p2
            }
        }
        return p1 to p2
    }
}