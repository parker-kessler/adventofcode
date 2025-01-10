package advent.of.code

@Files("/Day03.txt")
class Day03 : Puzzle<List<Char>, Int> {

    enum class Direction(val dx: Int, val dy: Int, val char: Char) {
        UP(0, 1, '^'),
        DOWN(0, -1, 'v'),
        LEFT(-1, 0, '<'),
        RIGHT(1, 0, '>');

        companion object {
            private val byChar = entries.associateBy { it.char }

            fun from(char: Char) = byChar.getValue(char)
        }
    }

    override fun parse(input: List<String>): List<Char> = input.first().toList()

    override fun partOne(input: List<Char>): Int {
        val seen = mutableSetOf(0 to 0)
        var santa = 0 to 0
        input.map { Direction.from(it) }.forEach { direction ->
            santa = santa.first + direction.dx to santa.second + direction.dy
            seen.add(santa)
        }
        return seen.size
    }

    override fun partTwo(input: List<Char>): Int {
        val seen = mutableSetOf(0 to 0)
        var santa = 0 to 0
        var roboSanta = 0 to 0
        input.map { Direction.from(it) }.forEachIndexed { index, direction ->
            if (index % 2 == 0) {
                santa = santa.first + direction.dx to santa.second + direction.dy
                seen.add(santa)
            } else {
                roboSanta = roboSanta.first + direction.dx to roboSanta.second + direction.dy
                seen.add(roboSanta)
            }
        }
        return seen.size
    }
}

fun main() {
    execute(Day03::class)
}
