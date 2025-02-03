package advent.of.code

@Files("/Day08.txt")
class Day08 : Puzzle<List<Day08.Instruction>, Int> {

    companion object {
        const val WIDTH = 50
        const val HEIGHT = 6
        const val ON = '#'
    }

    sealed interface Instruction

    data class Rect(val column: Int, val row: Int) : Instruction
    data class RotateColumn(val x: Int, val by: Int) : Instruction
    data class RotateRow(val y: Int, val by: Int) : Instruction

    override fun parse(input: List<String>) = input.map { line ->
        val nums = "[0-9]+".toRegex().findAll(line).map { it.value.toInt() }.toList()
        when {
            "rect" in line -> Rect(nums[0], nums[1])
            "column" in line -> RotateColumn(nums[0], nums[1])
            "row" in line -> RotateRow(nums[0], nums[1])
            else -> throw Error("Invalid input")
        }
    }

    override fun partOne(input: List<Instruction>): Int {
        return createGrid(input).sumOf { column -> column.count { it == ON } }
    }

    override fun partTwo(input: List<Instruction>): Int {
        printGrid(createGrid(input))
        return 0
    }

    private fun createGrid(instructions: List<Instruction>): Array<Array<Char>> {
        return Array(HEIGHT) { Array(WIDTH) { ' ' } }.apply {
            instructions.forEach { instruction ->
                when(instruction) {
                    is Rect -> {
                        for (x in 0 until instruction.row) {
                            for (y in 0 until instruction.column) {
                                this[x][y] = ON
                            }
                        }
                    }
                    is RotateColumn -> {
                        val before = (0 until HEIGHT).joinToString("") { this[it][instruction.x].toString() }
                        rotateString(before, instruction.by).forEachIndexed { index, c -> this[index][instruction.x] = c }
                    }
                    is RotateRow -> {
                        val before = (0 until WIDTH).joinToString("") { this[instruction.y][it].toString() }
                        rotateString(before, instruction.by).forEachIndexed { index, c -> this[instruction.y][index] = c }
                    }
                }
            }
        }
    }

    private fun rotateString(string: String, amount: Int): String {
        return string.takeLast(amount) + string.dropLast(amount)
    }

    private fun printGrid(grid: Array<Array<Char>>) {
        println(grid.joinToString("\n") { row -> row.joinToString("") })
    }
}

fun main() {
    execute(Day08::class)
}
