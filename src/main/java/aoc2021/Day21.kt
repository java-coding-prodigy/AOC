package aoc2021

import kotlin.math.max

fun main() = Day21().part2()

class Day21 : Day(21) {

    override fun part1(input: List<String>): Any {
        val game = Game(input)
        game.play()
        println(game.loser!!.score)
        println(game.steps)
        return game.loser!!.score * game.steps
    }

    class Game(input: List<String>) {
        private val player1: Player
        private val player2: Player
        private var winner: Player? = null
        var loser: Player? = null
        var steps: Int = 0
        private val dice: Dice = Dice()

        init {
            player1 = Player(input[0].last().digitToInt())
            player2 = Player(input[1].last().digitToInt())
        }

        fun play() {
            while (true) {
                steps += 3
                player1.move(dice.next())
                if (player1.hasWon()) {
                    winner = player1
                    loser = player2
                    break
                }
                steps += 3
                player2.move(dice.next())
                if (player2.hasWon()) {
                    winner = player2
                    loser = player1
                    break
                }
            }
        }
    }

    class Dice {
        private var num = 1
        fun next(): Int {
            if (num == 100) {
                num = 0
            }
            return num++ + num++ + num++
        }
    }

    class Player(private var position: Int, var score: Int) {

        constructor(position: Int) : this(position, 0)

        fun move(steps: Int): Player {
            position += steps
            if (position > 10) {
                position = (position - 1) % 10 + 1
            }
            score += position
            return this
        }

        constructor(previousPlayer: Player) : this(previousPlayer.position, previousPlayer.score)

        fun hasWon(): Boolean = score >= 1000

        fun hasWon2(): Boolean = score >= 21
    }


    open class Universe(var current: Player, var next: Player) {


        var winner: Player? = null

        fun roll(): Collection<Universe> {
            return if (isOver()) setOf(this) else next().map { die ->
                Universe(Player(next),
                    Player(current).move(die))
            }
        }

        fun isOver(): Boolean = current.hasWon2() || next.hasWon2()
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Universe) return false

            if (current != other.current) return false
            if (next != other.next) return false

            return true
        }

        override fun hashCode(): Int {
            var result = current.hashCode()
            result = 31 * result + next.hashCode()
            return result
        }

        // constructor(input: List<String>) : this(Player(input[0].last().digitToInt()),            Player(input[1].last().digitToInt()))

    }


//    private fun dfs(universe: Universe, p1: Player): Map<Boolean, Long> {
//        if (universe.isOver()) {
//            return mapOf(Pair(p1 == universe.winner, 1L), Pair(p1 != universe.winner, 0L))
//        }
//        return universe.roll().map { dfs(it, p1) }.reduce { map1, map2 ->
//            mapOf(Pair(true, map1[true]!! + map2[true]!!),
//                Pair(false, map1[false]!! + map2[false]!!))
//        }
//    }

    override fun part2(input: List<String>): Any {
        var p1Wins = 0L
        var p2Wins = 0L
        var map: MutableMap<Pair<Player, Player>, Long> = HashMap()
        map[Pair(Player(input[0].last().digitToInt()), Player(input[1].last().digitToInt()))] = 1
        var bool = true
        do {
            val roll = next().groupBy { it }.mapValues { (_, v) -> v.size }
            if (bool) {
                val newMap: MutableMap<Pair<Player, Player>, Long> = HashMap()
                for ((key, occ) in map.entries) {
                    val p1 = key.first
                    val p2 = key.second

                    val mapThingy = roll.mapKeys { Player(p1).move(it.key) }
                    mapThingy.filter { it.key.hasWon2() }.forEach { (_, v) ->
                        p1Wins += occ * v
                    }
                    mapThingy.filter { !it.key.hasWon2() }.forEach { (k, v) ->
                        val pair = Pair(k, p2)
                        newMap[pair] = (if (newMap.contains(pair)) newMap[pair]!! else 0) + v * occ
                    }
                }
                map = newMap
            } else {
                val newMap: MutableMap<Pair<Player, Player>, Long> = HashMap()
                for ((key, occ) in map.entries) {
                    val p1 = key.first
                    val p2 = key.second
                    val mapThingy = roll.mapKeys { Player(p2).move(it.key) }
                    mapThingy.filter { it.key.hasWon2() }.forEach { (_, v) ->
                        p2Wins += occ * v
                    }
                    mapThingy.filter { !it.key.hasWon2() }.forEach { (k, v) ->
                        val pair = Pair(p1, k)
                        newMap[pair] = (if (newMap.contains(pair)) newMap[pair]!! else 0) + v * occ
                    }
                }
                map = newMap
            }
            bool = !bool
            println("completed iteration")
        } while (map.isNotEmpty())
        println("p1 wins: $p1Wins p2 wins $p2Wins")
        return max(p1Wins, p2Wins)
    }
}

fun next() = semiNext(0).flatMap(::semiNext).flatMap(::semiNext)/*.toSet()*/

private fun semiNext(value: Int) = listOf(value + 1, value + 2, value + 3)
