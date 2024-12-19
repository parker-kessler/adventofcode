package advent.of.code

@Files("/Day15.txt")
class Day15 : Puzzle<Day15.Day15Input, Long> {

    data class Day15Input(val grid: List<MutableList<Char>>, val moves: List<Char>)

    companion object {
        const val EMPTY = '.'
        const val BOX = 'O'
        const val ROBOT = '@'
        const val OBSTACLE = '#'
        const val UP = '^'
        const val DOWN = 'v'
        const val LEFT = '<'
        const val RIGHT = '>'
        const val LEFT_BOX = '['
        const val RIGHT_BOX = ']'
    }

    override fun parse(input: List<String>): Day15Input {
        val (grid, moves) = input.filter { it.isNotBlank() }.partition { OBSTACLE in it }
        return Day15Input(
            grid = grid.map { it.toMutableList() },
            moves = moves.flatMap { it.toList() },
        )
    }

    override fun partOne(input: Day15Input): Long {
        val grid = input.grid
        input.moves.fold(findRobot(grid)) { robot, move ->

            val (xRange, yRange) = when (move) {
                LEFT -> robot.first..robot.first to (robot.second - 1 downTo 0)
                RIGHT -> robot.first..robot.first to (robot.second + 1 until grid.size)
                DOWN -> (robot.first + 1 until grid.first().size) to robot.second..robot.second
                UP -> (robot.first - 1 downTo 0) to robot.second..robot.second
                else -> error("Invalid move $move")
            }
            val nextRobot = xRange.first to yRange.first
            for (x in xRange) {
                for (y in yRange) {
                    when (grid[x][y]) {
                        OBSTACLE -> return@fold robot
                        EMPTY -> {
                            if (x != nextRobot.first || y != nextRobot.second) grid[x][y] = BOX
                            grid[robot.first][robot.second] = EMPTY
                            grid[nextRobot.first][nextRobot.second] = ROBOT
                            return@fold nextRobot
                        }
                    }

                }
            }
            robot
        }

        return gpsSum(grid, BOX)
    }

    override fun partTwo(input: Day15Input): Long {
        val grid = expandGrid(input.grid)

        input.moves.fold(findRobot(grid)) { robot, move ->
            val nextRobot = when (move) {
                LEFT -> moveLeft(grid, robot)
                RIGHT -> moveRight(grid, robot)
                UP -> moveUp(grid, robot)
                DOWN -> moveDown(grid, robot)
                else -> error("Invalid move $move")
            }

            grid[robot.first][robot.second] = EMPTY
            grid[nextRobot.first][nextRobot.second] = ROBOT

            nextRobot
        }

        return gpsSum(grid, LEFT_BOX)
    }

    private fun findRobot(grid: List<List<Char>>): Pair<Int, Int> {
        grid.forEachIndexed { i, row -> row.forEachIndexed { j, k -> if (k == ROBOT) return i to j } }
        return 0 to 0
    }

    private fun gpsSum(grid: List<MutableList<Char>>, char: Char): Long {
        var sum = 0L
        for (i in grid.indices) {
            for (j in grid[i].indices) {
                if (grid[i][j] == char) {
                    sum += i * 100 + j
                }
            }
        }
        return sum
    }

    private fun expandGrid(grid: List<List<Char>>): List<MutableList<Char>> {
        return grid.map { row ->
            row.map {
                when (it) {
                    OBSTACLE -> "##"
                    BOX -> "[]"
                    EMPTY -> ".."
                    ROBOT -> "@."
                    else -> error("Invalid move $it")
                }
            }.flatMap(String::toList).toMutableList()
        }
    }

    private fun moveLeft(grid: List<MutableList<Char>>, robot: Pair<Int, Int>): Pair<Int, Int> {
        val nextRobot = robot.first to robot.second - 1
        for (y in robot.second - 1 downTo 0) {
            when (grid[robot.first][y]) {
                OBSTACLE -> return robot
                EMPTY -> {
                    if (y != nextRobot.second) {
                        (y..robot.second step 2).forEach { i ->
                            grid[robot.first][i] = LEFT_BOX
                            grid[robot.first][i + 1] = RIGHT_BOX
                        }
                    }
                    return nextRobot
                }
            }
        }
        return robot
    }

