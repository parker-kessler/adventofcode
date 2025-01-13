package advent.of.code

@Files("/Day12.txt")
class Day12 : Puzzle<String, Int> {

    private data class State(var red: Boolean = false, var sum: Int = 0, val cause: String = "")

    override fun parse(input: List<String>) = input.first()

    override fun partOne(input: String): Int {
        return "-?[0-9]+".toRegex().findAll(input).sumOf { c -> c.value.toInt() }
    }

    override fun partTwo(input: String): Int {
        val stack = mutableListOf(State())
        "-?[0-9]+|\\{|\\}|\\[|]|red".toRegex().findAll(input).forEach { c ->
            when (val value = c.value) {
                "red" -> stack.last().takeIf { it.cause == "{" }?.red = true
                "{" -> stack.add(State(cause = value))
                "[" -> stack.add(State(cause = value))
                "]" -> stack.removeLast().let { stack.last().sum += it.sum }
                "}" -> stack.removeLast().takeIf { !it.red }?.let { stack.last().sum += it.sum }
                else -> stack.last().sum += value.toInt()
            }
        }
        return stack.last().sum
    }

}

fun main() {
    execute(Day12::class)
}
