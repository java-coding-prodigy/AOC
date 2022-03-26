package aoc2018

fun main() = Day13().run()

class Day13 : Day(13) {
    override fun part1(input: List<String>): Any {

        val grid = input.map(String::toCharArray)
        val carts = input.flatMapIndexed { y, line ->
            line.mapIndexedNotNull { x, ch ->
                when (ch) {
                    'v' -> {
                        grid[y][x] = '|'
                        Cart(x, y, 'N')
                    }
                    '>' -> {
                        grid[y][x] = '-'
                        Cart(x, y, 'E')
                    }
                    '^' -> {
                        grid[y][x] = '|'
                        Cart(x, y, 'S')
                    }
                    '<' -> {
                        grid[y][x] = '-'
                        Cart(x, y, 'W')
                    }
                    else -> null
                }
            }
        }
        while (true) {
            for (cart in carts) {
                if (carts.any { c2 -> cart !== c2 && cart.x == c2.x && cart.y == c2.y }) {
                    return cart
                }
                cart.move()
                cart.changeDir(grid[cart.y][cart.x])
            }
        }

    }

    override fun part2(input: List<String>): Any {
        val grid = input.map(String::toCharArray)
        var carts = input.flatMapIndexed { y, line ->
            line.mapIndexedNotNull { x, ch ->
                when (ch) {
                    'v' -> {
                        grid[y][x] = '|'
                        Cart(x, y, 'N')
                    }
                    '>' -> {
                        grid[y][x] = '-'
                        Cart(x, y, 'E')
                    }
                    '^' -> {
                        grid[y][x] = '|'
                        Cart(x, y, 'S')
                    }
                    '<' -> {
                        grid[y][x] = '-'
                        Cart(x, y, 'W')
                    }
                    else -> null
                }
            }
        }.sortedWith(comparator)
        while (carts.size > 1) {

            for (cart in carts) {


                if (cart.collided) {
                    continue
                }
                carts.collideCheck(cart)
                if (cart.collided) {
                    continue
                }
                cart.move()
                cart.changeDir(grid[cart.y][cart.x])

            }
            carts.forEach { carts.collideCheck(it) }
            carts = carts.filter { !it.collided }
                .sortedWith(comparator)
        }
        return carts.first()
    }

    private fun Iterable<Cart>.collideCheck(cart: Cart) {

        filter { c2 -> cart !== c2 && cart.x == c2.x && cart.y == c2.y && !c2.collided }
            .forEach {
                cart.collided = true
                it.collided = true
            }

    }

    private val comparator = Comparator.comparing(Cart::y).thenComparing(Cart::x)

    private data class Cart(
        var x: Int,
        var y: Int,
        var dir: Char,
        var intersections: Int = 0,
        var collided: Boolean = false

    ) {
        fun move() {
            when (dir) {
                'N' -> y++
                'E' -> x++
                'S' -> y--
                'W' -> x--
            }
        }


        fun changeDir(ch: Char) {
            dir = when (ch) {
                '+' ->
                    when (intersections++) {

                        0 -> when (dir) {
                            'N' -> 'E'
                            'E' -> 'S'
                            'S' -> 'W'
                            'W' -> 'N'
                            else -> throw IllegalStateException()
                        }
                        1 -> dir
                        2 -> when (dir) {
                            'N' -> 'W'
                            'E' -> 'N'
                            'S' -> 'E'
                            'W' -> 'S'
                            else -> throw IllegalStateException()
                        }
                        else -> throw IllegalStateException()

                    }

                '/' -> when (dir) {
                    'N' -> 'W'
                    'E' -> 'S'
                    'S' -> 'E'
                    'W' -> 'N'
                    else -> throw IllegalStateException()
                }
                '\\' ->
                    when (dir) {
                        'N' -> 'E'
                        'E' -> 'N'
                        'S' -> 'W'
                        'W' -> 'S'
                        else -> throw IllegalStateException()
                    }
                else -> dir
            }
            intersections %= 3
        }


        override fun toString() = "$x,$y"

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Cart) return false

            if (x != other.x) return false
            if (y != other.y) return false

            return true
        }

        override fun hashCode(): Int {
            var result = x
            result = 31 * result + y
            return result
        }
    }


}
