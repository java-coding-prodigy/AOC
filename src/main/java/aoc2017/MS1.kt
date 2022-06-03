package aoc2017

fun main() = MS1().run()

class MS1 : MS(1) {
    override fun part1(input: List<String>): Any = input[0].map(Char::digitToInt).captcha(1)

    private fun List<Int>.captcha(steps: Int) = filterIndexed { i, dig -> dig == this[(i + steps) % size] }
        .sum()

    override fun part2(input: List<String>): Any =
        input[0].map(Char::digitToInt).captcha(input[0].length / 2)
}
