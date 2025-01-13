package advent.of.code

@Files("/Day17.txt")
class Day17 : Puzzle<List<Int>, Int> {

    companion object {
        const val TOTAL_EGGNOG = 150
    }

    override fun parse(input: List<String>) = input.map { it.toInt() }

    override fun partOne(input: List<Int>): Int = countAll(input, 0, listOf()).size

    override fun partTwo(input: List<Int>): Int = countAll(input, 0, listOf()).let { lists ->
        val amountOfContainers = lists.minOf { it.size }
        lists.filter { it.size == amountOfContainers }.size
    }

    private fun countAll(containers: List<Int>, index: Int, list: List<Int>): List<List<Int>> {
        if (index == containers.size)
            return if (list.sum() == TOTAL_EGGNOG) listOf(list) else emptyList()

        val sumIncluded = countAll(containers, index + 1, list + containers[index])
        val sumExcluded = countAll(containers, index + 1, list)

        return sumExcluded + sumIncluded
    }
}

fun main() {
    execute(Day17::class)
}
