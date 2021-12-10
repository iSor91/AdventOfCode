package com.isor.aoc2021

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year

fun main() {
    Day10().executeGoals()
}

//@TestResources
@Year(2021)
class Day10 : AOC_Runner() {

    val m = allLines.map { it.chunked(1) }

    val validMap = mapOf<String,String>("(" to ")", "[" to "]", "{" to "}", "<" to ">")
    val bracketVal1 = mapOf<String, Int>(")" to 3, "]" to 57, "}" to 1197, ">" to 25137)
    val bracketVal2 = mapOf<String, Int>(")" to 1, "]" to 2, "}" to 3, ">" to 4)


    override fun executeGoal_1() {

        var sum = 0
        m.forEach outer@{
            val expectedChars = mutableListOf<String>()
            var corruptChar: String? = null
            it.forEach inner@{
                b->
                if(corruptChar != null) {
                    return@inner
                }
                if(validMap.keys.contains(b))
                    expectedChars.add(0,validMap[b]!!)
                else if(expectedChars[0] != b) {
                    corruptChar = b
                } else
                    expectedChars.removeAt(0)
            }
            if(corruptChar != null) {
                sum += bracketVal1[corruptChar]!!
            }
        }
        println(sum)
    }

    override fun executeGoal_2() {
        val sums = mutableListOf<Long>()
        m.forEach outer@{
            val expectedChars = mutableListOf<String>()
            var corruptChar: String? = null
            it.forEach inner@{
                b->
                if(validMap.keys.contains(b))
                    expectedChars.add(0,validMap[b]!!)
                else if(expectedChars[0] != b) {
                    return@outer
                } else
                    expectedChars.removeAt(0)
            }
            var sum = 0L
            expectedChars.forEach { e->
                sum *= 5
                sum += bracketVal2[e]!!
            }
            sums.add(sum)
        }
        val middle = sums.size / 2
        sums.sort()
        println(sums[middle])
    }
}