package advent.of.code

data class Translation(
    val destination: Long,
    val source: Long,
    val size: Long
)

@Files("/Day05.txt")
class Day05 : Puzzle<List<String>, Long> {

    override fun parse(input: List<String>): List<String> = input

    override fun partOne(input: List<String>): Long {
        val seeds = mutableListOf<Long>()
        val maps = mutableListOf<MutableList<Translation>>()

        input.forEach { line ->
            when {
                line.startsWith("seeds:") -> "[0-9]+".toRegex().findAll(line).forEach { seeds.add(it.value.toLong()) }
                line.isBlank() -> maps.add(mutableListOf())
                line.isNotBlank() && !line.contains("map") -> line.split(" ").let { maps.last().add(Translation(it[0].toLong(), it[1].toLong(), it[2].toLong())) }
            }
        }

        return seeds.minOf { seed ->
            var current = seed
            maps.forEach { translations ->
                translations.find { it.source <= current && current < it.source + it.size }
                    ?.let { current = it.destination + (current - it.source) }
            }
            current
        }
    }

    override fun partTwo(input: List<String>): Long = 0
}

fun main() {
    execute(Day05::class)
}
