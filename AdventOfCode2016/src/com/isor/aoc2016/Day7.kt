package com.isor.aoc2016

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year

fun main() {
    Day7().executeGoals()
}

@TestResources
@Year(2016)
class Day7:AOC_Runner() {

    override fun executeGoal_1() {
        val abba = Regex("^.*([a-z])([a-z])(\\2).*$")
        val hypernet = Regex(".*\\[.*([a-z])([a-z])\\2\\1.*\\].*")
        val filter = allLines.filter {
            abba.matches(it)
        }.filter {
            !hypernet.matches(it)
        }
        filter.forEach{ println(it)}
    }

    override fun executeGoal_2() {
    }
}