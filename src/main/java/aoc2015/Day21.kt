package aoc2015

import kotlin.math.ceil
import kotlin.math.max

fun main() = Day21().run()

class Day21 : Day(21) {

    data class Player(var hp: Int, val dmg: Int, val armor: Int) {

        constructor(stats: List<Int>) : this(stats[0], stats[1], stats[2])

        infix fun vs(other: Player): Boolean =
            ceil(
                hp.toDouble() / max(
                    other.dmg - armor,
                    1
                )
            ) >= (ceil(other.hp.toDouble() / max(dmg - other.armor, 1)))
    }

    data class Accessory(val cost: Int, val dmg: Int, val armor: Int) {
        constructor(stats: List<Int>) : this(stats[0], stats[1], stats[2])

        operator fun plus(other: Accessory) =
            Accessory(cost + other.cost, dmg + other.dmg, armor + other.armor)
    }

    override fun compute(input: List<String>): Pair<Any?, Any?> {
        val boss = Player(input.map { it.substringAfterLast(' ').toInt() })
        val (wins, losses) = accessories().partition { accessory ->
            Player(
                100,
                accessory.dmg,
                accessory.armor
            ) vs boss
        }
        return wins.minOf(Accessory::cost) to losses.maxOf(Accessory::cost)
    }

    private fun accessories(): List<Accessory> {
        val (weapons, armors, rings) = """
                Weapons:    Cost  Damage  Armor
                Dagger        8     4       0
                Shortsword   10     5       0
                Warhammer    25     6       0
                Longsword    40     7       0
                Greataxe     74     8       0
    
                Armor:      Cost  Damage  Armor
                Leather      13     0       1
                Chainmail    31     0       2
                Splintmail   53     0       3
                Bandedmail   75     0       4
                Platemail   102     0       5
    
                Rings:      Cost  Damage  Armor
                Damage +1    25     1       0
                Damage +2    50     2       0
                Damage +3   100     3       0
                Defense +1   20     0       1
                Defense +2   40     0       2
                Defense +3   80     0       3
            """.trimIndent().split("\n\n").map { data ->
            data.split('\n').drop(1).map {
                Accessory(
                    it.trim().substringAfter(' ').split(' ').map(String::trim)
                        .filter(String::isNotBlank)
                        .map(String::toInt).takeLast(3)
                )
            }
        }
        val allRings =
            rings + rings.flatMap {
                rings.filterNot(it::equals).map(it::plus)
            }.distinct() + Accessory(0, 0, 0)
        return weapons.flatMap { weapon ->
            (armors + Accessory(0, 0, 0)).flatMap { armor -> allRings.map(armor::plus) }
                .map(weapon::plus)
        }
    }
}