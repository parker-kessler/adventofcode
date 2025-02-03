package advent.of.code

@Files("/Day06.txt")
class Day06 : Puzzle<List<String>, String> {

    override fun parse(input: List<String>): List<String> = input

    override fun partOne(input: List<String>): String {
        return indexCharCounts(input).joinToString("") { counts -> counts.maxBy { it.value }.key.toString() }
    }

    override fun partTwo(input: List<String>): String {
        return indexCharCounts(input).joinToString("") { counts -> counts.minBy { it.value }.key.toString() }
    }

    private fun indexCharCounts(input: List<String>): List<Map<Char, Int>> {
        val byIndex = mutableMapOf<Int, MutableMap<Char, Int>>()
        input.forEach { line ->
            line.forEachIndexed { index, letter ->
                byIndex.computeIfAbsent(index) { mutableMapOf() }.compute(letter) { _, v -> (v ?: 0) + 1 }
            }
        }
        return byIndex.values.toList()
    }
}

fun main() {
    execute(Day06::class)
}
