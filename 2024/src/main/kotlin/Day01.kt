package advent.of.code

import kotlin.math.abs

@Files("/Day01.txt")
class Day01 : Puzzle<Pair<MutableList<Int>, MutableList<Int>>, Int> {

    override fun parse(input: List<String>): Pair<MutableList<Int>, MutableList<Int>> {
        val left = mutableListOf<Int>()
        val right = mutableListOf<Int>()
        input.forEach {
            val parts = it.split(" ")
            left.add(parts.first().toInt())
            right.add(parts.last().toInt())
        }
        return Pair(left, right)
    }

    override fun partOne(input: Pair<MutableList<Int>, MutableList<Int>>): Int {
        return input.let { (left, right) -> left.sorted().zip(right.sorted()).sumOf { (l, r) -> abs(l - r) }}
    }

    override fun partTwo(input: Pair<MutableList<Int>, MutableList<Int>>): Int {
        val (left, right) = input
        val rightCount = right.groupingBy { it }.eachCount()

        return left.sumOf { it * (rightCount[it] ?: 0) }
    }
}

fun main() {
    execute(Day01::class)
}
