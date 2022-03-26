package aoc2018

import java.util.*

fun main() = Day7().run()

class Day7 : Day(7) {

    override fun part1(input: List<String>): Any {
        val needed = needs(input)
        val toDo = needed.values.flatten().toSortedSet()
        toDo.addAll(needed.keys)
        val done = mutableListOf<Char>()
        while (toDo.isNotEmpty()) {
            for (ch in toDo) {
                if (needed.getOrDefault(ch, Collections.emptyList()).all(done::contains)) {
                    done.add(ch)
                    toDo.remove(ch)
                    break
                }
            }
        }
        return done.joinToString("")
    }

    override fun part2(input: List<String>): Any {
        val needed = needs(input)
        val toDo = needed.values.flatten().toSortedSet()
        toDo.addAll(needed.keys)
        val workers = 5
        var time = 0
        val done = mutableSetOf<Char>()

        val work = Array<Pair<Int, Char>?>(workers) { null }
        while (!toDo.isEmpty()) {
            for (ch in toDo) {
                if (needed.getOrDefault(ch, Collections.emptyList()).all(done::contains)) {

                    if (work.any { it?.second == ch }) {
                        continue
                    }

                    val idx = work.indexOf(null)
                    if (idx == -1) {
                        continue
                    }
                    work[idx] = Pair(duration(ch), ch)
                }
            }
            time++
            for ((i, t) in work.withIndex()) {
                work[i] = if (t == null) null else if (t.first == 1) {
                    done.add(t.second)
                    toDo.remove(t.second)
                    null
                } else Pair(t.first - 1, t.second)
            }
            //println(toDo)
        }
        return time
    }

    private fun duration(ch: Char): Int = ch.code - 4

    private fun needs(input: List<String>): Map<Char, Collection<Char>> {
        val map = mutableMapOf<Char, MutableCollection<Char>>()
        for (line in input) {
            val needed = line[5]
            val neededFor = line[36]
            map.computeIfAbsent(neededFor) { mutableSetOf() }.add(needed)
        }

        return map
    }
}
