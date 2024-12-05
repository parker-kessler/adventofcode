package advent.of.code

import kotlin.math.max

@Files("/Day02.txt")
class Day02 : Puzzle<List<String>, Int> {

    override fun parse(input: List<String>): List<String> = input

    override fun partOne(input: List<String>): Int {
        return input.sumOf { line ->
            if (isValidGame(line)) {
                "Game [0-9]+".toRegex().find(line)?.value?.split(" ")?.get(1)?.toInt() ?: 0
            } else 0
        }
    }

    override fun partTwo(input: List<String>): Int {
        return input.sumOf { line ->
            val map = mutableMapOf<String, Int>()
            "[0-9]+ (red|green|blue)".toRegex().findAll(line).map { it.value }.forEach { cubes ->
                val (count, color) = cubes.split(" ")
                map[color] = max((map[color] ?: 0), count.toInt())
            }
            map.values.reduce { a, b -> a * b }
        }
    }

    private fun isValidGame(line: String): Boolean {
        return "[0-9]+ (red|green|blue)".toRegex().findAll(line).map { it.value }.all { cubes ->
            val (count, color) = cubes.split(" ")
            when (color) {
                "red" -> count.toInt() <= 12
                "green" -> count.toInt() <= 13
                else -> count.toInt() <= 14
            }
        }
    }
}

fun main() {
    execute(Day02::class)
}
