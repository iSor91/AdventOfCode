package com.isor.aoc2021

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.Year

fun main() {
    Day8().executeGoals()
}

@Year(2021)
//@TestResources
class Day8: AOC_Runner() {

    data class Display(val tries: List<String>, val digits: List<String>)

    val displays: MutableList<Display> = mutableListOf()

    init {
        allLines.map {
            val split = it.split(Regex("\\|"))
            val tries = split[0].split(Regex(" ")).filter { tri -> tri.isNotBlank() }
            val digits = split[1].split(Regex(" ")).filter { digit -> digit.isNotBlank() }
            displays.add(Display(tries,digits))
        }
    }

    override fun executeGoal_1() {
        println(displays.map { it.digits.filter { d -> (d.length == 2) or (d.length == 4) or (d.length == 3) or (d.length == 7) }.count() }.sum())
    }

    override fun executeGoal_2() {
        println(displays.map {display -> display.digits.map { s -> deductValue(s, calculateWiring(display)) }.joinToString("").toInt()}.sum())
    }

    /**
     * Calculates the possible wiring of the segments, and returns a list.
     * The list contains the following 7 segment displays indexes with the possible wire representation as a String e.g. "abd".
     *
     *  0000
     * 1    2
     * 1    2
     *  3333
     * 4    5
     * 4    5
     *  6666
     *
     *  The results will be such, that the
     *  - 2/5 have the same value
     *  - 4/6 have the same value
     *  - 1/3 have the same value
     *  - 0 has one value
     */
    private fun calculateWiring(display: Display) : List<String>{

        val digits: MutableList<String> = MutableList(7) { "" }
        val allDigits = display.digits + display.tries
        val oneSegments = getSegments(allDigits, 2)
        digits[2] = oneSegments
        digits[5] = oneSegments
        val sevenSegments = getSegments(allDigits, 3)
        digits[0] = sevenSegments - oneSegments

        val fourSegments = getSegments(allDigits, 4)
        digits[1] = fourSegments - oneSegments
        digits[3] = fourSegments - oneSegments

        val eightSegments = getSegments(allDigits, 7)
        digits[4] = eightSegments - oneSegments - sevenSegments - fourSegments
        digits[6] = eightSegments - oneSegments - sevenSegments - fourSegments

        return digits
    }

    private fun getSegments(allDigits: List<String>, length: Int) =
            allDigits.filter { it.length == length }.flatMap { it.chunked(1) }.distinct().sorted().joinToString("")

    override operator fun String.minus(toRemove:String): String {
        return this.chunked(1).filter { !toRemove.contains(it) }.distinct().sorted().joinToString("")
    }


    /**
     * Deducts the intended value of the String according these rules:
     * according length 1,4,7,8 can ben determined
     *
     * in other cases more elaborate deduction is required, using the calculateWiring's result
     *
     *  0000
     * 1    2
     * 1    2
     *  3333
     * 4    5
     * 4    5
     *  6666
     *
     * if the length is 5, the possible values are 2,3,5
     *  - if the values of segments 1/3 are both present, but only one from the 2/5, it is a 2
     *  - if the values of segments 4/6 are both present, but only one from the 2/5, it is a 5
     *  - 3 otherwise
     *
     * if the length is 6, the possible values are 0,6,9
     *  - if only one value of the 1/3 segments is present, it is a 0
     *  - if only one value of the 4/6 segments is present, it is a 9
     *  - 6 otherwise
     */
    private fun deductValue(s: String, segments: List<String>): Int {
        return when(s.length) {
            2 -> 1
            3 -> 7
            4 -> 4
            7 -> 8
            5 -> {
                if((segments[1] - s).length == 1 && (segments[2]- s).length == 1)return 2
                if((segments[4]- s).length == 1 && (segments[2]- s).length == 1)return 5
                return 3
            }
            else -> {
                if((segments[1]-s).length == 1) return 0
                if((segments[4]-s).length == 1) return 9
                return 6
            }
        }
    }

}