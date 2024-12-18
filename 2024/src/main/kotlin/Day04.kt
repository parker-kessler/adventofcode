package advent.of.code

@Files("/Day04.txt")
class Day04 : Puzzle<List<String>, Int> {
    override fun parse(input: List<String>): List<String> = input

    override fun partOne(input: List<String>): Int {
        var sum = 0
        for (x in input.indices) {
            for (y in input[x].indices) {
                if (input[x][y] == 'X') {
                    if (input.safe(x - 1, y) == 'M' && input.safe(x - 2, y) == 'A' && input.safe(x - 3, y) == 'S') sum++
                    if (input.safe(x, y - 1) == 'M' && input.safe(x, y - 2) == 'A' && input.safe(x, y - 3) == 'S') sum++
                    if (input.safe(x + 1, y) == 'M' && input.safe(x + 2, y) == 'A' && input.safe(x + 3, y) == 'S') sum++
                    if (input.safe(x, y + 1) == 'M' && input.safe(x, y + 2) == 'A' && input.safe(x, y + 3) == 'S') sum++

                    if (input.safe(x - 1,y - 1) == 'M' && input.safe(x - 2, y - 2) == 'A' && input.safe(x - 3, y - 3) == 'S') sum++
                    if (input.safe(x - 1,y + 1) == 'M' && input.safe(x - 2, y + 2) == 'A' && input.safe(x - 3, y + 3) == 'S') sum++
                    if (input.safe(x + 1,y + 1) == 'M' && input.safe(x + 2, y + 2) == 'A' && input.safe(x + 3, y + 3) == 'S') sum++
                    if (input.safe(x + 1,y - 1) == 'M' && input.safe(x + 2, y - 2) == 'A' && input.safe(x + 3, y - 3) == 'S') sum++

                }
            }
        }
        return sum
    }

    override fun partTwo(input: List<String>): Int {
        var sum = 0
        for (x in 1 until input.size - 1) {
            for (y in 1 until input[x].length - 1) {
                if (input[x][y] == 'A') {
                    if (input[x - 1][y - 1] == 'M' && input[x - 1][y + 1] == 'M' && input[x + 1][y - 1] == 'S' && input[x + 1][y + 1] == 'S') sum++
                    if (input[x - 1][y - 1] == 'S' && input[x - 1][y + 1] == 'M' && input[x + 1][y - 1] == 'S' && input[x + 1][y + 1] == 'M') sum++
                    if (input[x - 1][y - 1] == 'S' && input[x - 1][y + 1] == 'S' && input[x + 1][y - 1] == 'M' && input[x + 1][y + 1] == 'M') sum++
                    if (input[x - 1][y - 1] == 'M' && input[x - 1][y + 1] == 'S' && input[x + 1][y - 1] == 'M' && input[x + 1][y + 1] == 'S') sum++
                }
            }
        }
        return sum
    }

    private fun List<String>.safe(x: Int, y: Int): Char =
        if (x < 0 || x >= this.size || y < 0 || y >= this[0].length) '.' else this[x][y]

}

fun main() {
    execute(Day04::class)
}
