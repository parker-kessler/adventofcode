package advent.of.code

import kotlin.math.abs

@Files("/Day01.txt")
class Day01 : Puzzle<List<Pair<Char, Int>>, Int> {

    enum class Direction(val dx: Int, val dy: Int) {
        NORTH(0, 1),
        SOUTH(0, -1),
        EAST(1, 0),
        WEST(-1, 0);

        fun turnLeft() = when (this) {
            NORTH -> WEST
            WEST -> SOUTH
            SOUTH -> EAST
            EAST -> NORTH
        }

        fun turnRight() = when (this) {
            NORTH -> EAST
            EAST -> SOUTH
            SOUTH -> WEST
            WEST -> NORTH
        }
    }

    override fun parse(input: List<String>) = input.first().split(", ").map {
        val scale = "[0-9]+".toRegex().find(it)?.value?.toInt() ?: 0
        it[0] to scale
    }

    override fun partOne(input: List<Pair<Char, Int>>): Int {
        var direction = Direction.NORTH
        var current = 0 to 0
        input.forEach { (char, scale) ->
            direction = if (char == 'L') direction.turnLeft() else direction.turnRight()
            current = current.first + direction.dx * scale to current.second + direction.dy * scale
        }
        return abs(current.first) + abs(current.second)
    }

    override fun partTwo(input: List<Pair<Char, Int>>): Int {
        val seen = mutableSetOf(0 to 0)
        var direction = Direction.NORTH
        var current = 0 to 0
        input.forEach { (char, scale) ->
            direction = if (char == 'L') direction.turnLeft() else direction.turnRight()
            repeat(scale) {
                current = current.first + direction.dx to current.second + direction.dy
                if (!seen.add(current)) {
                    return abs(current.first) + abs(current.second)
                }
            }
        }
        return 0
    }
}

fun main() {
    execute(Day01::class)
}
