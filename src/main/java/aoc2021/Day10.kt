package aoc2021

import java.util.*

fun main() = Day10().part2()/*println(Day10().part2Test("""[({(<(())[]>[[{[]{<()<>>
[(()[<>])]({[<{<<[]>>(
{([(<{}[<>[]}>{[]{[(<()>
(((({<>}<{<{<>}{[]{[]{}
[[<[([]))<([[{}[[()]]]
[{[{({}]{}}([{[{{{}}([]
{<[[]]>}<{[{[{[]{()[[[]
[<(<(<(<{}))><([]([]()
<{([([[(<>()){}]>(<<{{
<{([{{}}[<[[[<>{}]]]>[]]""".split("\n"),288957))*/

class Day10 : Day(10) {
    override fun part1(input: List<String>): Any {
        return input.map {
            getIncorrectBr(it)
        }.reduce { a, b -> a + b }
    }


    private fun getIncorrectBr(line: String): Int {
        val stack: Stack<Char> = Stack()
        for (ch in line) {
            if (isOpenBracket(ch)) {
                stack.push(ch)
            } else if (getClosingBracket(stack.peek()) == ch) {
                stack.pop()
            } else {
                return when (ch) {
                    ')' -> 3
                    ']' -> 57
                    '}' -> 1197
                    '>' -> 25137
                    else -> throw UnsupportedOperationException()
                }
            }
        }
        return 0
    }
    override fun part2(input: List<String>): Any {
        val incomplete = input.map { getIncompleteCode(it) }.filter{ it != 0L}.sorted()
        println(incomplete)
        return incomplete[incomplete.size / 2]

    }
    private fun getIncompleteCode(line: String): Long {
        val stack: Stack<Char> = Stack()
        for (ch in line) {
            if (isOpenBracket(ch)) {
                stack.push(ch)
            } else if (getClosingBracket(stack.peek()) == ch) {
                stack.pop()
            } else {
                return 0
            }
        }
        var result = 0L
        //println(stack)
        for (ch in stack.reversed()) {

            result = 5 * result + ch.score()
        }
        return result
    }

    private fun Char.score()
            : Int = when (this) {
        '(' -> 1
        '[' -> 2
        '{' -> 3
        '<' -> 4
        else -> throw UnsupportedOperationException()
    }

    private fun isOpenBracket(ch: Char): Boolean = ch == '(' || ch == '{' || ch == '<' || ch == '['

    private fun getClosingBracket(ch: Char): Char =
        when (ch) {
            '(' -> ')'
            '[' -> ']'
            '{' -> '}'
            '<' -> '>'
            else -> throw IllegalArgumentException()
        }
}
