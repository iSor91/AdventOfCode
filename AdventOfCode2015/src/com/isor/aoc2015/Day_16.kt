package com.isor.aoc2015

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.Year
import java.util.function.BiFunction

/**
 * --- Day 16: Aunt Sue ---
Your Aunt Sue has given you a wonderful gift, and you'd like to send her a thank you card. However, there's a small problem: she signed it "From, Aunt Sue".

You have 500 Aunts named "Sue".

So, to avoid sending the card to the wrong person, you need to figure out which Aunt Sue (which you conveniently number 1 to 500, for sanity) gave you the gift. You open the present and, as luck would have it, good ol' Aunt Sue got you a My First Crime Scene Analysis Machine! Just what you wanted. Or needed, as the case may be.

The My First Crime Scene Analysis Machine (MFCSAM for short) can detect a few specific compounds in a given sample, as well as how many distinct kinds of those compounds there are. According to the instructions, these are what the MFCSAM can detect:

children, by human DNA age analysis.
cats. It doesn't differentiate individual breeds.
Several seemingly random breeds of dog: samoyeds, pomeranians, akitas, and vizslas.
goldfish. No other kinds of fish.
trees, all in one group.
cars, presumably by exhaust or gasoline or something.
perfumes, which is handy, since many of your Aunts Sue wear a few kinds.
In fact, many of your Aunts Sue have many of these. You put the wrapping from the gift into the MFCSAM. It beeps inquisitively at you a few times and then prints out a message on ticker tape:

children: 3
cats: 7
samoyeds: 2
pomeranians: 3
akitas: 0
vizslas: 0
goldfish: 5
trees: 3
cars: 2
perfumes: 1
You make a list of the things you can remember about each Aunt Sue. Things missing from your list aren't zero - you simply don't remember the value.

What is the number of the Sue that got you the gift?

Your puzzle answer was 213.

The first half of this puzzle is complete! It provides one gold star: *

--- Part Two ---
As you're about to send the thank you note, something in the MFCSAM's instructions catches your eye. Apparently, it has an outdated retroencabulator, and so the output from the machine isn't exact values - some of them indicate ranges.

In particular, the cats and trees readings indicates that there are greater than that many (due to the unpredictable nuclear decay of cat dander and tree pollen), while the pomeranians and goldfish readings indicate that there are fewer than that many (due to the modial interaction of magnetoreluctance).

What is the number of the real Aunt Sue?
 */

fun main() {
    Day_16().executeGoals()
}

//@TestResources
@Year(2015)
class Day_16 : AOC_Runner() {

    private val searchCriterias =
"""children: 3
cats: 7
samoyeds: 2
pomeranians: 3
akitas: 0
vizslas: 0
goldfish: 5
trees: 3
cars: 2
perfumes: 1"""

    private val searchCriteriaMap = mutableMapOf<String, Int>()

    private val sueMap = mutableMapOf<Int, MutableMap<String, Int>>()

    val logic = mutableMapOf<String, BiFunction<Int, Int, Boolean>>()

    init {
        val split = searchCriterias.split("\n")
        split.forEach{ s -> getCriteria(s, searchCriteriaMap) }
        val sueRegex = Regex("(?:Sue )(\\d+)(?::)(( [a-z]+: \\d+[,]?)+)")
        allLines.forEach{
            val matchResult = sueRegex.find(it)
            val index = matchResult!!.groups[1]!!.value!!.toInt()
            sueMap.putIfAbsent(index,mutableMapOf())
            val criterias = matchResult!!.groups[2]!!.value.split(",")
            for(i in criterias) {
                getCriteria(i.trim(), sueMap[index]!!)
            }
        }

        logic.putIfAbsent("cats", BiFunction { t, u -> t < u})
        logic.putIfAbsent("trees", BiFunction { t, u -> t < u})
        logic.putIfAbsent("pomeranians", BiFunction { t, u -> t > u})
        logic.putIfAbsent("goldfish", BiFunction { t, u -> t > u})

    }

    private fun getCriteria(s: String, map: MutableMap<String, Int>) {
        val searchCriteria = s.split(": ")
        val commaSeparated = searchCriteria[1].endsWith(",")
        map.putIfAbsent(searchCriteria[0], if(commaSeparated) searchCriteria[1].substring(0, searchCriteria[1].length-1).toInt() else searchCriteria[1].toInt())
    }

    override fun executeGoal_1() {
        val filteredMap = mutableMapOf<Int, MutableMap<String, Int>>()
        sueMap.forEach {
            val criterias = it.value
            for (criteria in criterias) {
                if(searchCriteriaMap[criteria.key] != criteria.value) {
                    return@forEach
                }
            }
            filteredMap.putIfAbsent(it.key, it.value)
        }
        println(filteredMap.entries)
    }


    override fun executeGoal_2() {
        val filteredMap = mutableMapOf<Int, MutableMap<String, Int>>()
        sueMap.forEach {
            val criterias = it.value
            for (criteria in criterias) {
                val function = logic.getOrDefault(criteria.key, BiFunction { t, u -> t == u })
                if(!function.apply(searchCriteriaMap[criteria.key]!!,criteria.value)) {
                    return@forEach
                }
            }
            filteredMap.putIfAbsent(it.key, it.value)
        }
        println(filteredMap.entries)
    }

}