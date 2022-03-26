package aoc2018

fun main() = Day9().run()

class Day9 : Day(9) {

    override fun part1(input: List<String>): Any {
        val (players, lastScore) = input[0].split(' ').filter { it.matches("\\d+".toRegex()) }
            .map(String::toInt)
        return play(players, lastScore)
    }


    override fun part2(input: List<String>): Any {
        val (players, lastScore) = input[0].split(' ').filter { it.matches("\\d+".toRegex()) }
            .map(String::toInt)
        return play(players, lastScore * 100)
    }

    private class Node(var data: Int = 0, var next: Node? = null, var prev: Node? = null)

    private fun play(players: Int, lastScore: Int): Long {
        val scores = Array(players) { 0L }
        var head = Node()
        head.next = head
        head.prev = head
        for (marble in 1..lastScore) {


            if (marble % 23 == 0) {
                for (i in 1..7) {
                    head = head.prev!!
                }
                val player = (marble - 1) % players
                scores[player] = scores[player] + marble + head.data
                head.prev!!.next = head.next
                head.next!!.prev = head.prev
                head = head.next!!

            } else {
                val newNode = Node(marble, head.next!!.next, head.next)

                head.next!!.next!!.prev = newNode
                head.next!!.next = newNode
                head = newNode
            }

        }
        return scores.maxOrNull() ?: 0L
    }
}
