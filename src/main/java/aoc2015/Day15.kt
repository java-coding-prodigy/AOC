package aoc2015

import kotlin.math.max

fun main() = Day15().run()

class Day15 : Day(15) {

    data class Ingredient(
        val capacity: Int, val durability: Int, val flavor: Int, val texture: Int, val calories: Int
    ) {
        fun teaspoons(n: Int) =
            Ingredient(capacity * n, durability * n, flavor * n, texture * n, calories * n)

        operator fun plus(other: Ingredient) = Ingredient(
            capacity + other.capacity,
            durability + other.durability,
            flavor + other.flavor,
            texture + other.texture,
            calories + other.calories
        )

        operator fun component6() = max(capacity, 0) * max(durability, 0) * max(flavor, 0) * max(
            texture,
            0
        )

    }

    private fun cookie(ingredients: List<Ingredient>, counts: List<Int>): Ingredient =
        ingredients.mapIndexed { i, ing -> ing.teaspoons(counts[i]) }
            .reduce(Ingredient::plus)


    override fun compute(input: List<String>): Pair<Any?, Any?> {
        val ingredients =
            input.map {
                val (capacity, durability, flavor, texture, calories) = it.split(' ', ',')
                    .mapNotNull(String::toIntOrNull)
                Ingredient(capacity, durability, flavor, texture, calories)
            }
        var max1: Int = Int.MIN_VALUE
        var max2: Int = Int.MIN_VALUE
        for (i in 0..100) {
            for (j in 0..100 - i) {
                for (k in 0..100 - i - j) {
                    val (_, _, _, _, calories, score) = cookie(
                        ingredients,
                        listOf(i, j, k, 100 - i - j - k)
                    )
                    max1 = max(score, max1)
                    if (calories == 500) {
                        max2 = max(score, max2)
                    }
                }
            }
        }
        return max1 to max2
    }
}