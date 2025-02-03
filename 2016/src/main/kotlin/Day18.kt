package advent.of.code

@Files("/Day18.txt")
class Day18 : Puzzle<List<Char>, Int> {

    companion object {
        private const val TRAP = '^'
        private const val SAFE = '.'
    }

    override fun parse(input: List<String>) = input.first().toList()

    override fun partOne(input: List<Char>) = countSafe(input, 40)

    override fun partTwo(input: List<Char>) = countSafe(input, 400_000)

    private fun countSafe(input: List<Char>, rows: Int): Int {
        var sum = 0
        var current = input
        repeat(rows) {
            sum += current.count { it == SAFE }
            current = current.next()
        }
        return sum
    }

    private fun List<Char>.next() = List(size) { i ->
        if (safeGet(i - 1) != safeGet(i + 1)) TRAP else SAFE
    }

    private fun List<Char>.safeGet(index: Int): Char = if (index < 0 || index >= size) SAFE else get(index)

}

fun main() {
    execute(Day18::class)
}
