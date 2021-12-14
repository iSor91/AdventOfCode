package com.isor.aoc2021

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year


fun main() {
    Day14().executeGoals()
}

//@TestResources
@Year(2021)
class Day14: AOC_Runner() {
    val template = allLines[0]
    val insertionMap: Map<String, String> = allLines.subList(2, allLines.size).map {
        val split = it.split(Regex(" -> "))
        split[0] to split[1]
    }.toMap()

    override fun executeGoal_1() {
        var s = template
        repeat(10) {
            s = insert(s)
        }
        val charfrequence = s.chunked(1).groupingBy { it }.eachCount()
        println(charfrequence.maxByOrNull { it.value }!!.value - charfrequence.minByOrNull { it.value }!!.value)
    }

    private fun insert(s: String): String {
        var s1 = s
        s1 = "${
            s1.windowed(2).joinToString("") {
                "${it[0]}${insertionMap[it]!!}"
            }
        }${s1.last()}"
        return s1
    }

    override fun executeGoal_2() {
        var s = template

        var polymers = s.windowed(2).groupingBy { it }.eachCount().asSequence().associate { it.key to it.value.toLong() }.toMutableMap()
        
        repeat(40){
            polymers = insert2(polymers)
        }

        val charCountMap = mutableMapOf<String, Long>()
        polymers.forEach {
            charCountMap[it.key[0].toString()] = charCountMap.getOrPut(it.key[0].toString()) {0L} + it.value
            charCountMap[it.key[1].toString()] = charCountMap.getOrPut(it.key[1].toString()) {0L} + it.value
        }
        charCountMap[s[0].toString()] = charCountMap.getOrPut(s[0].toString()){0L} + 1
        charCountMap[s.last().toString()] = charCountMap.getOrPut(s.last().toString()){0L} + 1

        val toMap = charCountMap.map { it.key to it.value / 2L }.toMap()

        println(toMap.maxByOrNull { it.value }!!.value - toMap.minByOrNull { it.value }!!.value)


    }

    private fun insert2(polymers: MutableMap<String, Long>): MutableMap<String, Long> {
        val newMap = mutableMapOf<String, Long>()
        polymers.forEach {
            val newString = insertionMap[it.key]!!
            newMap["${it.key[0]}$newString"] = newMap.getOrPut("${it.key[0]}$newString") { 0L } + 1 * it.value
            newMap["$newString${it.key[1]}"] = newMap.getOrPut("$newString${it.key[1]}") { 0L } + 1 * it.value
        }
        return newMap
    }
}