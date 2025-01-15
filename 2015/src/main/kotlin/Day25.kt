package advent.of.code

@Files("/Day25.txt")
class Day25 : Puzzle<Pair<Int, Int>, Long> {

    override fun parse(input: List<String>) = input.first().let { line ->
        "[0-9]+".toRegex().findAll(line).map { it.value.toInt() }.toList().let { it[0] to it[1] }
    }

    override fun partOne(input: Pair<Int, Int>): Long {
        var current = 20151125L
        repeat(findN(input.first, input.second) - 1) {
            current = (current * 252533) % 33554393
        }
        return current
    }

    private fun findN(row: Int, column: Int): Int {
        if (row == 1 && column == 1) return 1
        val start = row + (column - 1)
        return (1 until start).reduce { acc, i -> acc + i } + column
    }

    override fun partTwo(input: Pair<Int, Int>): Long = 0
}

fun main() {
    execute(Day25::class)
}
