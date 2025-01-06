package advent.of.code

@Files("/Day21.txt")
class Day21 : Puzzle<List<String>, Long> {

    enum class Direction(val dx: Int, val dy: Int, val char: Char) {
        UP(0, 1, '^'),
        DOWN(0, -1, 'v'),
        LEFT(-1, 0, '<'),
        RIGHT(1, 0, '>');
    }

    interface Keypad {
        operator fun plus(direction: Direction): Keypad?
    }

    enum class NumericKeypad(val x: Int, val y: Int, val char: Char): Keypad {
        ACTIVATE(2, 0, 'A'),
        ZERO(1, 0, '0'),
        ONE(0, 1, '1'),
        TWO(1, 1, '2'),
        THREE(2, 1, '3'),
        FOUR(0, 2, '4'),
        FIVE(1, 2, '5'),
        SIX(2, 2, '6'),
        SEVEN(0, 3, '7'),
        EIGHT(1, 3, '8'),
        NINE(2, 3, '9');

        companion object {
            private val keyedByChar = entries.associateBy { it.char }
            private val keyedByPair = entries.associateBy { it.x to it.y }

            fun getNumericKeypad(char: Char): Keypad = keyedByChar[char]!!
        }

        override operator fun plus(direction: Direction): NumericKeypad? = keyedByPair[x + direction.dx to y + direction.dy]
    }

    enum class ArrowKeypad(val x: Int, val y: Int, val char: Char): Keypad {
        LEFT(0, 0, '<'),
        DOWN(1, 0, 'v'),
        RIGHT(2, 0, '>'),
        UP(1, 1, '^'),
        ACTIVATE(2, 1, 'A');

        companion object {
            private val keyedByChar = ArrowKeypad.entries.associateBy { it.char }
            private val keyedByPair = ArrowKeypad.entries.associateBy { it.x to it.y }

            fun getArrowKeypad(char: Char): Keypad = keyedByChar[char]!!
        }

        override fun plus(direction: Direction): ArrowKeypad? = keyedByPair[x + direction.dx to y + direction.dy]
    }

    override fun parse(input: List<String>) = input

    override fun partOne(input: List<String>): Long = 0L

    override fun partTwo(input: List<String>): Long {

        return Cheat.Day21(input).solvePart2()
    }

    private val cache = mutableMapOf<Pair<Char, Char>, List<String>>()

    private fun getComplexity(input: List<String>, robots: Int): Long {
        return input.sumOf { line ->
            var result = runRobot(line) { NumericKeypad.getNumericKeypad(it) }

            result.forEach { sadf ->
                var temp = listOf(sadf)
                repeat(2) { o ->
                    val fresult = temp.flatMap { r -> runRobot(r) { ArrowKeypad.getArrowKeypad(it)} }

                    temp = fresult
                }
            }

            repeat(4) { o ->
                val fresult = result.flatMap { r -> runRobot(r) { ArrowKeypad.getArrowKeypad(it)} }
                val min = fresult.minBy { it.length }
                val max = fresult.maxBy { it.length }
                if (min.length != max.length) {
                    println("Example for $line - $o")
                    println(result)
                    println(min)
                    println(max)
                }

                println(o)

                result = fresult
            }

            var l = result.minBy { it.length }

            repeat(robots - 2) {
                println(l.length)
                l = runMinRobot(l) { ArrowKeypad.getArrowKeypad(it) }
            }

            val shortest = l.length
            val numericValue = "[0-9]+".toRegex().findAll(line).map { it.value.toLong() }.first()

            println("$shortest $numericValue")

            numericValue * shortest
        }
    }

    private fun runRobot(input: String, keypad: (char: Char) -> Keypad): List<String> {
        val list = mutableListOf("" to 0)
        val results = mutableListOf<String>()
        while (list.isNotEmpty()) {
            val (outcome, index) = list.removeFirst()

            if (index == input.length) {
                results.add(outcome)
            } else {
                val previous = if (index > 0) input[index - 1] else 'A'
                val current = input[index]

                cache.getOrPut(previous to current) { getPaths(keypad(previous), keypad(current)) }.forEach {
                    list.add(outcome + it to index + 1)
                }
            }
        }
        return results
    }

    private fun runMinRobot(input: String, keypad: (char: Char) -> Keypad): String {
        var output = ""
        "A$input".zipWithNext { a, b ->
            output += cache.getOrPut(a to b) { getPaths(keypad(a), keypad(a)) }.first()
        }
        return output
    }

