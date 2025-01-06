package advent.of.code

@Files("/Day25.txt")
class Day25 : Puzzle<Pair<List<Array<Int>>, List<Array<Int>>>, Long> {

    override fun parse(input: List<String>): Pair<List<Array<Int>>, List<Array<Int>>> {
        return input
            .filter { it.isNotBlank() }
            .chunked(7)
            .partition { '#' in it[0] }
            .let { (locks, keys) -> locks.map { parseGrid(it) } to keys.map { parseGrid(it) } }
    }

    private fun parseGrid(columns: List<String>) = Array(5) { 0 }.apply {
        columns.forEach { row ->
            row.forEachIndexed { index, s -> if (s == '#') { this[index]++ } }
        }
    }

    override fun partOne(input: Pair<List<Array<Int>>, List<Array<Int>>>): Long {
        val (locks, keys) = input
        return locks.sumOf { lock -> keys.count { key -> checkMatch(lock, key) }.toLong() }
    }

    private fun checkMatch(lock: Array<Int>, key: Array<Int>): Boolean = lock.zip(key).all { (a, b) -> a + b < 8 }

    override fun partTwo(input: Pair<List<Array<Int>>, List<Array<Int>>>): Long = 0
}


fun main() {
    execute(Day25::class)
}
