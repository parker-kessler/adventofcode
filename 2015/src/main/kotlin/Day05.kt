package advent.of.code

@Files("/Day05.txt")
class Day05 : Puzzle<List<String>, Int> {

    override fun parse(input: List<String>): List<String> = input

    override fun partOne(input: List<String>): Int {
        val duplicates = ('a'..'z').map { "$it$it" }
        val illegal = listOf("ab", "cd", "pq", "xy")
        return input.count { word ->
            word.count { "aeiou".contains(it) } > 2
                    && duplicates.any { word.contains(it) }
                    && !illegal.any { word.contains(it) }
        }
    }

    override fun partTwo(input: List<String>): Int {
        val pairs = ('a'..'z').flatMap { first -> ('a'..'z').map { second -> "$first$second" } }
        return input.count { word ->
            pairs.any { word.split(it).size > 2 } && containDistancePair(word)
        }
    }

    private fun containDistancePair(input: String): Boolean {
        return (0 until input.length - 2).any { input[it] == input[it + 2] }
    }
}

fun main() {
    execute(Day05::class)
}
