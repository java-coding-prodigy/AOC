package aoc2021

import java.util.*

fun main() : Unit{


}



class Day18 : Day(18) {
    override fun part1(input: List<String>): Any {
        return input.reduce { a, b -> a.add(b) }.magnitude()
    }

    fun test() {
        //println("[[[[6,7],[6,7]],[[7,7],[0,7]]],[[[8,7],[7,7]],[[8,8],[8,0]]]]".add("[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]"))
        println(part1("""[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]
[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]
[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]
[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]
[7,[5,[[3,8],[1,4]]]]
[[2,[2,2]],[8,[8,1]]]
[2,9]
[1,[[[9,3],9],[[9,0],[0,7]]]]
[[[5,[7,4]],7],1]
[[[[4,2],2],6],[8,7]]""".split("\n")))
    }

    private fun String.add(other: String): String {
        return "[$this,$other]".reduce()
    }

    private fun String.reduce(): String {
        var str = this
        do {
            while (str.canExplode()) {
                //println(str)
                str = str.explode()

            }
            str = str.split()
        } while (str.canSplit() || str.canExplode())

        return str
    }

    private fun String.canSplit(): Boolean = contains(Regex("\\d{2,11}"))

    private fun String.canExplode(): Boolean {
        var count = 0
        for (ch in this) {
            if (ch == '[') {
                count++
            } else if (ch == ']') {
                count--
                if (count >= 4)
                    return true
            }
        }
        return false
    }

    private fun String.explode(): String {
        val stack = Stack<Int>()
        for ((index, ch) in withIndex()) {
            if (ch == '[') {
                stack.push(index)
            } else if (ch == ']') {
                val start = stack.pop()
                if (stack.size >= 4) {
                    val left = substring(0, start + 1)
                    val current = substring(start + 1, index)
                    val right = substring(index + 1)
                    //println("left: $left current: $current right: $right")
                    val (int1, int2) = current.split(",").filter(String::isNotEmpty)
                        .map(String::toInt)
                    return if (left.matches(Regex("\\[*"))) {
                        "${left.addToLast(int1)}0${right.addToFirst(int2)}".substring(1)
                    } else if (!left.contains(Regex("\\d"))) {
                        "${left.addToLast(int1)}0${right.addToFirst(int2)}".substring(2)
                    } else if (!left.substring(left.length - stack.size + 2)
                            .contains(Regex("\\d"))
                    ) {
                        "${left.substring(0, left.length - 1).addToLast(int1)}0${
                            right.addToFirst(int2)
                        }"
                    } else if (!right.contains(Regex("\\d"))) {
                        "${left.substring(0, left.length - 1).addToLast(int1)}0${
                            right.addToFirst(int2)
                                .substring(if (right.matches(Regex("]+"))) 0 else 1)
                        }"
                    } else if (!right.substring(0, stack.size - 2).contains(Regex("\\d"))) {
                        "${left.addToLast(int1).substringBeforeLast('[')}0${
                            right.addToFirst(int2)
                        }"
                    } else {
                        "${left.addToLast(int1)}${right.addToFirst(int2).substring(1)}"
                    }
                }
            }
        }
        return this
    }


    private fun String.addToLast(num: Int): String {
        val match = Regex("\\d+").findAll(this).lastOrNull()
        return if (match == null) this else this.replaceRange(match.range,
            (match.value.toInt() + num).toString())
    }

    private fun String.addToFirst(num: Int): String {
        val match = Regex("\\d+").findAll(this).firstOrNull()
        return if (match == null) this else this.replaceRange(match.range,
            (match.value.toInt() + num).toString())
    }

    fun String.split(): String {
        return try {
            val match = Regex("\\d{2,11}").findAll(this).first()
            val num = match.value.toInt()
            replaceRange(match.range,
                "[${num / 2},${if (num % 2 == 0) num / 2 else num / 2 + 1}]")
        } catch (e: NoSuchElementException) {
            this
        }
    }


    private fun String.magnitude(): Int {
        if (matches(Regex("\\d")))
            return toInt()
        if (matches(Regex("\\[(\\d),(.*)]"))) {
            val groups = Regex("\\[(\\d),(.*)]").matchEntire(this)!!.groups
            return 3 * groups[1]!!.value.toInt() + 2 * groups[2]!!.value.magnitude()
        }
        if (matches(Regex("\\[(.*),(\\d)]"))) {
            val groups = Regex("\\[(.*),(\\d)]").matchEntire(this)!!.groups
            return 3 * groups[1]!!.value.magnitude() + 2 * groups[2]!!.value.toInt()
        }

        val stack = Stack<Int>()
        for ((idx, ch) in withIndex()) {
            if (ch == '[')
                stack.push(idx)
            else if (ch == ']') {
                try {
                    stack.pop()
                } catch (e: EmptyStackException) {
                    println(this)
                    throw e
                }
                if (stack.size == 1) {
                    val left = substring(1, idx + 1)
                    val right = substring(idx + 2, length - 1)
                    //println("left $left right $right")
                    return 3 * left.magnitude() +
                            2 * right.magnitude()
                }
            }
        }
        throw IllegalStateException()
    }

    override fun part2(input: List<String>): Any {
        return input.flatMap { num -> input.map { num.add(it)} }.maxOf{  it.magnitude() }
    }
}
