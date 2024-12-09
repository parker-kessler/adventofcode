package advent.of.code

data class File(val position: Int, val size: Int, val id: Int)
data class Space(val position: Int, val size: Int)

@Files("/Day09.txt")
class Day09 : Puzzle<List<Int>, Long> {

    override fun parse(input: List<String>): List<Int> = input.first().map { it - '0' }

    override fun partOne(input: List<Int>): Long {
        val list = mutableListOf<Int>()

        input.forEachIndexed { index, size ->
            val id = if (index % 2 == 0) index / 2 else -1
            repeat(size) { list.add(id) }
        }

        var left = 0
        var right = list.size - 1
        while (left < right) {
            if (list[left] != -1) {
                left++
            } else if (list[right] == -1) {
                right--
            } else {
                list[left++] = list[right]
                list[right--] = -1
            }
        }

        return list.checkSum()
    }

    override fun partTwo(input: List<Int>): Long {
        val files = mutableListOf<File>()
        val spaces = mutableListOf<Space>()
        var position = 0

        val list = mutableListOf<Int>()
        input.forEachIndexed { index, size ->
            val id = if (index % 2 == 0) {
                files.add(File(position = position, size = size, id = index / 2))
                index / 2
            } else {
                spaces.add(Space(position = position, size = size))
                -1
            }
            repeat(size) { list.add(position++, id) }
        }

        files.reversed().forEach { file ->
            for ((index, space) in spaces.withIndex()) {
                if (space.position > file.position) break
                if (file.size > space.size) continue

                if (file.size == space.size) {
                    spaces.removeAt(index)
                } else {
                    spaces[index] = Space(position = space.position + file.size, size = space.size - file.size)
                }

                for (i in 0 until file.size) {
                    list[file.position + i] = -1
                    list[space.position + i] = file.id
                }
                break
            }
        }

        return list.checkSum()
    }

    private fun List<Int>.checkSum(): Long = mapIndexed { index, i ->  if (i == -1) 0 else index * i.toLong() }.sum()

}

fun main() {
    execute(Day09::class)
}
