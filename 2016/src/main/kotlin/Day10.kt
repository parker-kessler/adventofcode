package advent.of.code

@Files("/Day10.txt")
class Day10 : Puzzle<List<Day10.Instruction>, Int> {

    sealed interface Instruction
    data class Value(val value: Int, val bot: Int) : Instruction
    data class Gives(val bot: Int, val low: Action, val high: Action) : Instruction

    sealed interface Action
    data class Output(val index: Int) : Action
    data class PassToBot(val bot : Int) : Action

    data class Result(val valuesByBot: Map<Int, List<Int>>, val outputs: Map<Int, Int>)

    override fun parse(input: List<String>) = input.map { line ->
        val nums = "[0-9]+".toRegex().findAll(line).map { it.value.toInt() }.toList()
        when {
            "value" in line -> Value(nums[0], nums[1])
            else -> {
                Gives (
                    bot = nums[0],
                    low = if ("low to output" in line) Output(nums[1]) else PassToBot(nums[1]),
                    high = if ("high to output" in line) Output(nums[2]) else PassToBot(nums[2])
                )
            }
        }
    }

    override fun partOne(input: List<Instruction>): Int {
        val valuesByBot = analyze(input).valuesByBot
        return valuesByBot.entries.first { it.value == listOf(17, 61) }.key
    }

    override fun partTwo(input: List<Instruction>): Int {
        val outputs = analyze(input).outputs
        return listOf(0, 1, 2).mapNotNull { outputs[it] }.reduce { a, b -> a * b }
    }

    private fun analyze(instructions: List<Instruction>): Result {
        val valuesByBot = instructions.filterIsInstance<Value>()
            .groupBy { it.bot }
            .mapValues { entry -> entry.value.map { it.value }.toMutableList() }
            .toMutableMap()
        val gives = instructions.filterIsInstance<Gives>().associateBy { it.bot }
        val queue = mutableListOf(valuesByBot.entries.first { it.value.size == 2 }.key)
        val seen = mutableSetOf<Int>()
        val outputs = mutableMapOf<Int, Int>()

        while (queue.isNotEmpty()) {
            val bot = queue.removeFirst()
            val values = valuesByBot[bot]
            val give = gives[bot]
            if (values == null || values.size < 2 || give == null) {
                queue.add(bot)
                continue
            }

            if (!seen.add(bot)) continue

            values.sort()

            when (val low = give.low) {
                is PassToBot -> {
                    valuesByBot.computeIfAbsent(low.bot) { mutableListOf() }.add(values[0])
                    queue.add(low.bot)
                }
                is Output -> outputs[low.index] = values[0]
            }

            when (val high = give.high) {
                is PassToBot -> {
                    valuesByBot.computeIfAbsent(high.bot) { mutableListOf() }.add(values[1])
                    queue.add(high.bot)
                }
                is Output -> outputs[high.index] = values[1]
            }
        }
        return Result(valuesByBot, outputs)
    }
}

fun main() {
    execute(Day10::class)
}
