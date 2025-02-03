package advent.of.code

@Files("/Day09.txt")
class Day09 : Puzzle<String, Long> {

    override fun parse(input: List<String>) = input.first()

    override fun partOne(input: String): Long = decompressLength(input, false)

    override fun partTwo(input: String): Long = decompressLength(input, true)

    private fun decompressLength(input: String, fullDecompression: Boolean): Long {
        return if(input.contains('(')) {
            val before = input.substringBefore('(').length
            val (first, second) = input.substringAfter('(').substringBefore(')').split("x").map { it.toInt() }
            val after = input.substringAfter(')')

            before + if (fullDecompression) {
                decompressLength(after.take(first), true) * second
            } else {
                first * second * 1L
            } + decompressLength(after.drop(first), fullDecompression)
        } else input.length.toLong()
    }
}

fun main() {
    execute(Day09::class)
}
