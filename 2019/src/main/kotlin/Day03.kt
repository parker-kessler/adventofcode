package advent.of.code

import kotlin.math.abs

@Files("/Day03.txt")
class Day03 : Puzzle<Pair<List<String>, List<String>>, Int> {

    override fun parse(input: List<String>): Pair<List<String>, List<String>> {
        return input.map { it.split(",") }.let { (first, second) -> first to second }
    }

    override fun partOne(input: Pair<List<String>, List<String>>): Int {
        val pathOne = getPath(input.first).keys
        val pathTwo = getPath(input.second).keys

        pathTwo.retainAll { pathOne.contains(it) }

        return pathTwo.minOf { abs(it.first) + abs(it.second) }
    }

    override fun partTwo(input: Pair<List<String>, List<String>>): Int {
        val pathOne = getPath(input.first)
        val pathTwo = getPath(input.second)

        pathTwo.keys.retainAll { pathOne.keys.contains(it) }

        return pathTwo.entries.minOf { entry -> pathOne[entry.key]?.let { it + entry.value } ?: Int.MAX_VALUE }
    }

    private fun getPath(lines: List<String>): MutableMap<Pair<Int, Int>, Int> {
        val path = mutableMapOf<Pair<Int, Int>, Int>()
        var index = 0
        var current = 0 to 0
        lines.forEach { line ->
            val direction = when(line[0]) {
                'U' -> 0 to 1
                'D' -> 0 to -1
                'L' -> -1 to 0
                else -> 1 to 0
            }
            (1..line.drop(1).toInt()).forEach { _ ->
                current = current.first + direction.first to current.second + direction.second
                path[current] = ++index
            }
        }
        return path
    }
}

fun main() {
    execute(Day03::class)
}
