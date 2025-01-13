package advent.of.code

import kotlin.math.max

@Files("/Day15.txt")
class Day15 : Puzzle<List<Day15.Ingredient>, Int> {

    companion object {
        const val MAX = 100
        const val CALORIE_MAX = 100
    }

    data class Ingredient(val capacity: Int, val durability: Int, val flavor: Int, val texture: Int, val calories: Int)

    override fun parse(input: List<String>) = input.map { line ->
        val fields = "-?[0-9]+".toRegex().findAll(line).map { it.value.toInt() }.toList()
        Ingredient(fields[0], fields[1], fields[2], fields[3], fields[4])
    }

    override fun partOne(input: List<Ingredient>): Int {
        return permutation(input, Array(input.size) { 0 }.apply { this[0] = MAX }, 0)
    }

    override fun partTwo(input: List<Ingredient>): Int {
        return permutation(input, Array(input.size) { 0 }.apply { this[0] = MAX }, 0, limitCalories = true)
    }

    private fun permutation(ingredients: List<Ingredient>, amounts: Array<Int>, index: Int, limitCalories: Boolean = false): Int {
        if (index == amounts.size - 1) {
            var capacity = 0
            var durability = 0
            var flavor = 0
            var texture = 0
            var calories = 0
            ingredients.zip(amounts).forEach { (ingredient, amount) ->
                capacity += ingredient.capacity * amount
                durability += ingredient.durability * amount
                flavor += ingredient.flavor * amount
                texture += ingredient.texture * amount
                calories += ingredient.calories * amount
            }
            if (capacity < 0 || durability < 0 || flavor < 0 || texture < 0) {
                return 0
            }
            if (limitCalories && calories != CALORIE_MAX) {
                return 0
            }
            return capacity * durability * flavor * texture
        }

        var max = 0
        (1..amounts[index]).forEach { amount ->
            amounts[index] -= amount
            amounts[index + 1] += amount

            max = max(permutation(ingredients, amounts, index + 1, limitCalories), max)

            amounts[index] += amount
            amounts[index + 1] -= amount
        }
        return max
    }
}

fun main() {
    execute(Day15::class)
}
