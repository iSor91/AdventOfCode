package com.isor.aoc2021

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year

fun main() {
    Day23().executeGoals()
}

//@TestResources
@Year(2021)
class Day23: AOC_Runner() {

    data class Amphipod(val id: Int, val type: String, val cost: Int, val destination: Int, val currentSpace: Pair<Int,Int>) {
        override fun toString(): String {
            return "$type$id"
        }

        override fun equals(other: Any?): Boolean {
            return other is Amphipod && id == other.id
        }

        override fun hashCode(): Int {
            return id.hashCode()
        }
    }

    val costMap = mapOf("A" to 1, "B" to 10, "C" to 100, "D" to 1000)
    val destinationMap = mapOf("A" to 2, "B" to 4, "C" to 6, "D" to 8)

    val validCorridorPos = intArrayOf(0, 1, 3, 5, 7, 9, 10)
    val spaceMap = mutableMapOf<Pair<Int,Int>, Amphipod?>(
        //corridor
        0 to 0 to null,
        0 to 1 to null,
        0 to 2 to null,
        0 to 3 to null,
        0 to 4 to null,
        0 to 5 to null,
        0 to 6 to null,
        0 to 7 to null,
        0 to 8 to null,
        0 to 9 to null,
        0 to 10 to null,
        //rooms
        1 to 2 to null,
        2 to 2 to null,
        1 to 4 to null,
        2 to 4 to null,
        1 to 6 to null,
        2 to 6 to null,
        1 to 8 to null,
        2 to 8 to null
    )

    fun Map.Entry<Pair<Int,Int>, Amphipod?>.validMoves(map: Map<Pair<Int, Int>, Amphipod?>): List<Pair<Pair<Int,Int>,Int>> {
        val validPositions = mutableListOf<Pair<Pair<Int, Int>,Int>>()
        if(this.value == null
            || (this.key.first==2 && this.value!!.destination == this.key.second)
            || (this.key.first==1 && this.value!!.destination == this.key.second
                    && map[Pair(2,this.key.second)] != null && map[Pair(2,this.key.second)]!!.type == this.value!!.type)) {
            return validPositions
        }
        val amphipod = this.value!!
        val pos = this.key
        if(pos.first == 0) {
            //only the target room
            val destination = amphipod.destination
            val upper = Pair(1, destination)
            val lower = Pair(2, destination)
            var reachCost = pos.canReach(map, upper)
            if(reachCost != -1) {
                reachCost *= amphipod.cost
                if(map[lower] == null) {
                    validPositions.add(lower to reachCost + amphipod.cost)
                } else if(map[lower]!!.type == amphipod.type) {
                    validPositions.add(upper to reachCost)
                }
            }
        } else {
            //All available spaces
            val allValidPositions = map.keys
                .filter { map[it] == null }
                .filter { it.first == 0 || it.second == amphipod.destination }
                .filter { it.first != 0 || (it.first == 0 && it.second in validCorridorPos) }
                .map { it to pos.canReach(map, it) * amphipod.cost }
                .filter { it.second >= 0 }
            val targetPositions = allValidPositions.filter { it.first.second == amphipod.destination }
            val lowerTargetPosition = targetPositions.filter { it.first.first == 2 }
            validPositions.addAll(
                lowerTargetPosition.ifEmpty { targetPositions.ifEmpty {
                    allValidPositions
                } }
            )
        }
        return validPositions
    }

    fun Pair<Int,Int>.canReach(map: Map<Pair<Int, Int>, Amphipod?>, to: Pair<Int,Int>): Int {
        if(this == to) {
            return 0
        }
        val nextPos: Pair<Int, Int> = if(this.first > 0 && this.second != to.second) {
            Pair(this.first - 1, this.second)
        } else if(this.second == to.second) {
            Pair(this.first + 1, this.second)
        } else {
            if(this.second > to.second) Pair(this.first, this.second - 1) else Pair(this.first, this.second + 1)
        }
        if(map[nextPos] != null) {
            return -1
        }
        val canReach = nextPos.canReach(map, to)
        return if(canReach==-1) -1 else 1 + canReach
    }

    fun Map<Pair<Int, Int>, Amphipod?>.notInPosition(): Int {
        return this.count { it.value!=null && it.value!!.destination != it.key.second }
    }

    init {

        allLines.forEachIndexed { i, l ->
            l.chunked(1).forEachIndexed { j,a ->
                val startSpace = i+1 to (j+1)*2
                spaceMap[startSpace]=Amphipod(i * 4 + j, a,costMap[a]!!, destinationMap[a]!!, startSpace)
            }
        }
    }

    fun MutableMap<Pair<Int, Int>, Amphipod?>.printSpaces() {
        (-1..3).forEach { y ->
            (-1..11).forEach { x ->
                val pair = Pair(y, x)
                if(!this.keys.contains(pair)) {
                    print("##")
                } else {
                    print(this[pair] ?: "__")
                }
            }
            println()
        }
    }

    data class Situation(var map: MutableMap<Pair<Int, Int>, Amphipod?>, var cost: Int, val moves: MutableList<String> = mutableListOf()) {
        constructor(other: Situation) : this(other.map.toMap().toMutableMap(), other.cost, other.moves.toMutableList())

        override fun equals(other: Any?): Boolean {
            return other is Situation && other.cost == cost
        }

        override fun hashCode(): Int {
            return cost.hashCode()
        }
    }

    var mincost = Int.MAX_VALUE
    override fun executeGoal_1() {
        val startSituation = Situation(spaceMap, 0)

        val validSituations = startSituation.oneStep()
        println(validSituations.maxByOrNull { it.cost })
        println(validSituations.minByOrNull { it.cost })
    }

    private fun Situation.oneStep(): MutableSet<Situation> {
        val validSituations = mutableSetOf<Situation>()
        if(this.cost > mincost) {
            return validSituations
        }
        this.map.entries.sortedBy { it.value?.cost }.forEach { startSit ->
            this.moves.ifEmpty { println(startSit) }
            startSit.validMoves(this.map).forEach { targetPos ->
                val move = "${startSit.key}[${startSit.value}] -> $targetPos"
                val newSituation = Situation(this)
                newSituation.moves.add(move)
                newSituation.map[targetPos.first] = startSit.value
                newSituation.map[startSit.key] = null
                newSituation.cost += targetPos.second
                val notInPosition = newSituation.map.notInPosition()
                if (notInPosition > 0) {
                    validSituations.addAll(newSituation.oneStep())
                } else if(newSituation.cost < mincost) {
                    validSituations.add(newSituation)
                    println(newSituation)
                    mincost = newSituation.cost
                }
            }
        }
        return validSituations
    }

    override fun executeGoal_2() {

    }
}