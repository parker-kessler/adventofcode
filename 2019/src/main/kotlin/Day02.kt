package advent.of.code

@Files("/Day02.txt")
class Day02 : Puzzle<List<Int>, Int> {

    override fun parse(input: List<String>): List<Int> = input.flatMap { it.split(",").map(String::toInt) }

    override fun partOne(input: List<Int>): Int = execute(input, 12, 2)

    override fun partTwo(input: List<Int>): Int = (0 until 100*100).first { execute(input, it / 100, it % 100) == 19690720 }

    private fun execute(input: List<Int>, noun: Int, verb: Int): Int {
        val program = input.toMutableList()

        program[1] = noun
        program[2] = verb

        var index = 0
        while (true) {
            when (program[index]) {
                1 -> program[program[index + 3]] = program[program[index + 1]] + program[program[index + 2]]
                2 -> program[program[index + 3]] = program[program[index + 1]] * program[program[index + 2]]
                99 -> break
            }
            index += 4
        }
        return program.first()
    }
}

fun main() {
    execute(Day02::class)
}
