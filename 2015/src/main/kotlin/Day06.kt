package advent.of.code

@Files("/Day06.txt")
class Day06 : Puzzle<List<Triple<Day06.Action, Pair<Int, Int>, Pair<Int, Int>>>, Int> {

    enum class Action(val word: String) {
        TOGGLE("toggle"), TURN_ON("turn on"), TURN_OFF("turn off")
    }

    override fun parse(input: List<String>): List<Triple<Action, Pair<Int, Int>, Pair<Int, Int>>> = input.map { line ->
        val action = Action.entries.first { line.contains(it.word) }
        val numbers = "[0-9]+".toRegex().findAll(line).map { it.value.toInt() }.toList()

        Triple(action, numbers[0] to numbers[1], numbers[2] to numbers[3])
    }

    override fun partOne(input: List<Triple<Action, Pair<Int, Int>, Pair<Int, Int>>>): Int {
        return (0..999).sumOf { x ->
            (0..999).count { y ->
                var lightOn = false
                input.filter { (_, start, end) ->
                    start.first <= x && start.second <= y && x <= end.first && y <= end.second
                }.forEach { (action, _, _) ->
                    lightOn = when (action) {
                        Action.TOGGLE -> !lightOn
                        Action.TURN_ON -> true
                        Action.TURN_OFF -> false
                    }
                }
                lightOn
            }
        }
    }

    override fun partTwo(input: List<Triple<Action, Pair<Int, Int>, Pair<Int, Int>>>): Int {
        return (0..999).sumOf { x ->
            (0..999).sumOf { y ->
                var brightness = 0
                input.filter { (_, start, end) ->
                    start.first <= x && start.second <= y && x <= end.first && y <= end.second
                }.forEach { (action, _, _) ->
                    brightness += when (action) {
                        Action.TOGGLE -> 2
                        Action.TURN_ON -> 1
                        Action.TURN_OFF -> if (brightness == 0) 0 else -1
                    }
                }
                brightness
            }
        }
    }
}

fun main() {
    execute(Day06::class)
}
