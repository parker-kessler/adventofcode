package advent.of.code

import kotlin.math.abs

@Input("/Day02.txt")
class Day02 : Puzzle<List<List<Int>>, Int> {

    override fun parse(input: List<String>): List<List<Int>> {
        return input.map { line -> line.split(" ").map { it.toInt() } }
    }

    override fun partOne(input: List<List<Int>>): Int = input.count { isSafeReport(it) }

    override fun partTwo(input: List<List<Int>>): Int = input.count { row ->
        isSafeReport(row) || row.indices.any { i ->
            isSafeReport(row.subList(0, i) + row.subList(i + 1, row.size))
        }
    }

    private fun isSafeReport(list: List<Int>): Boolean {
        var increasing = true
        var decreasing = true
        var difference = true

        var previous = list[0]
        for (i in 1 until list.size) {
            decreasing = decreasing && previous > list[i]
            increasing = increasing && previous < list[i]
            difference = difference && abs(list[i] - previous) < 4

            previous = list[i]
        }

        return (increasing || decreasing) && difference
    }
}

fun main() {
    execute(Day02::class)
}
