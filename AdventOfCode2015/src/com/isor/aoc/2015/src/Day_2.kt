package com.isor.aoc.`2015`.src

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year

fun main() {
    Day_2().executeGoals()
}

@Year(2015)
//@TestResources
class Day_2 : AOC_Runner() {

    override fun executeGoal_1() {

        var i = 0
        allLines.forEach{
            val split = it.split("x")
            val sorted = split.map { it.toInt() }.sorted()
            i += sorted[0]*sorted[1]*3 + sorted[0]*sorted[2]*2 + sorted[1]*sorted[2]*2
        }
        println(i)

    }

    override fun executeGoal_2(){
        var i = 0
        allLines.forEach{
            val split = it.split("x")
            val sorted = split.map { it.toInt() }.sorted()
            i += sorted[0]*2 + sorted[1]*2 + sorted[0]*sorted[1]*sorted[2]
        }
        println(i)

    }


}