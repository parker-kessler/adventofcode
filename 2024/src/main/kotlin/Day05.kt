package advent.of.code

@Input("/Day05.txt")
class Day05 : Puzzle<Pair<List<String>, List<List<Int>>>, Int> {

    override fun parse(input: List<String>): Pair<List<String>, List<List<Int>>> {
        return input.filter { it.isNotBlank() }
            .partition { "|" in it }
            .let { (rules, pages) -> rules to pages.map { it.split(",").map(String::toInt) } }
    }

    override fun partOne(input: Pair<List<String>, List<List<Int>>>): Int {
        return input.let { (rules, pages) ->
            pages.filter { it.isValid(rules) }.sumOf { it.middle() }
        }
    }

    override fun partTwo(input: Pair<List<String>, List<List<Int>>>): Int {
        return input.let { (rules, pages) ->
            pages.filter { !it.isValid(rules) }.sumOf { it.sortedWith { a, b -> if ("$a|$b" in rules) -1 else 1 }.middle() }
        }
    }

    private fun List<Int>.isValid(rules: List<String>): Boolean {
        for (i in indices) {
            for (j in (i + 1) until size) {
                if ("${this[i]}|${this[j]}" !in rules) {
                    return false
                }
            }
        }
        return true
    }

    private fun List<Int>.middle(): Int = this[size / 2]

}

fun main() {
    execute(Day05::class)
}
