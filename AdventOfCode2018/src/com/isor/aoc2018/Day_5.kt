package com.isor.aoc2018

import com.isor.aoc.common.AOC_Runner

fun main() {
    Day_5().executeGoals()
}

class Day_5 : AOC_Runner(2018) {

    override fun executeGoal_1() {
        println(allLines)
        var s = allLines[0]
        val matchedIndexes = mutableListOf<Int>()
        do {
            matchedIndexes.clear()
            s.forEachIndexed{i,c ->
                if(i < s.length-1) {
                    val c1 = s[i + 1]
                    if((c1 == c.toUpperCase() && c.isLowerCase()) || (c1 == c.toLowerCase() && c.isUpperCase()))
                        if(!matchedIndexes.contains(i-1))
                            matchedIndexes.add(i)
                }
            }
            matchedIndexes.reversed().forEach{i ->
//                println(s)
                s = s.removeRange(i,i+2)}
        } while (matchedIndexes.size != 0)
        println(s.length)
    }

    override fun executeGoal_2() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
