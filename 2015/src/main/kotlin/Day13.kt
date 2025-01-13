package advent.of.code

@Files("/Day13.txt")
class Day13 : Puzzle<List<Triple<String, String, Int>>, Int> {

    override fun parse(input: List<String>) = input.map { line ->
        val from = line.split(" ").first()
        val to = line.split(" ").last().removeSuffix(".")
        val amount = (if ("lose" in line) -1 else 1) * ("[0-9]+".toRegex().find(line)?.value?.toInt() ?: 0)

        Triple(from, to, amount)
    }

    override fun partOne(input: List<Triple<String, String, Int>>) = findMaxHappiness(input, false)

    override fun partTwo(input: List<Triple<String, String, Int>>) = findMaxHappiness(input, true)

    private fun findMaxHappiness(input: List<Triple<String, String, Int>>, includeMyself: Boolean): Int {
        val unique = if (includeMyself) mutableSetOf("me") else mutableSetOf()
        val cost = mutableMapOf<String, Int>()
        input.forEach { (from, to, amount) ->
            unique.add(from)
            unique.add(to)
            cost[from + to] = amount
        }

        return unique.toList().permutations().maxOf { people ->
            (people + people.first()).zipWithNext().sumOf { (a, b) ->
                cost.getOrDefault(b + a, 0) + cost.getOrDefault(a + b, 0)
            }
        }
    }

    private val <T> List<T>.head: T
        get() = first()

    private val <T> List<T>.tail: List<T>
        get() = drop(1)

    private fun <T> List<T>.permutations(): List<List<T>> {
        if (isEmpty()) return emptyList()
        if (size == 1) return listOf(this)

        return tail.permutations()
            .fold(mutableListOf()) { acc, perm ->
                (0..perm.size).mapTo(acc) { i ->
                    perm.subList(0, i) + head + perm.subList(i, perm.size)
                }
            }
    }
}

fun main() {
    execute(Day13::class)
}
