package com.isor.aoc2021

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year

fun main() {
    Day17().executeGoals()
}

@Year(2021)
//@TestResources
class Day17: AOC_Runner() {

    val pattern = Regex("target area: x=(-?\\d+)\\.\\.(-?\\d+), y=(-?\\d+)\\.\\.(-?\\d+)")

    override fun executeGoal_1() {
        val target = allLines.map { pattern.find(it)!!.groupValues }[0]
        val x = target[1].toInt()..target[2].toInt()
        val y = target[3].toInt()..target[4].toInt()

        val validXTrajectory = mutableListOf<Pair<Int,Set<Int>>>()

        var initialX = 0
        while(initialX <= x.last) {
            val calculateXTrajectory = calculateXTrajectory(initialX)
            if(calculateXTrajectory.any { x.contains(it) }) {
                validXTrajectory.add(initialX to calculateXTrajectory)
            }
            initialX++
        }

        val validYTrajectory = mutableListOf<Pair<Int,List<Int>>>()
        var initialY = y.first-1
        while(initialY <= 100) {
            val calculateYTrajectory = calculateYTrajectory(initialY, y.first())
            if(calculateYTrajectory.any { y.contains(it) }) {
                validYTrajectory.add(initialY to calculateYTrajectory)
            }
            initialY++
        }

        val validXRanges = validXTrajectory.map {
            var indexOfLast = it.second.indexOfLast { x.contains(it) }
            if(indexOfLast == it.second.size-1)
                indexOfLast = Int.MAX_VALUE
            it.first to (
                it.second.indexOfFirst { t -> x.contains(t) } to indexOfLast
            )
        }

        val validYRanges = validYTrajectory.map {
            it.first to (
                it.second.indexOfFirst { t -> y.contains(t) } to it.second.indexOfLast { y.contains(it) }
            )
        }

        val validPairs = validXRanges.map { metszet(it, validYRanges) }

        val maxValidYTrajectory =
            validPairs.map { it.second }.flatten().distinct().maxByOrNull { validYTrajectory.first { y -> y.first == it }.second.maxOrNull()!! }

        println(validYTrajectory.first{ it.first == maxValidYTrajectory }.second.maxByOrNull { it })

        println(validPairs.sumBy { it.second.size })

    }

    private fun metszet(check: Pair<Int, Pair<Int, Int>>, validYRanges: List<Pair<Int, Pair<Int, Int>>>): Pair<Int, List<Int>> {
        return check.first to validYRanges.filter { check.second.first <= it.second.second && it.second.first <= check.second.second }.map { it.first }
    }

    fun calculateXTrajectory(initial: Int): Set<Int> {
        var i = initial
        val trajectory = mutableSetOf(0)
        while (i != 0) {
            trajectory.add(trajectory.last() + i)
            if(i > 0) i-- else i++
        }
        return trajectory
    }

    fun calculateYTrajectory(initial:Int, lowest: Int): List<Int> {
        var i = initial
        val trajectory = mutableListOf(0)
        while(trajectory.last() + i >= lowest) {
            trajectory.add(trajectory.last() + i)
            i--
        }
        return trajectory
    }

    override fun executeGoal_2() {
    }


}