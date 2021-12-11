package com.isor.aoc2020

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year
import kotlin.math.absoluteValue
import kotlin.reflect.KFunction1

fun main() {
    Day12_2().executeGoals()
}

//@TestResources
@Year(2020)
class Day12_2 : AOC_Runner() {

    lateinit var movePos: Pair<Int, Int>
    lateinit var shipPos: Pair<Int, Int>

    private val dirMap: Map<Char, Pair<Int, Int>> = mapOf('E' to (0 to 1), 'W' to (0 to -1), 'N' to (1 to 0), 'S' to (-1 to 0))

    override fun executeGoal_1() {
        navigate(dirMap['E']!!, this::incrementShipPos)
    }

    override fun executeGoal_2() {
        navigate(1 to 10, this::incrementMovePos)
    }

    private fun navigate(movePosStart: Pair<Int, Int>, incrementer: KFunction1<Pair<Int, Int>, Unit>) {
        movePos = movePosStart
        shipPos = 0 to 0
        allLines.forEach {
            interpretCommand(it, movePos, incrementer)
        }
        println("$shipPos ${shipPos.first.absoluteValue + shipPos.second.absoluteValue}")
    }

    private fun interpretCommand(it: String, moveDir: Pair<Int, Int>, incrementer: KFunction1<Pair<Int, Int>, Unit>) {
        val dirId = it.subSequence(0, 1)[0]
        val value = it.substring(1).toInt()
        when (dirId) {
            'F' -> {
                shipPos += moveDir * value
            }
            in charArrayOf('L', 'R') -> {
                movePos = rotateDir(value, dirId, moveDir)
            }
            else -> {
                incrementer.call(dirMap[dirId]!! * value)
            }
        }
    }

    private fun rotateDir(value: Int, dirId: Char, currentDir: Pair<Int, Int>) : Pair<Int,Int> {
        val rotateBy = (0.boundedPlus (value * if (dirId == 'L') -1 else 1, 0, 359)) / 90
        var tmpDir = currentDir
        for(i in 0 until rotateBy) tmpDir = Pair(-tmpDir.second, tmpDir.first)
        return tmpDir
    }

    fun incrementShipPos(incrementWith: Pair<Int,Int>) {
        this.shipPos += incrementWith
    }

    fun incrementMovePos(incrementWith: Pair<Int,Int>) {
        this.movePos += incrementWith
    }
}
