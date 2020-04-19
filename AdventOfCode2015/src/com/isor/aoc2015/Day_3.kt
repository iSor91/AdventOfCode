package com.isor.aoc2015

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year
import java.lang.IllegalStateException

/**
 * --- Day 3: Perfectly Spherical Houses in a Vacuum ---
Santa is delivering presents to an infinite two-dimensional grid of houses.

He begins by delivering a present to the house at his starting location, and then an elf at the North Pole calls him via radio and tells him where to move next. Moves are always exactly one house to the north (^), south (v), east (>), or west (<). After each move, he delivers another present to the house at his new location.

However, the elf back at the north pole has had a little too much eggnog, and so his directions are a little off, and Santa ends up visiting some houses more than once. How many houses receive at least one present?

For example:

> delivers presents to 2 houses: one at the starting location, and one to the east.
^>v< delivers presents to 4 houses in a square, including twice to the house at his starting/ending location.
^v^v^v^v^v delivers a bunch of presents to some very lucky children at only 2 houses.
Your puzzle answer was 2592.

--- Part Two ---
The next year, to speed up the process, Santa creates a robot version of himself, Robo-Santa, to deliver presents with him.

Santa and Robo-Santa start at the same location (delivering two presents to the same starting house), then take turns moving based on instructions from the elf, who is eggnoggedly reading from the same script as the previous year.

This year, how many houses receive at least one present?

For example:

^v delivers presents to 3 houses, because Santa goes north, and then Robo-Santa goes south.
^>v< now delivers presents to 3 houses, and Santa and Robo-Santa end up back where they started.
^v^v^v^v^v now delivers presents to 11 houses, with Santa going one direction and Robo-Santa going the other.
Your puzzle answer was 2360.
 */

fun main() {
    Day_3().executeGoals()
}

@Year(2015)
//@TestResources
class Day_3 : AOC_Runner() {


    override fun executeGoal_1() {
        val visitedHouses = hashMapOf<Pair<Int, Int>, Int>()

        var position : Pair<Int,Int> = Pair(0,0)

        val directions = allLines[0]
        val direction = directions.iterator()
        while (direction.hasNext()) {
            val dir = direction.nextChar()
            val pairDir = getDir(dir)
            visitedHouses.deliver(position)
            position = position.add(pairDir)
        }
        visitedHouses.deliver(position)
        println(visitedHouses.entries.size)
    }

    override fun executeGoal_2() {
        val visitedHouses = hashMapOf<Pair<Int, Int>, Int>()

        var santa = Pair(0,0)
        var robosanta = Pair(0,0)

        val directions = allLines[0]
        val direction = directions.iterator()
        var i = 0;
        while (direction.hasNext()) {
            var directed = when(i%2) {
                0 -> santa
                1 -> robosanta
                else -> throw  IllegalStateException("This cannot happen.")
            }

            val dir = direction.nextChar()
            val pairDir = getDir(dir)
            visitedHouses.deliver(directed)
            when(i++%2) {
                0 -> santa = directed.add(pairDir)
                1 -> robosanta = directed.add(pairDir)
                else -> throw  IllegalStateException("This cannot happen.")
            }
        }
        visitedHouses.deliver(santa)
        visitedHouses.deliver(robosanta)
        println(visitedHouses.entries.size)
    }

    private fun getDir(dir: Char) : Pair<Int,Int>{
        return when (dir) {
            '^' -> Pair(1, 0)
            'v' -> Pair(-1, 0)
            '>' -> Pair(0, 1)
            '<' -> Pair(0, -1)
            else -> throw IllegalStateException("Not valid direction char.")
        }
    }

    private infix fun HashMap<Pair<Int,Int>,Int>.deliver(coordinate : Pair<Int,Int>) {
        this.putIfAbsent(coordinate, 0)
        val i = this[coordinate]
        if (i != null) {
            this.put(coordinate, i.plus(1))
        }
    }

    private infix fun Pair<Int,Int>.add(a : Pair<Int,Int>) : Pair<Int, Int> {
        return Pair(a.first + first, a.second + second)
    }
}