package advent.of.code

@Files("/Day10.txt")
class Day10 : Puzzle<String, Int> {

    private class Element(val name: String, val sequence: String, val decays: String)

    private val elements = listOf(
        Element(name = "H", sequence = "22", decays = "H"),
        Element(name = "He", sequence = "13112221133211322112211213322112", decays = "Hf.Pa.H.Ca.Li"),
        Element(name = "Li", sequence = "312211322212221121123222112", decays = "He"),
        Element(name = "Be", sequence = "111312211312113221133211322112211213322112", decays = "Ge.Ca.Li"),
        Element(name = "B", sequence = "1321132122211322212221121123222112", decays = "Be"),
        Element(name = "C", sequence = "3113112211322112211213322112", decays = "B"),
        Element(name = "N", sequence = "111312212221121123222112", decays = "C"),
        Element(name = "O", sequence = "132112211213322112", decays = "N"),
        Element(name = "F", sequence = "31121123222112", decays = "O"),
        Element(name = "Ne", sequence = "111213322112", decays = "F"),
        Element(name = "Na", sequence = "123222112", decays = "Ne"),
        Element(name = "Mg", sequence = "3113322112", decays = "Pm.Na"),
        Element(name = "Al", sequence = "1113222112", decays = "Mg"),
        Element(name = "Si", sequence = "1322112", decays = "Al"),
        Element(name = "P", sequence = "311311222112", decays = "Ho.Si"),
        Element(name = "S", sequence = "1113122112", decays = "P"),
        Element(name = "Cl", sequence = "132112", decays = "S"),
        Element(name = "Ar", sequence = "3112", decays = "Cl"),
        Element(name = "K", sequence = "1112", decays = "Ar"),
        Element(name = "Ca", sequence = "12", decays = "K"),
        Element(name = "Sc", sequence = "3113112221133112", decays = "Ho.Pa.H.Ca.Co"),
        Element(name = "Ti", sequence = "11131221131112", decays = "Sc"),
        Element(name = "V", sequence = "13211312", decays = "Ti"),
        Element(name = "Cr", sequence = "31132", decays = "V"),
        Element(name = "Mn", sequence = "111311222112", decays = "Cr.Si"),
        Element(name = "Fe", sequence = "13122112", decays = "Mn"),
        Element(name = "Co", sequence = "32112", decays = "Fe"),
        Element(name = "Ni", sequence = "11133112", decays = "Zn.Co"),
        Element(name = "Cu", sequence = "131112", decays = "Ni"),
        Element(name = "Zn", sequence = "312", decays = "Cu"),
        Element(name = "Ga", sequence = "13221133122211332", decays = "Eu.Ca.Ac.H.Ca.Zn"),
        Element(name = "Ge", sequence = "31131122211311122113222", decays = "Ho.Ga"),
        Element(name = "As", sequence = "11131221131211322113322112", decays = "Ge.Na"),
        Element(name = "Se", sequence = "13211321222113222112", decays = "As"),
        Element(name = "Br", sequence = "3113112211322112", decays = "Se"),
        Element(name = "Kr", sequence = "11131221222112", decays = "Br"),
        Element(name = "Rb", sequence = "1321122112", decays = "Kr"),
        Element(name = "Sr", sequence = "3112112", decays = "Rb"),
        Element(name = "Y", sequence = "1112133", decays = "Sr.U"),
        Element(name = "Zr", sequence = "12322211331222113112211", decays = "Y.H.Ca.Tc"),
        Element(name = "Nb", sequence = "1113122113322113111221131221", decays = "Er.Zr"),
        Element(name = "Mo", sequence = "13211322211312113211", decays = "Nb"),
        Element(name = "Tc", sequence = "311322113212221", decays = "Mo"),
        Element(name = "Ru", sequence = "132211331222113112211", decays = "Eu.Ca.Tc"),
        Element(name = "Rh", sequence = "311311222113111221131221", decays = "Ho.Ru"),
        Element(name = "Pd", sequence = "111312211312113211", decays = "Rh"),
        Element(name = "Ag", sequence = "132113212221", decays = "Pd"),
        Element(name = "Cd", sequence = "3113112211", decays = "Ag"),
        Element(name = "In", sequence = "11131221", decays = "Cd"),
        Element(name = "Sn", sequence = "13211", decays = "In"),
        Element(name = "Sb", sequence = "3112221", decays = "Pm.Sn"),
        Element(name = "Te", sequence = "1322113312211", decays = "Eu.Ca.Sb"),
        Element(name = "I", sequence = "311311222113111221", decays = "Ho.Te"),
        Element(name = "Xe", sequence = "11131221131211", decays = "I"),
        Element(name = "Cs", sequence = "13211321", decays = "Xe"),
        Element(name = "Ba", sequence = "311311", decays = "Cs"),
        Element(name = "La", sequence = "11131", decays = "Ba"),
        Element(name = "Ce", sequence = "1321133112", decays = "La.H.Ca.Co"),
        Element(name = "Pr", sequence = "31131112", decays = "Ce"),
        Element(name = "Nd", sequence = "111312", decays = "Pr"),
        Element(name = "Pm", sequence = "132", decays = "Nd"),
        Element(name = "Sm", sequence = "311332", decays = "Pm.Ca.Zn"),
        Element(name = "Eu", sequence = "1113222", decays = "Sm"),
        Element(name = "Gd", sequence = "13221133112", decays = "Eu.Ca.Co"),
        Element(name = "Tb", sequence = "3113112221131112", decays = "Ho.Gd"),
        Element(name = "Dy", sequence = "111312211312", decays = "Tb"),
        Element(name = "Ho", sequence = "1321132", decays = "Dy"),
        Element(name = "Er", sequence = "311311222", decays = "Ho.Pm"),
        Element(name = "Tm", sequence = "11131221133112", decays = "Er.Ca.Co"),
        Element(name = "Yb", sequence = "1321131112", decays = "Tm"),
        Element(name = "Lu", sequence = "311312", decays = "Yb"),
        Element(name = "Hf", sequence = "11132", decays = "Lu"),
        Element(name = "Ta", sequence = "13112221133211322112211213322113", decays = "Hf.Pa.H.Ca.W"),
        Element(name = "W", sequence = "312211322212221121123222113", decays = "Ta"),
        Element(name = "Re", sequence = "111312211312113221133211322112211213322113", decays = "Ge.Ca.W"),
        Element(name = "Os", sequence = "1321132122211322212221121123222113", decays = "Re"),
        Element(name = "Ir", sequence = "3113112211322112211213322113", decays = "Os"),
        Element(name = "Pt", sequence = "111312212221121123222113", decays = "Ir"),
        Element(name = "Au", sequence = "132112211213322113", decays = "Pt"),
        Element(name = "Hg", sequence = "31121123222113", decays = "Au"),
        Element(name = "Tl", sequence = "111213322113", decays = "Hg"),
        Element(name = "Pb", sequence = "123222113", decays = "Tl"),
        Element(name = "Bi", sequence = "3113322113", decays = "Pm.Pb"),
        Element(name = "Po", sequence = "1113222113", decays = "Bi"),
        Element(name = "At", sequence = "1322113", decays = "Po"),
        Element(name = "Rn", sequence = "311311222113", decays = "Ho.At"),
        Element(name = "Fr", sequence = "1113122113", decays = "Rn"),
        Element(name = "Ra", sequence = "132113", decays = "Fr"),
        Element(name = "Ac", sequence = "3113", decays = "Ra"),
        Element(name = "Th", sequence = "1113", decays = "Ac"),
        Element(name = "Pa", sequence = "13", decays = "Th"),
        Element(name = "U", sequence = "3", decays = "Pa"),
    )

    override fun parse(input: List<String>) = input.first()

    override fun partOne(input: String): Int = decay(input, 40)

    override fun partTwo(input: String): Int = decay(input, 50)

    private fun decay(sequence: String, amount: Int): Int {
        val byName = elements.associateBy { it.name }
        var frequencies = mapOf(elements.first { it.sequence == sequence } to 1)
        repeat(amount) {
            frequencies = mutableMapOf<Element, Int>().apply {
                frequencies.entries.forEach { (element, count) ->
                    element.decays.split(".").mapNotNull { byName[it] }.forEach { decayedElement ->
                        compute(decayedElement) { _, v -> if (v == null) count else v + count }
                    }
                }
            }
        }
        return frequencies.entries.sumOf { (element, count) -> element.sequence.length * count }
    }

}

fun main() {
    execute(Day10::class)
}
