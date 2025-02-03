package advent.of.code

@Files("/Day03.txt")
class Day03 : Puzzle<List<List<Int>>, Int> {

    override fun parse(input: List<String>): List<List<Int>> = input.map { line ->
        "[0-9]+".toRegex().findAll(line).map { it.value.toInt() }.toList()
    }

    override fun partOne(input: List<List<Int>>): Int {
        return input.count { it.isTriangle() }
    }

    override fun partTwo(input: List<List<Int>>): Int {
        return input.chunked(3).sumOf { chunk ->
            listOf(
                chunk.map { it[0] },
                chunk.map { it[1] },
                chunk.map { it[2] }
            ).count { it.isTriangle() }
        }
    }

    private fun List<Int>.isTriangle(): Boolean = sorted().let { it[0] + it[1] > it[2] }
}

fun main() {
    execute(Day03::class)
}
