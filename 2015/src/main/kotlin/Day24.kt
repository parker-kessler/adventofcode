package advent.of.code

@Files("/Day24.txt")
class Day24 : Puzzle<List<Long>, Long> {

    override fun parse(input: List<String>) = input.map { it.toLong() }.sortedDescending()

    override fun partOne(input: List<Long>): Long {
        return mutableListOf<List<Long>>().apply {
            split(input, input.sum() / 3, 0, emptyList(), this)
        }.minOf { it.multiply() }
    }

    override fun partTwo(input: List<Long>): Long {
        return mutableListOf<List<Long>>().apply {
            split(input, input.sum() / 4, 0, emptyList(), this)
        }.minOf { it.multiply() }
    }

    private fun split(
        packages: List<Long>,
        target: Long,
        index: Int,
        sack: List<Long>,
        answers: MutableList<List<Long>>
    ) {
        if (index == packages.size) {
            sack.sum().takeIf { it == target }?.let { answers.add(sack) }
            return
        }

        if (answers.isEmpty() || sack.size < answers.first().size) {
            split(packages, target, index + 1, sack + packages[index], answers)
            split(packages, target, index + 1, sack, answers)
        }
    }

    private fun List<Long>.multiply() = reduce { acc, num -> acc * num }
}

fun main() {
    execute(Day24::class)
}
