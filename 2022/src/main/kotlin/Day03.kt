package advent.of.code

@Files("/Day03.txt")
class Day03 : Puzzle<List<String>, Int> {

    override fun parse(input: List<String>): List<String> = input

    override fun partOne(input: List<String>): Int {
        return input.sumOf { line ->
            val n = line.length / 2
            val left = line.take(n).toSet().toMutableSet()
            val right = line.takeLast(n).toSet()

            left.retainAll(right)

            left.sumOf { it.priority() }
        }
    }

    override fun partTwo(input: List<String>): Int {
        return input.chunked(3).sumOf { lines ->
            val unique = lines.first().toSet().toMutableSet()
            lines.forEach { line -> unique.retainAll(line.toSet()) }
            unique.sumOf { it.priority() }
        }
    }

    private fun Char.priority(): Int = when (this) {
        in 'a'..'z' -> this - 'a' + 1
        in 'A'..'Z' -> this - 'A' + 27
        else -> 0
    }
}

fun main() {
    execute(Day03::class)
}
