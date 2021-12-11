package com.isor.aoc2016

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year
import kotlin.math.abs

/**
--- Day 1: No Time for a Taxicab ---
Santa's sleigh uses a very high-precision clock to guide its movements, and the clock's oscillator is regulated by stars. Unfortunately, the stars have been stolen... by the Easter Bunny. To save Christmas, Santa needs you to retrieve all fifty stars by December 25th.

Collect stars by solving puzzles. Two puzzles will be made available on each day in the Advent calendar; the second puzzle is unlocked when you complete the first. Each puzzle grants one star. Good luck!

You're airdropped near Easter Bunny Headquarters in a city somewhere. "Near", unfortunately, is as close as you can get - the instructions on the Easter Bunny Recruiting Document the Elves intercepted start here, and nobody had time to work them out further.

The Document indicates that you should start at the given coordinates (where you just landed) and face North. Then, follow the provided sequence: either turn left (L) or right (R) 90 degrees, then walk forward the given number of blocks, ending at a new intersection.

There's no time to follow such ridiculous instructions on foot, though, so you take a moment and work out the destination. Given that you can only walk on the street grid of the city, how far is the shortest path to the destination?

For example:

Following R2, L3 leaves you 2 blocks East and 3 blocks North, or 5 blocks away.
R2, R2, R2 leaves you 2 blocks due South of your starting position, which is 2 blocks away.
R5, L5, R5, R3 leaves you 12 blocks away.
How many blocks away is Easter Bunny HQ?

Your puzzle answer was 298.

--- Part Two ---
Then, you notice the instructions continue on the back of the Recruiting Document. Easter Bunny HQ is actually at the first location you visit twice.

For example, if your instructions are R8, R4, R4, R8, the first location you visit twice is 4 blocks away, due East.

How many blocks away is the first location you visit twice?

Your puzzle answer was 158.
 */

fun main() {
    Day1().executeGoals()
}

@TestResources
@Year(2016)
class Day1: AOC_Runner() {

    data class Command(val rotationAngle: Int, val length: Int)

    private val directionMap = mapOf("R" to 90, "L" to 270)

    private val commands = allLines.map { it.split(Regex(", "))
        .map { s->Command(directionMap[s.substring(0..0)]!!, s.substring(1).toInt()) } }

    private val finalPositions1 = mutableListOf<Pair<Int, Int>>()
    private val finalPositions2 = mutableListOf<Pair<Int, Int>>()

    init {
        commands.forEach outer@{
            val visited = mutableListOf<Pair<Int, Int>>()
            var pos: Pair<Int,Int> = Pair(0,0)
            var dir = Pair(1,0)
            var firstTwice: Pair<Int, Int>? = null
            it.forEach {
                    c ->
                dir = dir.rotate(c.rotationAngle)
                repeat(c.length) {
                    pos += dir
                    if(visited.contains(pos) && firstTwice == null) {
                        firstTwice = pos
                    }
                    visited.add(pos)
                }
            }
            finalPositions1.add(pos)
            firstTwice?.let { it1 -> finalPositions2.add(it1) }
        }
    }

    override fun executeGoal_1() {
        println(finalPositions1.map { abs(it.first) + abs(it.second) })
    }

    override fun executeGoal_2() {
        println(finalPositions2.map { abs(it.first) + abs(it.second) })
    }
}