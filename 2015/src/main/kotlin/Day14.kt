package advent.of.code

import kotlin.math.min

@Files("/Day14.txt")
class Day14 : Puzzle<List<Triple<Int, Int, Int>>, Int> {

    companion object {
        private const val SECONDS = 2503
    }

    override fun parse(input: List<String>) = input.map { line ->
        val reindeer = "[0-9]+".toRegex().findAll(line).map { it.value.toInt() }.toList()
        Triple(reindeer[0], reindeer[1], reindeer[2])
    }

    override fun partOne(input: List<Triple<Int, Int, Int>>): Int {
        return input.maxOf { (speed, amount, rest) ->
            val interval = amount + rest
            speed * ((SECONDS / interval) * amount + min(SECONDS % interval, amount))
        }
    }

    override fun partTwo(input: List<Triple<Int, Int, Int>>): Int {
        val locations = input.map { (speed, amount, rest) -> getLocations(speed, amount, rest) }

        val scores = List(input.size) { index -> index to 0 }.toMap().toMutableMap()
        repeat(SECONDS) { second ->
            val max = locations.maxOf { it[second] }
            locations.forEachIndexed { index, location ->
                if (max == location[second]) {
                    scores.computeIfPresent(index) { _, value -> value + 1 }
                }
            }
        }
        return scores.maxOf { it.value }
    }

    private fun getLocations(speed: Int, amount: Int, rest: Int): Array<Int> {
        return Array(SECONDS) { 0 }.apply {
            val interval = amount + rest
            repeat(size) { index ->
                this[index] = if ((index % interval) < amount) {
                    speed + (if (index == 0) 0 else this[index - 1])
                } else {
                    this[index - 1]
                }
            }
        }
    }
}

fun main() {
    execute(Day14::class)
}
