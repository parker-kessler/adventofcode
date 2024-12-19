package advent.of.code

@Files("/Day06.txt")
class Day06 : Puzzle<List<Pair<Long, Long>>, Int> {

    override fun parse(input: List<String>): List<Pair<Long, Long>> = input.map { line ->
        "[0-9]+".toRegex().findAll(line).map { it.value.toLong() }
    }.let { (top, bottom) -> top.zip(bottom).toList() }

    override fun partOne(input: List<Pair<Long, Long>>) =
        input.map { (time, distance) -> count(time, distance) }.reduce{a, b -> a * b}

    override fun partTwo(input: List<Pair<Long, Long>>): Int {
        val time = input.joinToString(separator = "") { it.first.toString() }.toLong()
        val distance = input.joinToString(separator = "") { it.second.toString() }.toLong()

        return count(time, distance)
    }

    private fun count(time: Long, distance: Long): Int = (0..time).map { it * (time - it) }.count { it > distance }
}

fun main() {
    execute(Day06::class)
}
