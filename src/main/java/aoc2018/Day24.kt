package aoc2018

import java.util.stream.IntStream
import kotlin.math.max

fun main() = Day24().run()

class Day24 : Day(24) {

    data class Group(
        var units: Int,
        val hp: Int,
        val weak: Collection<String>,
        val immune: Collection<String>,
        val dmg: Int,
        val type: String,
        val initiative: Int,
    ) : Comparable<Group> {
        val effectivePower get() = units * dmg

        fun damageTo(defender: Group): Int =

            effectivePower * (if (defender.immuneTo(type) || units <= 0) 0 else if (defender.weakTo(
                    type
                )
            ) 2 else 1)


        infix fun attack(defender: Group) {
            val damage = damageTo(defender)
            val kill = damage / defender.hp
            defender.units = max(defender.units - kill, 0)
        }


        fun immuneTo(type: String) = immune.contains(type)

        private fun weakTo(type: String) = weak.contains(type)

        override fun compareTo(other: Group): Int {
            val cmp = effectivePower.compareTo(other.effectivePower)
            return if (cmp == 0) initiative.compareTo(other.initiative) else cmp
        }

    }

    override fun part1(input: List<String>): Any {
        return stimulate(input).filterNot(List<Group>::isEmpty).first().sumOf(Group::units)
    }

    override fun part2(input: List<String>): Any? {
        val check = { boost: Int -> stimulate(input, boost)[0].isNotEmpty() }
        return IntStream.iterate(0, Int::inc).filter(check)
            .map { stimulate(input, it)[0].sumOf(Group::units) }.findFirst().orElseThrow()
    }

    private fun stimulate(input: List<String>, boost: Int = 0): List<List<Group>> {
        val teams = listOf(
            input.subList(1, input.indexOf(""))
                .mapTo(mutableListOf()) { s -> parseGroup(s, boost) },
            input.drop(input.indexOf("") + 2).mapTo(mutableListOf()) { s -> parseGroup(s) })

        while (teams.all(List<Group>::isNotEmpty)) {
            val targetMap = sortedMapOf<Group, Group>(
                Comparator.comparingInt(Group::initiative).reversed()
            )
            teams.forEachIndexed { i, team ->
                val targetsLeft = teams[1 - i].toMutableSet()
                team.sortedDescending(
                ).forEach { attacker ->
                    targetsLeft.maxWithOrNull(
                        Comparator.comparingInt(attacker::damageTo)
                            .thenComparingInt(Group::effectivePower)
                            .thenComparingInt(Group::initiative)
                    )?.also { target ->
                        if (!target.immuneTo(attacker.type)) {
                            targetsLeft.remove(target)
                            targetMap[attacker] = target
                        }
                    }
                }


            }
            if (targetMap.all { (attacker, defender) -> defender.hp > attacker.damageTo(defender) }) {
                return listOf(listOf(), listOf())
            }

            targetMap.forEach { (attacker, defender) ->
                if (attacker.units > 0)
                    attacker.attack(defender)
            }
            teams.forEach { team -> team.removeIf { it.units <= 0 } }
        }
        return teams
    }

    private fun parseGroup(s: String, boost: Int = 0): Group {

        val words = s.split(' ')
        val (units, hp, damage, initiative) = words.mapNotNull(String::toIntOrNull)
        val type = words[words.size - 5]
        val immune =
            s.substringAfter("immune to ", "").substringBefore(';').substringBefore(')').split(", ")
                .filter(String::isNotBlank)
        val weak =
            s.substringAfter("weak to ", "").substringBefore(';').substringBefore(')').split(", ")
                .filter(String::isNotBlank)

        return Group(
            units = units,
            hp = hp,
            weak = weak,
            immune = immune,
            dmg = (damage + boost),
            type = type,
            initiative = initiative,
        )
    }


}
