package advent.of.code

@Files("/Day24.txt")
class Day24 : Puzzle<List<List<Char>>, Int> {

    companion object {
        const val WALL = '#'
        const val OPEN = '.'

        val DIRECTIONS = listOf((0 to 1), (0 to -1), (-1 to 0), (1 to 0))
    }

    override fun parse(input: List<String>) = input.map { it.toList() }

    override fun partOne(input: List<List<Char>>): Int {
        val nums = input.flatten().distinct() - listOf(WALL, OPEN)
        val distances = getDistances(input, nums)

        return (nums - listOf('0')).permutations().minOf {
            val path = listOf('0') + it
            path.zipWithNext { a, b ->
                distances["$a$b"] ?: error("Should never happen")
            }.sum()
        }
    }

    override fun partTwo(input: List<List<Char>>): Int {
        val nums = input.flatten().distinct() - listOf(WALL, OPEN)
        val distances = getDistances(input, nums)

        return (nums - listOf('0')).permutations().minOf {
            val path = listOf('0') + it + listOf('0')
            path.zipWithNext { a, b ->
                distances["$a$b"] ?: error("Should never happen")
            }.sum()
        }
    }

    private fun getDistances(input: List<List<Char>>, nums: List<Char>): Map<String, Int> {
        val distances = mutableMapOf<String, Int>()
        for (i in nums.indices) {
            for (j in nums.indices) {
                val startChar = nums[i]
                val endChar = nums[j]

                distances["$startChar$endChar"] = shortestDistance(input, find(input, startChar), find(input, endChar))
            }
        }
        return distances
    }

    private fun shortestDistance(grid: List<List<Char>> ,start: Pair<Int, Int>, end: Pair<Int, Int>): Int {
        val seen = Array(grid.size) { IntArray(grid[0].size) }
        val queue = mutableListOf(start)
        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()

            DIRECTIONS.forEach { direction ->
                val newX = direction.first + node.first
                val newY = direction.second + node.second

                if (seen[newX][newY] == 0 && grid[newX][newY] != WALL) {
                    seen[newX][newY] = seen[node.first][node.second] + 1
                    queue.add(newX to newY)
                }
            }
        }
        return seen[end.first][end.second]
    }

    private fun find(grid: List<List<Char>>, number: Char): Pair<Int, Int> {
        for (i in grid.indices) {
            for (j in grid[0].indices) {
                if (grid[i][j] == number) {
                    return i to j
                }
            }
        }
        return -1 to -1
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
    execute(Day24::class)
}