    private fun getPaths(start: Keypad, end: Keypad): List<String> {
        val paths = mutableListOf<String>()
        val list = mutableListOf(listOf(start) to "")
        while (list.isNotEmpty()) {
            val (pressedButtons, path) = list.removeFirst()
            if (pressedButtons.last() == end) {
                paths.add(path + 'A')
                continue
            }

            Direction.entries.forEach { direction ->
                (pressedButtons.last() + direction)
                    ?.takeIf { !pressedButtons.contains(it) }
                    ?.let { list.add(pressedButtons + it to path + direction.char) }
            }
        }

        val minLength = paths.minOf { it.length }
        return paths.filter { it.length == minLength }
    }
}


fun main() {
    execute(Day21::class)
}

data class Point2D(val x: Int, val y: Int) {
    operator fun plus(other: Point2D) =
        Point2D(x + other.x, y + other.y)

    operator fun minus(other: Point2D) =
        Point2D(x - other.x, y - other.y)

    fun neighbours() =
        listOf(
            Point2D(x - 1, y),
            Point2D(x + 1, y),
            Point2D(x, y - 1),
            Point2D(x, y + 1),
        )

    companion object {
        val UP = Point2D(0, -1)
        val RIGHT = Point2D(1, 0)
        val DOWN = Point2D(0, 1)
        val LEFT = Point2D(-1, 0)
    }
}

class Cheat {

    class Day21(private val codes: List<String>) {
        private val numeric = mapOf(
            Point2D(0, 0) to '7', Point2D(1, 0) to '8', Point2D(2, 0) to '9',
            Point2D(0, 1) to '4', Point2D(1, 1) to '5', Point2D(2, 1) to '6',
            Point2D(0, 2) to '1', Point2D(1, 2) to '2', Point2D(2, 2) to '3',
            Point2D(1, 3) to '0', Point2D(2, 3) to 'A'
        )

        private val directional = mapOf(
            Point2D(1, 0) to '^', Point2D(2, 0) to 'A',
            Point2D(0, 1) to '<', Point2D(1, 1) to 'v', Point2D(2, 1) to '>'
        )

        private val numericPaths = buildShortestPaths(keypad = numeric)

        private val directionalPaths = buildShortestPaths(keypad = directional)

        fun solvePart1() = solve(levels = 3)

        fun solvePart2() = solve(levels = 26)

        private fun solve(levels: Int) =
            codes.sumOf { code -> code.findCost(levels) * code.filter { it.isDigit() }.toInt() }

        private fun String.findCost(
            levels: Int,
            keypad: Map<Char, Map<Char, List<String>>> = numericPaths,
            cache: MutableMap<Pair<String, Int>, Long> = mutableMapOf()
        ): Long =
            cache.getOrPut(this to levels) {
                when (levels) {
                    0 -> length.toLong()
                    else -> {
                        "A$this".zipWithNext().sumOf { (from, to) ->
                            keypad.getValue(from).getValue(to).minOf { path ->
                                "${path}A".findCost(levels - 1, directionalPaths, cache)
                            }
                        }
                    }
                }
            }

        private fun buildShortestPaths(keypad: Map<Point2D, Char>): Map<Char, Map<Char, List<String>>> =
            buildMap {
                for (start in keypad.keys) {
                    val paths = mutableMapOf<Char, MutableList<String>>()
                    paths[keypad.getValue(start)] = mutableListOf("")

                    val queue = mutableListOf(start to "")
                    val visited = mutableSetOf<Point2D>()

                    while (queue.isNotEmpty()) {
                        val (current, path) = queue.removeFirst()
                        visited += current

                        current.neighbours().forEach {
                            if (it in keypad && it !in visited) {
                                val newPath = path + (it - current).toChar()
                                queue += it to newPath
                                paths.getOrPut(keypad.getValue(it)) { mutableListOf() } += newPath
                            }
                        }
                    }

                    set(keypad[start]!!, paths)
                }
            }

        private fun Point2D.toChar() =
            when (this) {
                Point2D.UP -> "^"
                Point2D.RIGHT -> ">"
                Point2D.DOWN -> "v"
                Point2D.LEFT -> "<"
                else -> error("Invalid direction: $this")
            }
    }
}
