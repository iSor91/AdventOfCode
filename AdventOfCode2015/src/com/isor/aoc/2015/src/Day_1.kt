package com.isor.aoc.`2015`.src

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year


fun main() {
    Day_1().executeGoals()
}

//@TestResources
@Year(2015)
class Day_1 : AOC_Runner() {

    override fun executeGoal_1() {
        val instructions = allLines[0]
        val iterator = instructions.iterator()
        var level = 0
        while (iterator.hasNext()) {
            val next = iterator.next()
            if(next == '(') {
                level++
            } else if ( next == ')') {
                level--
            }
        }
        println(level)
    }

    override fun executeGoal_2() {
        val instructions = allLines[0]
        val iterator = instructions.iterator()
        var level = 0
        var i = 0
        while (iterator.hasNext() && level >= 0) {
            val next = iterator.next()
            if(next == '(') {
                level++
            } else if ( next == ')') {
                level--
            }
            i++
        }
        println(i)
    }

}