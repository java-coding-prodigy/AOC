package aoc2015

import kotlin.math.max

fun main() = Day22().run()

class Day22 : Day(22) {

    enum class Spell(
        val cost: Int,
        val dmg: Int = 0,
        val armor: Int = 0,
        val heal: Int = 0,
        val money: Int = 0,
        val turns: Int = 1
    ) {
        MAGIC_MISSILE(cost = 53, dmg = 4), DRAIN(cost = 73, dmg = 2, heal = 2), SHIELD(
            cost = 113, armor = 7, turns = 6
        ),
        POISON(cost = 173, dmg = 3, turns = 6), RECHARGE(cost = 229, money = 101, turns = 5),
    }

    private fun simulate(
        hp: Int,
        mana: Int,
        spent: Int,
        activeSpells: Map<Spell, Int>,
        bossHp: Int,
        bossDmg: Int,
        playerTurn: Boolean,
        part2: Boolean
    ): Int? {
        val spells = activeSpells.keys
        val newHp = hp + spells.sumOf(Spell::heal) - if (part2 && playerTurn) 1 else 0
        if (newHp <= 0) {
            return null
        }
        val armor = spells.sumOf(Spell::armor)
        val newBossHp = bossHp - spells.sumOf(Spell::dmg)
        val newMana = mana + spells.sumOf(Spell::money)
        if (newBossHp <= 0) {
            return spent
        }
        val newActiveSpells = activeSpells.mapValues { (_, v) -> v - 1 }.filterValues { it > 0 }
        if (playerTurn) {
            return Spell.values().filter { !newActiveSpells.contains(it) && newMana >= it.cost }
                .mapNotNull { spell ->
                    simulate(
                        newHp,
                        newMana - spell.cost,
                        spent + spell.cost,
                        newActiveSpells + (spell to spell.turns),
                        newBossHp,
                        bossDmg,
                        false,
                        part2
                    )
                }.minOrNull()
        } else {
            val hpAfterAttack = newHp - max(bossDmg - armor, 1)
            return if (hpAfterAttack > 0) simulate(
                hpAfterAttack, newMana, spent, newActiveSpells, newBossHp, bossDmg, true, part2
            )
            else null
        }
    }

    override fun compute(input: List<String>): Pair<Any?, Any?> {
        val (bossHp, bossDmg) = input.map { it.substringAfterLast(' ').toInt() }
        return simulate(
            50, 500, 0, emptyMap(), bossHp, bossDmg, playerTurn = true, part2 = false
        ) to simulate(
            50, 500, 0, emptyMap(), bossHp, bossDmg, playerTurn = true, part2 = true
        )
    }
}