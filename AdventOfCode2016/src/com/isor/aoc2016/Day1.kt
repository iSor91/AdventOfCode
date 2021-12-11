package com.isor.aoc2016

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.Year
import kotlin.math.abs

fun main() {
    Day1().executeGoals()
}

//@TestResources
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