package com.isor.aoc2020

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year

fun main() {
    Day19().executeGoals()
}

//@TestResources
@Year(2020)
class Day19: AOC_Runner() {

    var strings: List<String>
    var rules: MutableMap<Int, String>
    val complexRuleMap = mutableMapOf<Int, ComplexRule>()
    val atomicRuleMap = mutableMapOf<Char, AtomicRule>()

    init {

        val emptyLineIndex = allLines.indexOf("")
        val rulesList = allLines.subList(0, emptyLineIndex)
        strings = allLines.subList(emptyLineIndex + 1, allLines.size)

        rules = rulesList.map { it.split(": ")[0].toInt() to it.split(": ")[1] }.toMap().toMutableMap()

        rules.forEach {
            process(it.key)
        }

    }

    private fun process(i: Int) {
        val orParts = rules[i]!!.split(" | ")
        val rule = complexRuleMap.getOrPut(i){ ComplexRule(i) }
        orParts.forEach {
            val newOrPart = OrPart()
            rule.orparts.add(newOrPart)
            val andParts = it.split(" ")
            andParts.forEach {andPart ->
                if(andPart.startsWith("\"")) {
                    val charset = andPart.substring(1 until it.length - 1).toCharArray()
                    charset.forEach { c -> newOrPart.subrules.add(atomicRuleMap.getOrPut(c){ AtomicRule(c) }) }
                } else {
                    newOrPart.subrules.add(complexRuleMap.getOrPut(andPart.toInt()){ ComplexRule(andPart.toInt()) })
                }
            }
        }
    }

    open class Rule(val num: Int) {
        open fun toString(previous: List<Int>, max: Int): String {
            return this.toString()
        }
    }

    data class AtomicRule(val char: Char) : Rule(char.hashCode()) {
        override fun toString(): String {
            return char.toString()
        }
    }

    class ComplexRule(num: Int) : Rule(num) {
        val orparts : MutableList<OrPart> = mutableListOf()

        override fun toString(previous: List<Int>, max: Int) : String {
            if(orparts.any{orPart -> orPart.subrules.any { sr ->  previous.count { it == sr.num } == max } }) {
                return ""
            }
            val extendableList = previous.toMutableList()
            extendableList.add(this.num)
            val joinToString = orparts.map { it.toString(extendableList, max) }.joinToString("|")
            if(orparts.size > 1) {
                return "($joinToString)"
            } else {
                return joinToString
            }
        }

        override fun toString(): String {
            val joinToString = orparts.joinToString("|")
            if(orparts.size > 1) {
                return "($joinToString)"
            } else {
                return joinToString
            }
        }
    }

    class OrPart() {
        val subrules = mutableListOf<Rule>()

        fun toString(previous: List<Int>, max: Int) : String {
            return subrules.map { it.toString(previous, max) }.joinToString("")
        }

        override fun toString(): String {
            return subrules.joinToString ( "" )
        }
    }

    override fun executeGoal_1() {
        val pattern = complexRuleMap[0].toString()
        val r = Regex(pattern)
        println(pattern)
        println(strings.count{r.matches(it)})
    }


    override fun executeGoal_2() {
        rules[8] = "42 | 42 8"
        rules[11] = "42 31 | 42 11 31"
        complexRuleMap.clear()
        rules.forEach {
            process(it.key)
        }
        val pattern = complexRuleMap[0]!!.toString(listOf<Int>(), 10)
        val r = Regex(pattern)
        println(pattern)
        println(strings.count{r.matches(it)})
    }
}
