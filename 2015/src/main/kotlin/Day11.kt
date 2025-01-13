package advent.of.code

@Files("/Day11.txt")
class Day11 : Puzzle<String, String> {

    private val banned = listOf('i', 'o', 'l').map { it - 'a' }.toSet()

    override fun parse(input: List<String>) = input.first()

    override fun partOne(input: String): String = nextPassword(input)

    override fun partTwo(input: String): String = nextPassword(nextPassword(input))

    private fun nextPassword(password: String): String {
        var digits = password.map { it - 'a' }.next()
        while (!digits.isValid()) {
            digits = digits.next()
        }
        return digits.joinToString("") { (it + 'a'.code).toChar().toString() }
    }

    private fun List<Int>.isValid(): Boolean {
        return zipWithNext().filter { (a, b) -> a == b }.map { it.first }.toSet().size > 1
                && all { !banned.contains(it) }
                && (0 until size - 2).any { this[it] + 1 == this[it + 1] && this[it] + 2 == this[it + 2] }
    }

    private fun List<Int>.next(): List<Int> {
        var carryOver = 1
        return asReversed().map {
            if (it + carryOver > 25) {
                carryOver = 1
                0
            } else {
                val new = it + carryOver
                carryOver = 0
                new
            }
        }.asReversed()
    }
}

fun main() {
    execute(Day11::class)
}
