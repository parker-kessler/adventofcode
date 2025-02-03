package advent.of.code

@Files("/Day02.txt")
class Day02 : Puzzle<List<String>, String> {

    enum class Direction(val dx: Int, val dy: Int, val letter: Char) {
        UP(-1, 0, 'U'),
        DOWN(1, 0, 'D'),
        RIGHT(0, 1, 'R'),
        LEFT(0, -1, 'L');

        companion object {
            val byLetter = entries.associateBy { it.letter }
        }
    }

    override fun parse(input: List<String>): List<String> = input

    override fun partOne(input: List<String>): String {
        val keypad = """
            .....
            .123.
            .456.
            .789.
            .....
        """.trimIndent().split("\n").map { line -> line.toCharArray() }
        return getCode(keypad, input, 2 to 2)
    }

    override fun partTwo(input: List<String>): String {
        val keypad = """
            .......
            ...1...
            ..234..
            .56789.
            ..ABC..
            ...D...
            .......
        """.trimIndent().split("\n").map { line -> line.toCharArray() }
        return getCode(keypad, input, 3 to 1)
    }

    private fun getCode(keypad: List<CharArray>, instructions: List<String>, start: Pair<Int, Int>): String {
        var currentButton = start
        val result = Array(instructions.size) { '.' }
        instructions.forEachIndexed { index, line ->
            line.mapNotNull { Direction.byLetter[it] }.forEach { direction ->
                val nextButton = currentButton.first + direction.dx to currentButton.second + direction.dy
                if (keypad[nextButton.first][nextButton.second] != '.') {
                    currentButton = nextButton
                }
                result[index] = keypad[currentButton.first][currentButton.second]
            }
        }

        return result.joinToString("")
    }
}

fun main() {
    execute(Day02::class)
}
