package advent.of.code

import kotlin.math.max
import kotlin.math.min

@Files("/Day03.txt")
class Day03 : Puzzle<List<String>, Int> {

    override fun parse(input: List<String>): List<String> = input

    override fun partOne(input: List<String>): Int {
        var index = -1

        return input.sumOf { line ->
            index++
            "[0-9]+".toRegex().findAll(line).sumOf { match ->
                var value = 0
                outer@ for (i in max(index - 1, 0).. min(index + 1, input.size - 1)) {
                    for (j in max(match.range.first - 1, 0)..min(match.range.last + 1, line.length - 1)) {
                        if (!input[i][j].isDigit() && input[i][j] != '.') {
                            value = match.value.toInt()
                            break@outer
                        }
                    }
                }
                value
            }
        }
    }

    override fun partTwo(input: List<String>): Int {
        val map = mutableMapOf<Pair<Int, Int>, MutableList<Int>>()

        input.forEachIndexed { index, line ->
            "[0-9]+".toRegex().findAll(line).forEach { match ->
                outer@ for (i in max(index - 1, 0).. min(index + 1, input.size - 1)) {
                    for (j in max(match.range.first - 1, 0)..min(match.range.last + 1, line.length - 1)) {
                        if (input[i][j] == '*') {
                            map.computeIfAbsent(i to j) { mutableListOf() }.add(match.value.toInt())
                            break@outer
                        }
                    }
                }
            }
        }

        return map.values.filter { it.size == 2 }.sumOf { it.reduce { a, b -> a * b } }
    }
}

fun main() {
    execute(Day03::class)
}
