package advent.of.code

@Files("/Day22.txt")
class Day22 : Puzzle<List<Long>, Long> {

    override fun parse(input: List<String>) = input.map { it.toLong() }

    override fun partOne(input: List<Long>): Long {
        return input.sumOf {
            var secret = it
            repeat(2000) {
                secret = nextSecret(secret)
            }
            secret
        }
    }

    override fun partTwo(input: List<Long>): Long {
        return input.flatMap { analyzeSecret(it) }
            .groupBy { it.first }
            .mapValues { value -> value.value.map { it.second }.reduce { acc, i -> acc + i } }
            .values.max()
    }

    private fun analyzeSecret(secret: Long): List<Pair<String, Long>> {
        val map = mutableMapOf<String, Long>()
        val list = mutableListOf<Long>()
        var value = secret
        repeat(2000) {
            val temp = nextSecret(value)
            list.add(temp % 10 - value % 10)
            if (list.size > 4) {
                list.removeFirst()
            }
            if (list.toSet().size == 4) {
                map.putIfAbsent(list.joinToString(""), temp % 10)
            }
            value = temp
        }
        return map.toList()
    }

    private fun nextSecret(secret: Long): Long = mixAndPrune(secret * 64, secret)
        .let { mixAndPrune(it / 32, it) }
        .let { mixAndPrune(it * 2048, it) }

    private fun mixAndPrune(value: Long, secret: Long): Long = (value xor secret) % 16777216

}


fun main() {
    execute(Day22::class)
}
