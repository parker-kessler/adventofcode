package advent.of.code

@Files("/Day23.txt")
class Day23 : Puzzle<List<Day23.Instruction>, Int> {

    sealed interface Instruction

    data class Hlf(val register: Char) : Instruction
    data class Tpl(val register: Char) : Instruction
    data class Inc(val register: Char) : Instruction
    data class Jmp(val offset: Int) : Instruction
    data class Jie(val register: Char, val offset: Int) : Instruction
    data class Jio(val register: Char, val offset: Int) : Instruction

    override fun parse(input: List<String>) = input.map { line ->
        val parts = line.split(" ")

        when {
            "hlf" in line -> Hlf(parts[1][0])
            "tpl" in line -> Tpl(parts[1][0])
            "inc" in line -> Inc(parts[1][0])
            "jmp" in line -> Jmp(parts[1].toInt())
            "jie" in line -> Jie(parts[1][0], parts[2].toInt())
            "jio" in line -> Jio(parts[1][0], parts[2].toInt())
            else -> throw Error("Unknown input")
        }
    }

    override fun partOne(input: List<Instruction>): Int = execute(mutableMapOf('a' to 0, 'b' to 0), input)

    override fun partTwo(input: List<Instruction>): Int = execute(mutableMapOf('a' to 1, 'b' to 0), input)

    private fun execute(registers: MutableMap<Char, Int>, instructions: List<Instruction>): Int {
        var index = 0
        while (index < instructions.size) {
            when (val instruction = instructions[index]) {
                is Hlf -> registers.computeIfPresent(instruction.register) { _, v -> v / 2 }.also { index++ }
                is Tpl -> registers.computeIfPresent(instruction.register) { _, v -> v * 3 }.also { index++ }
                is Inc -> registers.computeIfPresent(instruction.register) { _, v -> v + 1 }.also { index++ }
                is Jmp -> index += instruction.offset
                is Jie -> registers[instruction.register]?.takeIf { it % 2 == 0 }?.let { index += instruction.offset }
                    ?: run { index++ }

                is Jio -> registers[instruction.register]?.takeIf { it == 1 }?.let { index += instruction.offset }
                    ?: run { index++ }
            }
        }
        return registers['b'] ?: 0
    }
}

fun main() {
    execute(Day23::class)
}
