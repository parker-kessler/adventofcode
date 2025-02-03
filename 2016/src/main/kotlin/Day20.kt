package advent.of.code

import kotlin.math.max

@Files("/Day20.txt")
class Day20 : Puzzle<List<LongRange>, Long> {

    override fun parse(input: List<String>) = input.map { line ->
        val (start, end) = line.split("-").map { it.toLong() }
        LongRange(start, end)
    }

    override fun partOne(input: List<LongRange>): Long {
        return mergeRanges(input).first().last + 1
    }

    override fun partTwo(input: List<LongRange>): Long {
        return 4294967295 - mergeRanges(input).sumOf { range -> range.last - range.first + 1 } + 1
    }

    private fun mergeRanges(input: List<LongRange>): List<LongRange> {
        return mutableListOf<LongRange>().apply {
            input.sortedBy { it.first }.forEach { next ->
                if (isEmpty() || last().last + 1 < next.first) {
                    add(next)
                } else {
                    this[lastIndex] = LongRange(last().first, max(next.last, last().last))
                }
            }
        }
    }
}

fun main() {
    execute(Day20::class)
}
