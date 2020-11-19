package com.isor.aoc2015

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year

/**
 * --- Day 17: No Such Thing as Too Much ---
The elves bought too much eggnog again - 150 liters this time. To fit it all into your refrigerator, you'll need to move it into smaller containers. You take an inventory of the capacities of the available containers.

For example, suppose you have containers of size 20, 15, 10, 5, and 5 liters. If you need to store 25 liters, there are four ways to do it:

15 and 10
20 and 5 (the first 5)
20 and 5 (the second 5)
15, 5, and 5
Filling all containers entirely, how many different combinations of containers can exactly fit all 150 liters of eggnog?

Your puzzle answer was 4372.

The first half of this puzzle is complete! It provides one gold star: *

--- Part Two ---
While playing with all the containers in the kitchen, another load of eggnog arrives! The shipping and receiving department is requesting as many containers as you can spare.

Find the minimum number of containers that can exactly fit all 150 liters of eggnog. How many different ways can you fill that number of containers and still hold exactly 150 litres?

In the example above, the minimum number of containers was two. There were three ways to use that many containers, and so the answer there would be 3.
 */

fun main() {
    Day_17().executeGoals()
}

//@TestResources
@Year(2015)
class Day_17 : AOC_Runner() {
    val values : List<Int> = allLines.map(String::toInt)
    var filteredCombinations: List<List<Int>>

    init {
        val combinations = mutableListOf<List<Int>>()
        for ( numberCount in values.indices) {
            combinations.addAll(generateCombinations(numberCount + 1, 0, mutableListOf()))
        }
        filteredCombinations = combinations.filter { it.sum() == 150 }
    }

    private fun generateCombinations(numberCount: Int, startIndex: Int, combination: List<Int>): List<List<Int>> {
        val combinations = mutableListOf<List<Int>>()
        if(combination.size < numberCount) {
            for(nextStartIndex in startIndex until values.size) {
                val biggerList = combination.copy
                biggerList.add(values[nextStartIndex])
                combinations.addAll(generateCombinations(numberCount, nextStartIndex + 1, biggerList ))
            }
        } else {
            combinations.add(combination)
        }
        return combinations
    }

    override fun executeGoal_1() {
        filteredCombinations.forEach{ println(it)}
        println(filteredCombinations.size)
    }

    override fun executeGoal_2() {
        val size = filteredCombinations.minBy { it.size }!!.size
        val minimalElementCombinations = filteredCombinations.filter { it.size == size }
        minimalElementCombinations.forEach{ println(it)}
        println(minimalElementCombinations.size)
    }

}