package advent.of.code

@Files("/Day06.txt")
class Day06 : Puzzle<String, Int> {

    override fun parse(input: List<String>): String = input.first()

    override fun partOne(input: String): Int = input.detect(4)

    override fun partTwo(input: String): Int = input.detect(14)

    private fun String.detect(size: Int): Int = indexOf(windowed(size).first { it.toSet().size == size }) + size
}

fun main() {
    execute(Day06::class)
}
