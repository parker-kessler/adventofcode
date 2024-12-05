package advent.of.code

@Files("/Day01.txt")
class Day01 : Puzzle<List<String>, Long> {

    override fun parse(input: List<String>): List<String> = input

    override fun partOne(input: List<String>): Long {
        val elves = mutableListOf<Long>()
        var elf = 0L
        input.forEach { line ->
            if (line.isBlank()) {
                elves.add(elf)
                elf = 0
            } else {
                elf += line.toLong()
            }
        }
        elves.add(elf)

        return elves.max()
    }

    override fun partTwo(input: List<String>): Long {
        val elves = mutableListOf<Long>()
        var elf = 0L
        input.forEach { line ->
            if (line.isBlank()) {
                elves.add(elf)
                elf = 0
            } else {
                elf += line.toLong()
            }
        }
        elves.add(elf)

        return elves.sorted().takeLast(3).sum()
    }
}

fun main() {
    execute(Day01::class)
}
