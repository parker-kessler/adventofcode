package advent.of.code

@Files("/Day12.txt")
class Day12 : Puzzle<List<Day12.Instruction>, Int> {

    sealed interface Instruction

    data class CpyValue(val value: Int, val register: Char) : Instruction
    data class CpyRegister(val value: Char, val register: Char) : Instruction
    data class Inc(val register: Char) : Instruction
    data class Dec(val register: Char) : Instruction
    data class Jnz(val register: Char, val offset: Int) : Instruction

    override fun parse(input: List<String>) = input.map { line ->
        val parts = line.split(" ")
        when (parts[0]) {
            "cpy" -> {
                if (parts[1].toIntOrNull() == null) {
                    CpyRegister(parts[1][0], parts[2][0])
                } else {
                    CpyValue(parts[1].toInt(), parts[2][0])
                }
            }
            "inc" -> Inc(parts[1][0])
            "dec" -> Dec(parts[1][0])
            "jnz" -> Jnz(parts[1][0], parts[2].toInt())
            else -> throw Error("Invalid line $line")
        }
    }

    override fun partOne(input: List<Instruction>): Int = evaluate(mutableMapOf('a' to 0, 'b' to 0, 'c' to 0, 'd' to 0), input)

    override fun partTwo(input: List<Instruction>): Int = evaluate(mutableMapOf('a' to 0, 'b' to 0, 'c' to 1, 'd' to 0), input)

    private fun evaluate(registers: MutableMap<Char, Int>, instructions: List<Instruction>): Int {
        var index = 0
        while (index < instructions.size) {
            when (val instruction = instructions[index]) {
                is CpyValue -> registers[instruction.register] = instruction.value
                is CpyRegister -> registers[instruction.register] = registers[instruction.value] ?: 0
                is Inc -> registers.compute(instruction.register) { _, v -> (v ?: 0) + 1 }
                is Dec -> registers.compute(instruction.register) { _, v -> (v ?: 0) - 1 }
                is Jnz -> if (registers[instruction.register] != 0) index += (instruction.offset - 1)
            }
            index++
        }
        return registers['a'] ?: 0
    }
}

fun main() {
    execute(Day12::class)
}
