package advent.of.code

@Files("/Day23.txt")
class Day23 : Puzzle<MutableList<Day23.Instruction>, Int> {

    sealed interface Instruction

    data class Cpy(val first: String, val second: String) : Instruction
    data class Jnz(val first: String, val second: String) : Instruction
    data class Inc(val register: Char) : Instruction
    data class Dec(val register: Char) : Instruction
    data class Tgl(val register: Char) : Instruction

    override fun parse(input: List<String>) = input.map { line ->
        val parts = line.split(" ")
        when (parts[0]) {
            "cpy" -> Cpy(parts[1], parts[2])
            "inc" -> Inc(parts[1][0])
            "dec" -> Dec(parts[1][0])
            "jnz" -> Jnz(parts[1], parts[2])
            "tgl" -> Tgl(parts[1][0])
            else -> throw Error("Invalid line $line")
        }
    }.toMutableList()

    override fun partOne(input: MutableList<Instruction>): Int = evaluate(mutableMapOf('a' to 7, 'b' to 0, 'c' to 0, 'd' to 0), input)

    override fun partTwo(input: MutableList<Instruction>): Int = evaluate(mutableMapOf('a' to 12, 'b' to 0, 'c' to 0, 'd' to 0), input)

    private fun evaluate(registers: MutableMap<Char, Int>, instructions: MutableList<Instruction>): Int {
        var index = 0
        while (index < instructions.size) {
            when (val instruction = instructions[index]) {
                is Inc -> registers.compute(instruction.register) { _, v -> (v ?: 0) + 1 }
                is Dec -> registers.compute(instruction.register) { _, v -> (v ?: 0) - 1 }
                is Cpy -> {
                    if (instruction.second.toIntOrNull() == null) {
                        val register = instruction.second[0]
                        val value = instruction.first.toIntOrNull() ?: registers[instruction.first[0]] ?: 0
                        registers[register] = value
                    }
                }
                is Jnz -> {
                    val first = instruction.first.toIntOrNull() ?: registers[instruction.first[0]] ?: 0
                    val second = instruction.second.toIntOrNull() ?: registers[instruction.second[0]] ?: 0
                    if (first != 0) {
                        index += (second - 1)
                    }
                }
                is Tgl -> {
                    val affectedIndex = index + (registers[instruction.register] ?: 0)
                    if (0 <= affectedIndex && affectedIndex < instructions.size) {
                        instructions[affectedIndex] = when (val affectedInstruction = instructions[affectedIndex]) {
                            is Inc -> Dec(affectedInstruction.register)
                            is Dec -> Inc(affectedInstruction.register)
                            is Tgl -> Inc(affectedInstruction.register)
                            is Cpy -> Jnz(affectedInstruction.first, affectedInstruction.second)
                            is Jnz -> Cpy(affectedInstruction.first, affectedInstruction.second)
                        }
                    }
                }
            }
            index++
        }
        return registers['a'] ?: 0
    }
}

fun main() {
    execute(Day23::class)
}
