package advent.of.code

@Files("/Day09.txt")
class Day09 : Puzzle<List<Triple<String, String, Int>>, Int> {

    override fun parse(input: List<String>) = input.map { line ->
        val split = line.split(" ")
        Triple(split[0], split[2], split[4].toInt())
    }

    override fun partOne(input: List<Triple<String, String, Int>>): Int {
        val map = mutableMapOf<String, Int>()
        val unique = mutableSetOf<String>()
        input.forEach { (from, to, distance) ->
            map[from + to] = distance
            map[to + from] = distance
            unique.add(from)
            unique.add(to)
        }
        return unique.toList().permutations().minOf { cities ->
            cities.zipWithNext().sumOf { (a, b) -> map.getOrDefault(a + b, 0) }
        }
    }

    override fun partTwo(input: List<Triple<String, String, Int>>): Int {
        val map = mutableMapOf<String, Int>()
        val unique = mutableSetOf<String>()
        input.forEach { (from, to, distance) ->
            map[from + to] = distance
            map[to + from] = distance
            unique.add(from)
            unique.add(to)
        }
        return unique.toList().permutations().maxOf { cities ->
            cities.zipWithNext().sumOf { (a, b) -> map.getOrDefault(a + b, 0) }
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
    execute(Day09::class)
}
