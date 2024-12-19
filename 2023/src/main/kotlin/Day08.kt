package advent.of.code

@Files("/Day08.txt")
class Day08 : Puzzle<Day08.Day08Input, Int> {

    data class Day08Input(val instructions: List<Char>, val map: Map<String, Pair<String, String>>)

    override fun parse(input: List<String>): Day08Input {
        val (pairings, instructions) = input.filter { it.isNotBlank() }.partition { "=" in it }
        return Day08Input(
            instructions = instructions.flatMap { it.toList() },
            map = pairings.map { pairing -> "[1-9A-Z]+".toRegex().findAll(pairing).map { it.value }.toList() }.associate {
                it[0] to (it[1] to it[2])
            }
        )
    }

    override fun partOne(input: Day08Input): Int {
        var current = "AAA"
        var count = 0
        var index = 0
        while (current != "ZZZ") {
            input.map[current]?.let {
                current = if (input.instructions[index] == 'L') it.first else it.second
            }
            count++
            index = (index + 1) % input.instructions.size
        }
        return count
    }

    override fun partTwo(input: Day08Input): Int {
        var current = input.map.keys.filter { it.endsWith('A') }
        var count = 0
        var index = 0
        while (current.any { !it.endsWith('Z') }) {
            current = current.map {
                input.map[it]?.let {
                    if (input.instructions[index] == 'L') it.first else it.second
                }.orEmpty()
            }
            count++
            index = (index + 1) % input.instructions.size
        }
        return count
    }
}

fun main() {
    execute(Day08::class)
}
