package advent.of.code

data class Directory(
    val name: String,
    val children: MutableList<Directory> = mutableListOf(),
    val files: MutableList<File> = mutableListOf(),
) {
    fun computeSize(): Long = files.sumOf { it.size } + children.sumOf { it.computeSize() }
}

data class File(val name: String, val size: Long)

@Files("/Day07.txt")
class Day07 : Puzzle<Directory, Long> {

    override fun parse(input: List<String>): Directory {
        val list = mutableListOf(Directory("/"))
        input.forEach { line ->

            when {
                line.startsWith("$ cd") -> {
                    when(val name = line.removePrefix("$ cd ")) {
                        ".." -> if (list.size > 1) list.removeLast()
                        "/" -> while (list.size > 1) list.removeLast()
                        else -> list.add(list.last().children.first { it.name == name })
                    }
                }
                line.startsWith("dir") -> {
                    list.last().children.add(Directory(name = line.removePrefix("dir ")))
                }
                line[0].isDigit() -> {
                    val (size, name) = line.split(" ")
                    list.last().files.add(File(name = name, size = size.toLong()))
                }
            }
        }
        return list.first()
    }

    override fun partOne(input: Directory): Long {
        val list = mutableListOf(input)
        var sum = 0L
        while (list.isNotEmpty()) {
            val directory = list.removeLast()
            list.addAll(directory.children)

            val size = directory.computeSize()
            if (size <= 100_000) {
                sum += size
            }
        }
        return sum
    }

    override fun partTwo(input: Directory): Long {
        val sizes = mutableListOf<Long>()
        val list = mutableListOf(input)

        while (list.isNotEmpty()) {
            val directory = list.removeLast()
            list.addAll(directory.children)

            sizes.add(directory.computeSize())
        }

        return sizes.sorted().first{ 40_000_000 >= sizes.first() - it }
    }
}

fun main() {
    execute(Day07::class)
}
