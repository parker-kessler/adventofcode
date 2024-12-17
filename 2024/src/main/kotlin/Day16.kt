package advent.of.code

import java.util.PriorityQueue

data class Reindeer(val score: Int, val direction: MazeDirection, val path: List<Pair<Int, Int>>)

enum class MazeDirection(val dx: Int, val dy: Int) {
    UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1);

    fun opposite(): MazeDirection = when (this) {
        UP -> DOWN
        DOWN -> UP
        LEFT -> RIGHT
        RIGHT -> LEFT
    }
}

@Files("/Day16.txt")
class Day16 : Puzzle<List<MutableList<Char>>, Int> {

    companion object {
        const val START = 'S'
        const val END = 'E'
        const val EMPTY = '.'

        const val TURN_COST = 1001
        const val COST = 1
    }

    override fun parse(input: List<String>): List<MutableList<Char>> = input.map { it.toMutableList() }

    override fun partOne(input: List<MutableList<Char>>) = findReindeerPaths(input).first().score

    override fun partTwo(input: List<MutableList<Char>>) = findReindeerPaths(input).flatMap { it.path }.toSet().size

    private fun findReindeerPaths(grid: List<List<Char>>): List<Reindeer> {
        val queue = PriorityQueue<Reindeer>(compareBy { it.score }).apply {
            add(Reindeer(0, MazeDirection.RIGHT, listOf(findStart(grid))))
        }

        val seen = mutableMapOf<Pair<Pair<Int, Int>, MazeDirection>, Int>()
        val reindeers = mutableListOf<Reindeer>()

        while (queue.isNotEmpty()) {
            val reindeer = queue.poll()
            val currentLocation = reindeer.path.last()

            val key = currentLocation to reindeer.direction
            if (reindeer.score <= seen.getOrDefault(key, Int.MAX_VALUE)) {
                seen[key] = reindeer.score
            } else continue

            if (grid[currentLocation.first][currentLocation.second] == END) {
                reindeers.add(reindeer)
                continue
            }

            MazeDirection.entries.asSequence()
                .filter { it != reindeer.direction.opposite() }
                .associateWith { currentLocation.first + it.dx to currentLocation.second + it.dy }
                .filter { (_, location) -> grid[location.first][location.second] in listOf(EMPTY, END) }
                .map { (direction, location) ->
                    Reindeer(
                        score = reindeer.score + if (direction != reindeer.direction) TURN_COST else COST,
                        direction = direction,
                        path = reindeer.path + location
                    )
                }
                .forEach(queue::add)
        }

        return reindeers
    }

    private fun findStart(grid: List<List<Char>>): Pair<Int, Int> {
        grid.forEachIndexed { i, row -> row.forEachIndexed { j, k -> if (k == START) return i to j } }
        return 0 to 0
    }

}

fun main() {
    execute(Day16::class)
}
