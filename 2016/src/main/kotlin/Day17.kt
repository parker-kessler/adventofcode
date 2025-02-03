package advent.of.code

import java.math.BigInteger
import java.security.MessageDigest

@Files("/Day17.txt")
class Day17 : Puzzle<String, String> {

    private enum class Direction(val dx: Int, val dy: Int, val char: Char) {
        UP(0, -1, 'U'),
        DOWN(0, 1, 'D'),
        LEFT(-1, 0, 'L'),
        RIGHT(1, 0, 'R')
    }

    companion object {
        private val VALID = setOf('b', 'c', 'd', 'e', 'f')
        private val DIRECTIONS = listOf(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT)
    }

    data class State(val current: Pair<Int, Int>, val steps: String)

    override fun parse(input: List<String>) = input.first()

    override fun partOne(input: String): String = findRoutes(input).minBy { it.length }

    override fun partTwo(input: String): String = findRoutes(input).maxOf { it.length }.toString()

    private fun findRoutes(input: String): List<String> {
        val queue = mutableListOf(State(0 to 0, ""))
        val routes = mutableListOf<String>()
        while (queue.isNotEmpty()) {
            val state = queue.removeFirst()
            if (state.current.first == 3 && state.current.second == 3) {
                routes.add(state.steps)
                continue
            }

            val hash = md5(input + state.steps)
            DIRECTIONS.forEachIndexed { index, dir ->
                if (VALID.contains(hash[index])) {
                    val newX = state.current.first + dir.dx
                    val newY = state.current.second + dir.dy
                    if (newX >= 0 && newY >= 0 && newX <= 3 && newY <= 3) {
                        queue.add(State(newX to newY, state.steps + dir.char))
                    }
                }
            }
        }

        return routes
    }

    private fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

}

fun main() {
    execute(Day17::class)
}
