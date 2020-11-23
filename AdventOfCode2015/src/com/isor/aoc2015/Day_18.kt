package com.isor.aoc2015

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year

/**
 * --- Day 18: Like a GIF For Your Yard ---
After the million lights incident, the fire code has gotten stricter: now, at most ten thousand lights are allowed. You arrange them in a 100x100 grid.

Never one to let you down, Santa again mails you instructions on the ideal lighting configuration. With so few lights, he says, you'll have to resort to animation.

Start by setting your lights to the included initial configuration (your puzzle input). A # means "on", and a . means "off".

Then, animate your grid in steps, where each step decides the next configuration based on the current one. Each light's next state (either on or off) depends on its current state and the current states of the eight lights adjacent to it (including diagonals). Lights on the edge of the grid might have fewer than eight neighbors; the missing ones always count as "off".

For example, in a simplified 6x6 grid, the light marked A has the neighbors numbered 1 through 8, and the light marked B, which is on an edge, only has the neighbors marked 1 through 5:

1B5...
234...
......
..123.
..8A4.
..765.
The state a light should have next is based on its current state (on or off) plus the number of neighbors that are on:

A light which is on stays on when 2 or 3 neighbors are on, and turns off otherwise.
A light which is off turns on if exactly 3 neighbors are on, and stays off otherwise.
All of the lights update simultaneously; they all consider the same current state before moving to the next.

Here's a few steps from an example configuration of another 6x6 grid:

Initial state:
.#.#.#
...##.
#....#
..#...
#.#..#
####..

After 1 step:
..##..
..##.#
...##.
......
#.....
#.##..

After 2 steps:
..###.
......
..###.
......
.#....
.#....

After 3 steps:
...#..
......
...#..
..##..
......
......

After 4 steps:
......
......
..##..
..##..
......
......
After 4 steps, this example has four lights on.

In your grid of 100x100 lights, given your initial configuration, how many lights are on after 100 steps?

Your puzzle answer was 814.

--- Part Two ---
You flip the instructions over; Santa goes on to point out that this is all just an implementation of Conway's Game of Life. At least, it was, until you notice that something's wrong with the grid of lights you bought: four lights, one in each corner, are stuck on and can't be turned off. The example above will actually run like this:

Initial state:
##.#.#
...##.
#....#
..#...
#.#..#
####.#

After 1 step:
#.##.#
####.#
...##.
......
#...#.
#.####

After 2 steps:
#..#.#
#....#
.#.##.
...##.
.#..##
##.###

After 3 steps:
#...##
####.#
..##.#
......
##....
####.#

After 4 steps:
#.####
#....#
...#..
.##...
#.....
#.#..#

After 5 steps:
##.###
.##..#
.##...
.##...
#.#...
##...#
After 5 steps, this example now has 17 lights on.

In your grid of 100x100 lights, given your initial configuration, but with the four corners always in the on state, how many lights are on after 100 steps?

Your puzzle answer was 924.
 */

fun main() {
    Day_18().executeGoals()
}

//@TestResources
@Year(2015)
class Day_18 : AOC_Runner() {
    var lights : Array<CharArray>
    val lightsNum : Int
    val times = 99

    enum class LightState(val value : Char) {
        ON('#'), OFF('.');
        companion object {
            fun get(char: Char) : LightState {
                return when(char) {
                    '.' -> OFF
                    '#' -> ON
                    else -> OFF
                }
            }
        }
    }

    init {
        lights = allLines.map { l -> l.toCharArray() }.toTypedArray()
        lightsNum = lights.size
        for(i in 0..times) {
            update()
        }
    }

    fun update() {
        val copy = Array(lightsNum) {CharArray(lightsNum){'-'} }
        fixLightsForPartTwo() //SWITCH GOAL HERE!
        for(i in 0 until lightsNum) {
            for(j in 0 until lightsNum) {
                copy[i][j] = calculate(i, j).value
            }
        }
        lights = copy
    }

    private fun fixLightsForPartTwo() {
        lights[0][0] = LightState.ON.value
        lights[0][lightsNum-1] = LightState.ON.value
        lights[lightsNum-1][0] = LightState.ON.value
        lights[lightsNum-1][lightsNum-1] = LightState.ON.value
    }

    private fun calculate(i: Int, j: Int): LightState {
        val orig = LightState.get(lights[i][j])
        val xStart = i+getStartIndex(i)
        val yStart = j+getStartIndex(j)
        val xEnd = i+getEndIndex(i)
        val yEnd = j+getEndIndex(j)

        var onAround = 0
        for(x in xStart..xEnd) {
            for(y in yStart..yEnd) {
                if(x==i && y == j) continue
                val current = lights[x][y]
                onAround += if(LightState.ON == LightState.get(current)) 1 else 0
            }
        }
        return when(onAround) {
            2 -> if(orig == LightState.ON) LightState.ON else LightState.OFF
            3 -> LightState.ON
            else -> LightState.OFF
        }
    }

    private fun getStartIndex(i: Int) : Int {
        return when (i) {
            0 -> 0
            else -> -1
        }
    }

    private fun getEndIndex(i: Int) : Int {
        return when (i) {
            lightsNum-1 -> 0
            else -> 1
        }
    }

    override fun executeGoal_1() {
        val size = lights.flatMap { it.asList() }.filter { LightState.get(it) == LightState.ON }.size
        println(size)
    }

    private fun printLights() {
        lights.forEach {
            it.forEach { c -> print(c) }
            println()
        }
        println()
    }

    override fun executeGoal_2() {
        //answer switchable in update method
        fixLightsForPartTwo()
        val size = lights.flatMap { it.asList() }.filter { LightState.get(it) == LightState.ON }.size
        println(size)
    }

}
