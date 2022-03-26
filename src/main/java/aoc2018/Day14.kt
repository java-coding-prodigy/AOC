package aoc2018



fun main() = Day14().run()

class Day14 : Day(14) {
    override fun part1(input: List<String>): Any {
        val count = input[0].toInt()
        var i1 = 0
        var i2 = 1
        val recipes = mutableListOf(3, 7)
        val next10 = StringBuilder("37")
        repeat(8) { next10.append('\uFFFF') }

        while (recipes.size - 10 <= count) {
            val c1 = recipes[i1]
            val c2 = recipes[i2]
            val sum = c1 + c2
            if (sum >= 10) {
                val n = sum / 10
                recipes.add(n)
                if(recipes.size - 10 > count){
                    break
                }
                next10.append(n).deleteAt(0)
            }
            val n = sum % 10
            recipes.add(n)
            next10.append(n).deleteAt(0)
            i1 = (i1 + c1 + 1) % recipes.size
            i2 = (i2 + c2 + 1) % recipes.size
        }
        return next10
    }

    override fun part2(input: List<String>): Any {
        val recipe = input[0]
        var i1 = 0
        var i2 = 1
        val recipes = mutableListOf(3, 7)
        val currentRecipe = StringBuilder("37")
        repeat(recipe.length - 2) { currentRecipe.insert(0, '\uFFFF') }

        while (currentRecipe.toString() != recipe) {
            val c1 = recipes[i1]
            val c2 = recipes[i2]
            val sum = c1 + c2
            if (sum >= 10) {
                val n = sum / 10
                recipes.add(n)
                currentRecipe.append(n).deleteAt(0)
                if (currentRecipe.toString() == recipe) {
                    break
                }
            }
            val n = sum % 10
            recipes.add(n)
            currentRecipe.append(n).deleteAt(0)
            i1 = (i1 + c1 + 1) % recipes.size
            i2 = (i2 + c2 + 1) % recipes.size
        }
        return recipes.size - recipe.length
    }

}
