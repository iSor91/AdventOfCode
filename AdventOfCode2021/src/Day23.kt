package com.isor.aoc2021

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year

fun main() {
    Day23().executeGoals()
}

@TestResources
@Year(2021)
class Day23: AOC_Runner() {

    data class Amphipod(val name: String, val cost: Int, val destination: Int, val currentSpace: Space) {
        override fun toString(): String {
            return name
        }

        fun reachableDestination(): Boolean {
            //todo check if destination *20 +1/2 are occupied, and if +2 is occupied, is it by a same type amphipod

            //todo determine the correct destination e.g. 21 or 22 if the 22 is not occupied, that is the destination
            return this.currentSpace.isReachable(destination)
        }
    }

    data class Space(val index: Int, val connection: MutableList<Space>, var occupant: Amphipod? = null, var isStayable: Boolean = true) {
        fun correctOccupant(): Boolean {
            return occupant==null || occupant!!.destination == index / 10
        }

        override fun toString(): String {
            return if(occupant == null) "_" else occupant.toString()
        }

        fun isReachable(destination: Int): Boolean {
            //todo create a path to destination, and check if any of the path's space is occupied ->
            val direction = if(index > 10) index / 10 - destination else index - destination
            connection.forEach {
                it.index == destination
            }
            return false
        }
    }

    val costMap = mapOf("A" to 1, "B" to 10, "C" to 100, "D" to 1000)
    val destinationMap = mapOf("A" to 2, "B" to 4, "C" to 6, "D" to 8)
    val spaceMap: MutableMap<Int, Space>

    init {
        spaceMap = mutableMapOf(0 to Space(0, mutableListOf()))
        (1..10).forEach {
            val next = Space(it, mutableListOf(spaceMap[it-1]!!))
            spaceMap[it-1]!!.connection.add(next)
            spaceMap[it] = next
        }
        arrayOf(2,4,6,8).forEach {
            val parent = spaceMap[it]!!
            parent.isStayable=false
            val upper = it * 10 + 1
            val lower = it * 10 + 2
            val room1 = Space(upper, mutableListOf(spaceMap[it-1]!!))
            val room2 = Space(lower, mutableListOf(room1))
            parent.connection.add(room1)
            room1.connection.add(room2)
            spaceMap[upper] = room1
            spaceMap[lower] = room2
        }

        allLines.forEachIndexed { i, l ->
            l.chunked(1).forEachIndexed { j,a ->
                val startSpace = spaceMap[(j + 1) * 20 + i + 1]!!
                startSpace.occupant=Amphipod(a,costMap[a]!!, destinationMap[a]!!, startSpace)
            }
        }

        printSpaces()
    }

    fun printSpaces() {
        (0..10).forEach {
            print(spaceMap[it])
        }
        println()
        print(" ")
        (1..4).forEach {
            print(" ${spaceMap[it*20+1]}")
        }
        println()
        print(" ")
        (1..4).forEach {
            print(" ${spaceMap[it*20+2]}")
        }

        println()
        println()
    }

    override fun executeGoal_1() {
        val worstAmphipod = spaceMap.values.filter { !it.correctOccupant() }.maxByOrNull { it.occupant?.cost ?: 0 }
        val occupant = worstAmphipod!!.occupant!!
        val destination = occupant.destination
        val i = destination * 20
        val lower = spaceMap[i + 2]!!.correctOccupant()
        println()
    }

    override fun executeGoal_2() {

    }
}