package advent.of.code

import kotlin.math.max

@Files("/Day21.txt")
class Day21 : Puzzle<Pair<Day21.Shop, Day21.Boss>, Int> {

    companion object {
        private const val HIT_POINTS = 100
        private val EMPTY_ITEM = Item(cost = 0, damage = 0, armor = 0)
        private val shop = """
            Weapons:    Cost  Damage  Armor
            Dagger        8     4       0
            Shortsword   10     5       0
            Warhammer    25     6       0
            Longsword    40     7       0
            Greataxe     74     8       0
    
            Armor:      Cost  Damage  Armor
            Leather      13     0       1
            Chainmail    31     0       2
            Splintmail   53     0       3
            Bandedmail   75     0       4
            Platemail   102     0       5
    
            Rings:      Cost  Damage  Armor
            Damage +1    25     1       0
            Damage +2    50     2       0
            Damage +3   100     3       0
            Defense +1   20     0       1
            Defense +2   40     0       2
            Defense +3   80     0       3
        """.trimIndent()
    }

    data class Shop(val weapons: List<Item>, val armors: List<Item>, val rings: List<Item>)
    data class Item(val cost: Int, val damage: Int, val armor:Int) {
        operator fun plus(item: Item): Item {
            return Item(cost = cost + item.cost, damage = damage + item.damage, armor = armor + item.armor)
        }
    }
    data class Boss(val hitPoints: Int, val damage: Int, val armor: Int)

    override fun parse(input: List<String>): Pair<Shop, Boss> {
        val regex = "[0-9]+".toRegex()

        val weapons = mutableListOf<Item>()
        val armors = mutableListOf<Item>()
        val rings = mutableListOf<Item>()

        var currentItems = weapons

        shop.split("\n").filter { it.isNotBlank() }.forEach { line ->

            when {
                "Weapons:" in line -> currentItems = weapons
                "Armor:" in line -> currentItems = armors
                "Rings:" in line -> currentItems = rings
                else -> {
                    regex.findAll(line).map { it.value.toInt() }.toList().asReversed().let {
                        currentItems.add(Item(cost = it[2], damage = it[1], armor = it[0]))
                    }
                }
            }
        }

        return Shop(weapons, armors, rings) to Boss(
            hitPoints = regex.find(input[0])?.value?.toInt() ?: 0,
            damage = regex.find(input[1])?.value?.toInt() ?: 0,
            armor = regex.find(input[2])?.value?.toInt() ?: 0
        )
    }

    override fun partOne(input: Pair<Shop, Boss>): Int {
        val (shop, boss) = input
        return getCombinations(shop).filter { doIWin(it, boss) }.minOf { it.cost }

    }

    override fun partTwo(input: Pair<Shop, Boss>): Int {
        val (shop, boss) = input
        return getCombinations(shop).filterNot { doIWin(it, boss) }.maxOf { it.cost }
    }

    private fun getCombinations(shop: Shop): List<Item> {
        val combinations = mutableListOf<Item>()
        shop.weapons.forEach { weapon ->
            (shop.armors + EMPTY_ITEM).forEach { armor ->
                (shop.rings + EMPTY_ITEM).forEachIndexed { firstRingIndex, firstRing ->
                    (shop.rings + EMPTY_ITEM).forEachIndexed { secondRingIndex, secondRing ->
                        if (firstRingIndex != secondRingIndex) {
                            combinations += (weapon + armor + firstRing + secondRing)
                        }
                    }
                }
                combinations += (weapon + armor)
            }
        }
        return combinations
    }

    private fun doIWin(item: Item, boss: Boss): Boolean {
        val myDamage = max(1, item.damage - boss.armor)
        val bossDamage = max(1, boss.damage - item.armor)
        val myHits = (boss.hitPoints + myDamage - 1) / myDamage
        val bossHits = (HIT_POINTS + bossDamage - 1) / bossDamage

        return myHits <= bossHits
    }
}

fun main() {
    execute(Day21::class)
}