    private fun moveRight(grid: List<MutableList<Char>>, robot: Pair<Int, Int>): Pair<Int, Int> {
        val nextRobot = robot.first to robot.second + 1
        for (y in robot.second + 1 until grid.first().size) {
            when (grid[robot.first][y]) {
                OBSTACLE -> return robot
                EMPTY -> {
                    if (y != nextRobot.second) {
                        (robot.second..y step 2).forEach { i ->
                            grid[robot.first][i] = LEFT_BOX
                            grid[robot.first][i + 1] = RIGHT_BOX
                        }
                    }
                    return nextRobot
                }
            }
        }
        return robot
    }

    private fun moveUp(grid: List<MutableList<Char>>, robot: Pair<Int, Int>): Pair<Int, Int> {
        val boxes = mutableListOf<Pair<Int, Int>>()
        when (grid[robot.first - 1][robot.second]) {
            OBSTACLE -> return robot
            EMPTY -> return robot.first - 1 to robot.second
            LEFT_BOX -> boxes.add(robot.first - 1 to robot.second)
            RIGHT_BOX -> boxes.add(robot.first - 1 to robot.second - 1)
        }

        val validBoxes = mutableListOf<Pair<Int, Int>>()
        while (boxes.isNotEmpty()) {
            val box = boxes.removeFirst()
            when (grid[box.first - 1][box.second]) {
                OBSTACLE -> return robot
                LEFT_BOX -> boxes.add(box.first - 1 to box.second)
                RIGHT_BOX -> boxes.add(box.first - 1 to box.second - 1)
            }
            when (grid[box.first - 1][box.second + 1]) {
                OBSTACLE -> return robot
                LEFT_BOX -> boxes.add(box.first - 1 to box.second + 1)
            }
            validBoxes.add(box)
        }

        while (validBoxes.isNotEmpty()) {
            val box = validBoxes.removeLast()
            grid[box.first - 1][box.second] = LEFT_BOX
            grid[box.first - 1][box.second + 1] = RIGHT_BOX
            grid[box.first][box.second] = EMPTY
            grid[box.first][box.second + 1] = EMPTY
        }
        return robot.first - 1 to robot.second
    }

    private fun moveDown(grid: List<MutableList<Char>>, robot: Pair<Int, Int>): Pair<Int, Int> {
        val boxes = mutableListOf<Pair<Int, Int>>()
        when (grid[robot.first + 1][robot.second]) {
            OBSTACLE -> return robot
            EMPTY -> return robot.first + 1 to robot.second
            LEFT_BOX -> boxes.add(robot.first + 1 to robot.second)
            RIGHT_BOX -> boxes.add(robot.first + 1 to robot.second - 1)
        }

        val validBoxes = mutableListOf<Pair<Int, Int>>()
        while (boxes.isNotEmpty()) {
            val box = boxes.removeFirst()
            when (grid[box.first + 1][box.second]) {
                OBSTACLE -> return robot
                LEFT_BOX -> boxes.add(box.first + 1 to box.second)
                RIGHT_BOX -> boxes.add(box.first + 1 to box.second - 1)
            }
            when (grid[box.first + 1][box.second + 1]) {
                OBSTACLE -> return robot
                LEFT_BOX -> boxes.add(box.first + 1 to box.second + 1)
            }
            validBoxes.add(box)
        }

        while (validBoxes.isNotEmpty()) {
            val box = validBoxes.removeLast()
            grid[box.first + 1][box.second] = LEFT_BOX
            grid[box.first + 1][box.second + 1] = RIGHT_BOX
            grid[box.first][box.second] = EMPTY
            grid[box.first][box.second + 1] = EMPTY
        }
        return robot.first + 1 to robot.second
    }

}

fun main() {
    execute(Day15::class)
}
