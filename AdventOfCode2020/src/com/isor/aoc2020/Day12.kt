package com.isor.aoc2020

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * --- Day 12: Rain Risk ---
Your ferry made decent progress toward the island, but the storm came in faster than anyone expected. The ferry needs to take evasive actions!

Unfortunately, the ship's navigation computer seems to be malfunctioning; rather than giving a route directly to safety, it produced extremely circuitous instructions. When the captain uses the PA system to ask if anyone can help, you quickly volunteer.

The navigation instructions (your puzzle input) consists of a sequence of single-character actions paired with integer input values. After staring at them for a few minutes, you work out what they probably mean:

Action N means to move north by the given value.
Action S means to move south by the given value.
Action E means to move east by the given value.
Action W means to move west by the given value.
Action L means to turn left the given number of degrees.
Action R means to turn right the given number of degrees.
Action F means to move forward by the given value in the direction the ship is currently facing.
The ship starts by facing east. Only the L and R actions change the direction the ship is facing. (That is, if the ship is facing east and the next instruction is N10, the ship would move north 10 units, but would still move east if the following action were F.)

For example:

F10
N3
F7
R90
F11
These instructions would be handled as follows:

F10 would move the ship 10 units east (because the ship starts by facing east) to east 10, north 0.
N3 would move the ship 3 units north to east 10, north 3.
F7 would move the ship another 7 units east (because the ship is still facing east) to east 17, north 3.
R90 would cause the ship to turn right by 90 degrees and face south; it remains at east 17, north 3.
F11 would move the ship 11 units south to east 17, south 8.
At the end of these instructions, the ship's Manhattan distance (sum of the absolute values of its east/west position and its north/south position) from its starting position is 17 + 8 = 25.

Figure out where the navigation instructions lead. What is the Manhattan distance between that location and the ship's starting position?

Your puzzle answer was 636.

--- Part Two ---
Before you can give the destination to the captain, you realize that the actual action meanings were printed on the back of the instructions the whole time.

Almost all of the actions indicate how to move a waypoint which is relative to the ship's position:

Action N means to move the waypoint north by the given value.
Action S means to move the waypoint south by the given value.
Action E means to move the waypoint east by the given value.
Action W means to move the waypoint west by the given value.
Action L means to rotate the waypoint around the ship left (counter-clockwise) the given number of degrees.
Action R means to rotate the waypoint around the ship right (clockwise) the given number of degrees.
Action F means to move forward to the waypoint a number of times equal to the given value.
The waypoint starts 10 units east and 1 unit north relative to the ship. The waypoint is relative to the ship; that is, if the ship moves, the waypoint moves with it.

For example, using the same instructions as above:

F10 moves the ship to the waypoint 10 times (a total of 100 units east and 10 units north), leaving the ship at east 100, north 10. The waypoint stays 10 units east and 1 unit north of the ship.
N3 moves the waypoint 3 units north to 10 units east and 4 units north of the ship. The ship remains at east 100, north 10.
F7 moves the ship to the waypoint 7 times (a total of 70 units east and 28 units north), leaving the ship at east 170, north 38. The waypoint stays 10 units east and 4 units north of the ship.
R90 rotates the waypoint around the ship clockwise 90 degrees, moving it to 4 units east and 10 units south of the ship. The ship remains at east 170, north 38.
F11 moves the ship to the waypoint 11 times (a total of 44 units east and 110 units south), leaving the ship at east 214, south 72. The waypoint stays 4 units east and 10 units south of the ship.
After these operations, the ship's Manhattan distance from its starting position is 214 + 72 = 286.

Figure out where the navigation instructions actually lead. What is the Manhattan distance between that location and the ship's starting position?

Your puzzle answer was 26841.
 */

fun main() {
    Day12().executeGoals()
}

//@TestResources
@Year(2020)
class Day12 :AOC_Runner() {


    enum class Direction {
        EAST('E', 90, (0 to 1)),WEST('W',270, 0 to -1),NORTH('N', 0, 1 to 0),SOUTH('S',180, -1 to 0);

        val id : Char
        val value : Int
        val change: Pair<Int,Int>

        constructor(id: Char, value : Int, change: Pair<Int,Int>) {
            this.id = id
            this.value = value
            this.change = change
        }

        companion object{
            fun getByID(id: Char): Direction? {
                return Direction.values().find { it.id == id }
            }
            fun getByValue(value: Int): Direction? {
                return Direction.values().find { it.value == value }
            }
        }
    }

    fun Char.translate(value: Int, current: Direction): Direction? {
        val x = if(this == 'L') -value else value
        val newDir = current.value.boundedPlus(x,0, 359)
        return Direction.getByValue(newDir)
    }

    override fun executeGoal_1() {
        var current : Direction = Direction.EAST
        var position : Pair<Int,Int> = Pair(0,0)
//        val list : MutableList<Pair<Direction,Pair<String, Pair<Int,Int>>>> = mutableListOf()
        allLines.forEach {
            val dirId = it.substring(0, 1)[0]
            var value = it.substring(1).toInt()
            var dir = Direction.getByID(dirId)
//            list.add(Pair(current,Pair(it, position.copy())))
            if(dirId == 'F') {
                dir = current
            } else if (dir == null) {
                dir = dirId.translate(value, current)!!
                current = dir
                value = 0
            }
            position += (dir!!.change * value)
        }
        println(position.first.absoluteValue + position.second.absoluteValue)
    }

    override fun executeGoal_2() {
        var wayPoint = 1 to 10
        var ship: Pair<Int, Int> = 0 to 0
        var current : Direction = Direction.EAST

        allLines.forEach {
            val dirId = it.substring(0, 1)[0]
            var value = it.substring(1).toInt()
            var dir = Direction.getByID(dirId)
            val shipDistance = wayPoint - ship
            when {
                dir != null -> {
                    wayPoint += dir!!.change * value
                }
                dirId == 'F' -> {
                    ship += (shipDistance * value)
                    wayPoint = ship + shipDistance
                }
                else -> {
                    val original = current.value
                    current = dirId.translate(value, current)!!
                    wayPoint = ship + shipDistance.rotate(current.value.boundedPlus(-original,0,359))
//                    println("$shipDistance -> ${wayPoint-ship}")
                }
            }
        }
        println(ship.first.absoluteValue + ship.second.absoluteValue)
    }

}

private operator fun Pair<Int, Int>.times(value: Int) : Pair<Int, Int> {
    return this.first * value to this.second * value
}

private operator fun Pair<Int, Int>.plus(value: Pair<Int, Int>) : Pair<Int, Int> {
    return this.first + value.first to this.second + value.second
}

private operator fun Pair<Int, Int>.minus(value: Pair<Int, Int>) : Pair<Int, Int> {
    return this.first - value.first to this.second - value.second
}

private fun Pair<Int, Int>.rotate(value: Int) : Pair<Int,Int> {
    val radVal = toRad(value)
    val cosx = cos(radVal).roundToInt()
    val sinx = sin(radVal).roundToInt()
    return Pair((this.first * cosx - this.second * sinx), (this.first * sinx + this.second * cosx))
}

private fun toRad(value: Int) = value.toDouble() * Math.PI / 180

