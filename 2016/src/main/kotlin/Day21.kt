package advent.of.code

@Files("/Day21.txt")
class Day21 : Puzzle<List<Day21.Instruction>, String> {

    sealed interface Instruction

    data class SwapPosition(var x: Int, var y: Int) : Instruction
    data class SwapLetter(val x: Char, val y: Char) : Instruction
    data class RotateLeft(val amount: Int) : Instruction
    data class RotateRight(val amount: Int) : Instruction
    data class RotateBasedUponPosition(val x: Char) : Instruction
    data class ReversePositions(val x: Int, val y: Int) : Instruction
    data class MovePosition(val x: Int, val y: Int) : Instruction

    override fun parse(input: List<String>) = input.map { line ->
        val nums = "[0-9]+".toRegex().findAll(line).map { it.value.toInt() }.toList()
        when {
            "swap position" in line -> SwapPosition(nums[0], nums[1])
            "swap letter" in line -> SwapLetter(" [a-z] ".toRegex().find(line)?.value.orEmpty()[1], line.last())
            "rotate left" in line -> RotateLeft(nums[0])
            "rotate right" in line -> RotateRight(nums[0])
            "rotate based on position" in line -> RotateBasedUponPosition(line.last())
            "reverse" in line -> ReversePositions(nums[0], nums[1])
            "move" in line -> MovePosition(nums[0], nums[1])
            else -> throw Error("Invalid input $line")
        }
    }

    override fun partOne(input: List<Instruction>): String = scramble("abcdefgh".toList(), input)

    override fun partTwo(input: List<Instruction>): String {
        return "abcdefgh".toList().permutations().first { scramble(it, input) == "fbgdceah" }.joinToString(separator = "")
    }

    private fun scramble(start: List<Char>, instructions: List<Instruction>): String {
        var scramble = start.toMutableList()
        instructions.forEach { instruction ->
            when (instruction) {
                is MovePosition -> {
                    scramble.add(instruction.y, scramble.removeAt(instruction.x))
                }
                is ReversePositions -> {
                    var left = instruction.x
                    var right = instruction.y
                    while (left < right) {
                        scramble.swap(left++, right--)
                    }
                }
                is RotateBasedUponPosition -> {
                    val index = scramble.indexOfFirst { it == instruction.x }
                    val amount = 1 + index + if (index > 3) 1 else 0

                    repeat(amount) {
                        scramble = (scramble.takeLast(1) + scramble.dropLast(1)).toMutableList()
                    }
                }
                is RotateLeft -> {
                    scramble = (scramble.drop(instruction.amount) + scramble.take(instruction.amount)).toMutableList()
                }
                is RotateRight -> {
                    scramble = (scramble.takeLast(instruction.amount) + scramble.dropLast(instruction.amount)).toMutableList()
                }
                is SwapLetter -> {
                    scramble.swap(scramble.indexOfFirst { it == instruction.x }, scramble.indexOfFirst { it == instruction.y })
                }
                is SwapPosition -> scramble.swap(instruction.x, instruction.y)
            }
        }
        return scramble.joinToString("")
    }

    private fun MutableList<Char>.swap(left: Int, right: Int) {
        val temp = this[left]
        this[left] = this[right]
        this[right] = temp
    }

    private val <T> List<T>.head: T
        get() = first()

    private val <T> List<T>.tail: List<T>
        get() = drop(1)

    private fun <T> List<T>.permutations(): List<List<T>> {
        if (isEmpty()) return emptyList()
        if (size == 1) return listOf(this)

        return tail.permutations()
            .fold(mutableListOf()) { acc, perm ->
                (0..perm.size).mapTo(acc) { i ->
                    perm.subList(0, i) + head + perm.subList(i, perm.size)
                }
            }
    }

}

fun main() {
    execute(Day21::class)
}
