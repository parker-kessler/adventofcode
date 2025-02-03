package advent.of.code

import java.util.PriorityQueue
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.max
import kotlin.math.min

@Files("/Day22.txt")
class Day22 : Puzzle<Day22.Boss, Int> {

    enum class Spell(val mana: Int, val duration: Int) {
        MAGIC_MISSILE(53, 0),
        DRAIN(73, 0),
        SHIELD(113, 6),
        POISON(173, 6),
        RECHARGE(229, 5)
    }

    data class Boss(val hitPoints: Int, val damage: Int)

    override fun parse(input: List<String>): Boss {
        return Boss(
            hitPoints = input[0].split(" ").last().toInt(),
            damage = input[1].split(" ").last().toInt()
        )
    }

    override fun partOne(input: Boss): Int = solve(input, false)

    override fun partTwo(input: Boss): Int = solve(input, true)

    private fun solve(boss: Boss, hard: Boolean): Int {
        val wizards = PriorityQueue { a: Wizard, b: Wizard ->
            b.manaSpent.compareTo(a.manaSpent)
        }
        val minMana = AtomicInteger(Int.MAX_VALUE)
        wizards.add(Wizard(50, 500, arrayOf(boss.hitPoints, boss.damage).toIntArray()))
        while (wizards.isNotEmpty()) {
            val curr = wizards.poll()
            if (hard && curr.hp-- <= 0) {
                continue
            }
            curr.applyEffect()
            Spell.entries.filter { curr.canCast(it) }.forEach { spell ->
                val next = curr.clone()
                next.castSpell(spell)
                next.applyEffect()

                if (next.boss[0] <= 0) {
                    minMana.set(min(minMana.get(), next.manaSpent))
                    wizards.removeIf { w: Wizard -> w.manaSpent > minMana.get() }
                } else {
                    next.hp -= max(1, (next.boss[1] - next.armor))
                    if (next.hp > 0 && next.mana > 0 && next.manaSpent < minMana.get()) wizards.add(next)
                }
            }
        }
        return minMana.get()
    }

    internal class Wizard(
        var hp: Int, var mana: Int,
        var boss: IntArray
    ) : Cloneable {
        var armor: Int = 0
        var manaSpent: Int = 0

        private var activeSpells = mutableMapOf<Spell, Int>()

        fun canCast(spell: Spell) = mana >= spell.mana && !activeSpells.containsKey(spell)

        fun castSpell(spell: Spell) {
            mana -= spell.mana
            manaSpent += spell.mana
            when (spell) {
                Spell.MAGIC_MISSILE -> boss[0] -= 4
                Spell.DRAIN -> {
                    hp += 2
                    boss[0] -= 2
                }

                else -> activeSpells[spell] = spell.duration
            }
        }

        fun applyEffect() {
            armor = 0
            activeSpells.keys.forEach { spell ->
                when (spell) {
                    Spell.SHIELD -> armor = 7
                    Spell.POISON -> boss[0] -= 3
                    Spell.RECHARGE -> mana += 101
                    else -> throw Error("Unknown spell $spell")
                }
            }
            activeSpells = activeSpells.mapValues { it.value - 1 }.filterValues { it > 0 }.toMutableMap()
        }

        public override fun clone(): Wizard {
            val neu = Wizard(hp, mana, boss.clone())
            neu.armor = armor
            neu.manaSpent = manaSpent
            neu.activeSpells = activeSpells.toMap().toMutableMap()
            return neu
        }
    }

}

fun main() {
    execute(Day22::class)
}
