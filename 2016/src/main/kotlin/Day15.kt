package advent.of.code

@Files("/Day15.txt")
class Day15 : Puzzle<List<Day15.Disc>, Int> {

    data class Disc(val positions: Int, val currentPosition: Int)

    override fun parse(input: List<String>) = input.map { line ->
        val fields = "-?[0-9]+".toRegex().findAll(line).map { it.value.toInt() }.toList()
        Disc(fields[1], fields[3])
    }

    override fun partOne(input: List<Disc>): Int = findStartTime(input)

    override fun partTwo(input: List<Disc>): Int = findStartTime(input + Disc(11, 0))

    private fun findStartTime(startingDiscs: List<Disc>): Int {
        var discs = startingDiscs.mapIndexed { index, disc ->
            disc.copy(currentPosition = (disc.currentPosition + index) % disc.positions)
        }
        val max = discs.maxOf { it.positions }
        var time = 0
        while (discs.sumOf { it.currentPosition } != 0) {
            discs = discs.map { it.copy(currentPosition = (it.currentPosition + max) % it.positions) }
            time++
        }
        return max * time - 1
    }

}

fun main() {
    execute(Day15::class)
}
