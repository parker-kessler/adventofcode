package advent.of.code

import kotlin.math.pow

data class Program(val registerA: Long, val instructions: List<Int>)

@Files("/Day17.txt")
class Day17 : Puzzle<Program, String> {

    override fun parse(input: List<String>) = Program(
        registerA = "[0-9]+".toRegex().find(input[0])?.value?.toLong() ?: 0,
        instructions = "[0-9]+".toRegex().findAll(input[4]).map { it.value.toInt() }.toList()
    )

    override fun partOne(input: Program): String = execute(input).second.joinToString(",")

    override fun partTwo(input: Program): String = findMinInput(input.instructions).toString()

    private fun execute(program: Program, debug: Boolean = false): Pair<Long, List<Long>> {
        var registerA = program.registerA
        var registerB = 0L
        var registerC = 0L

        val output = mutableListOf<Long>()

        var index = 0
        while (index < program.instructions.size) {
            val instruction = program.instructions[index++]
            val operand = program.instructions[index++]

            val comboOperand = when (operand) {
                4 -> registerA
                5 -> registerB
                6 -> registerC
                else -> operand.toLong()
            }

            when (instruction) {
                0 -> registerA = divide(registerA, comboOperand)
                1 -> registerB = registerB xor operand.toLong()
                2 -> registerB = comboOperand % 8
                3 -> when {
                    debug -> break
                    registerA != 0L -> {
                        index = operand
                        continue
                    }
                }
                4 -> registerB = registerB xor registerC
                5 -> output.add(comboOperand % 8)
                6 -> registerB = divide(registerA, comboOperand)
                7 -> registerC = divide(registerA, comboOperand)
            }
        }

        return registerA to output
    }

    private fun divide(register: Long, operand: Long): Long = register / 2.0.pow(operand.toInt()).toLong()

    private fun findMinInput(instructions: List<Int>): Long {
        val pending = mutableListOf(instructions.size to 0L)
        val valid = mutableListOf<Long>()
        while (pending.isNotEmpty()) {
            val (index, value) = pending.removeFirst()
            if (index == 0) {
                valid += value
                continue
            }
            val nextIndex = index - 1

            (0..7).map { (value shl 3) + it }.forEach { case ->
                val (registerA, output) = execute(Program(case, instructions), true)
                if (registerA == value && output.first() == instructions[nextIndex].toLong()) {
                    pending.add(nextIndex to case)
                }
            }
        }
        return valid.min()
    }
}

fun main() {
    execute(Day17::class)
}
