package com.isor.aoc2015

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.Year
import java.util.function.Function

/**
 * --- Day 6: Probably a Fire Hazard ---
Because your neighbors keep defeating you in the holiday house decorating contest year after year, you've decided to deploy one million lights in a 1000x1000 grid.

Furthermore, because you've been especially nice this year, Santa has mailed you instructions on how to display the ideal lighting configuration.

Lights in your grid are numbered from 0 to 999 in each direction; the lights at each corner are at 0,0, 0,999, 999,999, and 999,0. The instructions include whether to turn on, turn off, or toggle various inclusive ranges given as coordinate pairs. Each coordinate pair represents opposite corners of a rectangle, inclusive; a coordinate pair like 0,0 through 2,2 therefore refers to 9 lights in a 3x3 square. The lights all start turned off.

To defeat your neighbors this year, all you have to do is set up your lights by doing the instructions Santa sent you in order.

For example:

turn on 0,0 through 999,999 would turn on (or leave on) every light.
toggle 0,0 through 999,0 would toggle the first line of 1000 lights, turning off the ones that were on, and turning on the ones that were off.
turn off 499,499 through 500,500 would turn off (or leave off) the middle four lights.
After following the instructions, how many lights are lit?

Your puzzle answer was 543903.

--- Part Two ---
You just finish implementing your winning light pattern when you realize you mistranslated Santa's message from Ancient Nordic Elvish.

The light grid you bought actually has individual brightness controls; each light can have a brightness of zero or more. The lights all start at zero.

The phrase turn on actually means that you should increase the brightness of those lights by 1.

The phrase turn off actually means that you should decrease the brightness of those lights by 1, to a minimum of zero.

The phrase toggle actually means that you should increase the brightness of those lights by 2.

What is the total brightness of all lights combined after following Santa's instructions?

For example:

turn on 0,0 through 0,0 would increase the total brightness by 1.
toggle 0,0 through 999,999 would increase the total brightness by 2000000.
Your puzzle answer was 14687245.
 */

fun main() {
    Day_6().executeGoals()
}

@Year(2015)
//@TestResources
class Day_6 : AOC_Runner() {

    private val coordinates = Regex("\\d\\d?\\d?,\\d\\d?\\d?")

    private val instructions1 = hashMapOf<String, Function<Int, Int>>(
            "turn on" to Function {1},
            "turn off" to Function {0},
            "toggle" to Function {if(it == 1) 0 else 1}
    )

    private val instructions2 = hashMapOf<String, Function<Int, Int>>(
            "turn on" to Function {it+1},
            "turn off" to Function {
                if(it-1 < 0) 0 else it-1
            },
            "toggle" to Function {it+2}
    )

    override fun executeGoal_1() {
        lit(instructions1)
    }

    override fun executeGoal_2() {
        lit(instructions2)
    }

    private fun lit(instructions: HashMap<String, Function<Int, Int>>) {
        val array = Array(1000) { Array(1000) { 0 } }

        allLines.forEach {
            val findAll = coordinates.findAll(it)
            val indexOfFirst = it.indexOfFirst { c -> c.isDigit() }
            val substring = it.substring(0, indexOfFirst - 1)
            val startCoordinate = findAll.elementAt(0)
            val endCoordinate = findAll.elementAt(1)
            array.update(instructions[substring], startCoordinate.value, endCoordinate.value)
        }
        var i = 0
        for (array in array) {
            for (value in array) {
                i += value
            }
        }
        println(i)
    }

    fun Array<Array<Int>>.update(func :Function<Int,Int>?, start: String, end:String) {
        val startC= start.split(",")
        val endC = end.split(",")
        val x1 = startC[0].toInt()
        val y1 = startC[1].toInt()
        val x2 = endC[0].toInt()
        val y2 = endC[1].toInt()
        for ( i in x1..x2) {
            for( j in y1..y2) {
                this[i][j] = func!!.apply(this[i][j])
            }
        }
    }
}