package aoc2017

fun main() = MS19().run()

class MS19 : MS(19) {
    override fun part1(input: List<String>): Any? {
        var dir = 'n'
        val letters = StringBuilder()
        var y = 0
        var x = input[y].indexOf('|')
        fun move(dir: Char) {
            when (dir) {
                'n' -> y++
                'e' -> x++
                's' -> y--
                'w' -> x--
            }
        }

        while (true) {
            when (input[y].getOrElse(x){' '}) {
                '|', '-' -> move(dir)
                '+' -> {
                    val n = input.getOrElse(y + 1) { "" }.getOrElse(x) { ' ' }
                    val e = input.getOrElse(y) { "" }.getOrElse(x + 1) { ' ' }
                    val s = input.getOrElse(y - 1) { "" }.getOrElse(x) { ' ' }
                    val w = input.getOrElse(y) { "" }.getOrElse(x - 1) { ' ' }
                    when {
                        n != ' ' && dir != 'n' && dir != 's' -> dir = 'n'
                        e != ' ' && dir != 'e' && dir != 'w' -> dir = 'e'
                        s != ' ' && dir != 's' && dir != 'n' -> dir = 's'
                        w != ' ' && dir != 'w' && dir != 'e' -> dir = 'w'
                    }
                    move(dir)
                }
                ' ' -> break
                else -> {
                    if (!letters.contains(input[y][x]))
                        letters.append(input[y][x])
                    move(dir)
                }
            }
        }
        return letters
    }

    override fun part2(input: List<String>): Any? {
        var dir = 'n'
        var moves = 0
        var y = 0
        var x = input[y].indexOf('|')
        fun move(dir: Char) {
            when (dir) {
                'n' -> y++
                'e' -> x++
                's' -> y--
                'w' -> x--
            }
        }

        while (true) {
            when (input[y].getOrElse(x){' '}) {
                '+' -> {
                    val n = input.getOrElse(y + 1) { "" }.getOrElse(x) { ' ' }
                    val e = input.getOrElse(y) { "" }.getOrElse(x + 1) { ' ' }
                    val s = input.getOrElse(y - 1) { "" }.getOrElse(x) { ' ' }
                    val w = input.getOrElse(y) { "" }.getOrElse(x - 1) { ' ' }
                    when {
                        n != ' ' && dir != 'n' && dir != 's' -> dir = 'n'
                        e != ' ' && dir != 'e' && dir != 'w' -> dir = 'e'
                        s != ' ' && dir != 's' && dir != 'n' -> dir = 's'
                        w != ' ' && dir != 'w' && dir != 'e' -> dir = 'w'
                    }
                }
                ' ' -> break
            }
            move(dir)
            moves++
        }
        return moves
    }
}
