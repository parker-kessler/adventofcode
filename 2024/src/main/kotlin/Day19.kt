package advent.of.code

@Files("/Day19.txt")
class Day19 : Puzzle<Day19.Day19Input, Long> {

    data class Trie(val children: MutableMap<Char, Trie> = mutableMapOf(), var pattern: String? = null)

    data class Day19Input(val pattens: List<String>, val towels: List<String>)

    override fun parse(input: List<String>): Day19Input {
        val (patterns, towels) = input.filter { it.isNotBlank() }.partition { ',' in it }
        return Day19Input("[a-z]+".toRegex().findAll(patterns.first()).map { it.value }.toList(), towels)
    }

    override fun partOne(input: Day19Input): Long = countDistinctCombinations(input).count { it > 0L }.toLong()

    override fun partTwo(input: Day19Input) = countDistinctCombinations(input).sum()

    private fun countDistinctCombinations(input: Day19Input): List<Long> {
        val trie = Trie().apply {
            input.pattens.forEach { pattern ->
                var current = this
                pattern.forEach { char ->
                    current = current.children.computeIfAbsent(char) { Trie() }
                }
                current.pattern = pattern
            }
        }
        return input.towels.map { towel -> analyze(0, trie, towel, mutableMapOf(towel.length to 1L)) }
    }

    private fun analyze(index: Int, root: Trie, towel: String, counts: MutableMap<Int, Long>): Long {
        return counts.getOrPut(index) {
            var current = root
            var sum = 0L
            for (i in index until towel.length) {
                current = current.children[towel[i]] ?: break
                if (current.pattern != null) {
                    sum += analyze(i + 1, root, towel, counts)
                }
            }
            sum
        }
    }

}

fun main() {
    execute(Day19::class)
}
