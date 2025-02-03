package advent.of.code

@Files("/Day16.txt")
class Day16 : Puzzle<String, String> {

    override fun parse(input: List<String>) = input.first()

    override fun partOne(input: String): String = fillDisk(input, 272)

    override fun partTwo(input: String): String = fillDisk(input, 35651584)

    private fun fillDisk(input: String, size: Int): String {
        var current = input
        while (current.length < size) {
            current = next(current)
        }
        return checkSum(current.take(size))
    }

    private fun next(input: String): String {
        val sb = StringBuilder(input).append('0')
        input.reversed().forEach {
            sb.append(if (it == '0') '1' else '0')
        }
        return sb.toString()
    }

    private fun checkSum(input: String): String {
        if (input.length % 2 == 1) {
            return input
        }
        val sb = StringBuilder()
        input.chunked(2).forEach {
            sb.append(if (it.first() == it.last()) '1' else '0')
        }
        return checkSum(sb.toString())
    }
}

fun main() {
    execute(Day16::class)
}
