package advent.of.code

import kotlin.math.pow

@Files("/Day04.txt")
class Day04 : Puzzle<List<String>, Int> {

    override fun parse(input: List<String>): List<String> = input

    override fun partOne(input: List<String>): Int {
        return input.sumOf { line ->
            val (_, inputs) = line.split(": ")
            val (winning, available) = inputs.split(" | ")

            val uniqueWinning = "[0-9]+".toRegex().findAll(winning).map { it.value }.toSet()
            val uniqueAvailable = "[0-9]+".toRegex().findAll(available).map { it.value }.toMutableSet()

            uniqueAvailable.retainAll(uniqueWinning)

            2.0.pow(uniqueAvailable.size - 1).toInt()
        }
    }

    override fun partTwo(input: List<String>): Int {
        val map = mutableMapOf<Int, Int>()
        var maxCard = 1
        input.forEach { line ->
            val (card, inputs) = line.split(": ")
            val (winning, available) = inputs.split(" | ")

            val cardNumber = "[0-9]+".toRegex().find(card)?.value?.toInt() ?: 0
            val uniqueWinning = "[0-9]+".toRegex().findAll(winning).map { it.value }.toSet()
            val uniqueAvailable = "[0-9]+".toRegex().findAll(available).map { it.value }.toMutableSet()

            uniqueAvailable.retainAll(uniqueWinning)

            map[cardNumber] = map[cardNumber] ?: 1
            for (i in 1 .. uniqueAvailable.size) {
                map[cardNumber + i] = (map[cardNumber + i] ?: 1) + (map[cardNumber] ?: 0)
            }
            maxCard = cardNumber
        }
        return map.filter { it.key <= maxCard }.values.sum()
    }
}

fun main() {
    execute(Day04::class)
}
