package com.isor.aoc2015

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year
import kotlin.math.pow

/**
 * --- Day 15: Science for Hungry People ---
Today, you set out on the task of perfecting your milk-dunking cookie recipe. All you have to do is find the right balance of ingredients.

Your recipe leaves room for exactly 100 teaspoons of ingredients. You make a list of the remaining ingredients you could use to finish the recipe (your puzzle input) and their properties per teaspoon:

capacity (how well it helps the cookie absorb milk)
durability (how well it keeps the cookie intact when full of milk)
flavor (how tasty it makes the cookie)
texture (how it improves the feel of the cookie)
calories (how many calories it adds to the cookie)
You can only measure ingredients in whole-teaspoon amounts accurately, and you have to be accurate so you can reproduce your results in the future. The total score of a cookie can be found by adding up each of the properties (negative totals become 0) and then multiplying together everything except calories.

For instance, suppose you have these two ingredients:

Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8
Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3
Then, choosing to use 44 teaspoons of butterscotch and 56 teaspoons of cinnamon (because the amounts of each ingredient must add up to 100) would result in a cookie with the following properties:

A capacity of 44*-1 + 56*2 = 68
A durability of 44*-2 + 56*3 = 80
A flavor of 44*6 + 56*-2 = 152
A texture of 44*3 + 56*-1 = 76
Multiplying these together (68 * 80 * 152 * 76, ignoring calories for now) results in a total score of 62842880, which happens to be the best score possible given these ingredients. If any properties had produced a negative total, it would have instead become zero, causing the whole score to multiply to zero.

Given the ingredients in your kitchen and their properties, what is the total score of the highest-scoring cookie you can make?

Your puzzle answer was 18965440.

The first half of this puzzle is complete! It provides one gold star: *

--- Part Two ---
Your cookie recipe becomes wildly popular! Someone asks if you can make another recipe that has exactly 500 calories per cookie (so they can use it as a meal replacement). Keep the rest of your award-winning process the same (100 teaspoons, same ingredients, same scoring system).

For example, given the ingredients above, if you had instead selected 40 teaspoons of butterscotch and 60 teaspoons of cinnamon (which still adds to 100), the total calorie count would be 40*8 + 60*3 = 500. The total score would go down, though: only 57600000, the best you can do in such trying circumstances.

Given the ingredients in your kitchen and their properties, what is the total score of the highest-scoring cookie you can make with a calorie total of 500?
 */

fun main() {
    Day_15().executeGoals()
}

//@TestResources
@Year(2015)
class Day_15 : AOC_Runner() {

    private val capacityRegex = "(?: capacity )([-]?\\d+),"
    private val durabilityRegex = "(?: durability )([-]?\\d+),"
    private val flavorRegex = "(?: flavor )([-]?\\d+),"
    private val textureRegex = "(?: texture )([-]?\\d+),"
    private val caloriesRegex = "(?: calories )([-]?\\d+)"
    private val regex = Regex("([A-Z][a-z]+):$capacityRegex$durabilityRegex$flavorRegex$textureRegex$caloriesRegex")

    private val ingredients: MutableList<Ingredient> = mutableListOf()
    private val results = mutableListOf<Pair<Int, MutableList<Int>>>()

    init {
        for (allLine in allLines) {
            val find = regex.find(allLine)!!
            ingredients.add(Ingredient(find.groups[1]!!.value, find.int(2), find.int(3), find.int(4), find.int(5), find.int(6)))
        }

        val spoons = 100
        val rangeSize = spoons.toString().length
        val possibilities = spoons.possibilities(ingredients.size)
        val base = possibilities - possibilities.toString().substring(1).toLong()
        val ranges = mutableListOf<IntRange>()
        val values = mutableListOf<Int>()
        for (i1 in 0 until ingredients.size) {
            ranges.add(IntRange(1 + i1 * rangeSize, 1 + i1 * rangeSize + rangeSize - 1))
            values.add(0)
        }
        var iterator: Long = base

        var update = false
        while (iterator < possibilities) {
            for (i in 0 until ingredients.size) {
                values[i] = iterator.toString().substring(ranges[i]).toInt()
            }
            for (i in ingredients.size - 1 downTo 0) {
                if (values[i] > 100) {
                    update = true
                    if (i != 0) {
                        values[i] = 0
                        values[i - 1]++
                    }
                }
            }
            if (update) {
                iterator = 1L
                for (i in 0 until ingredients.size) {
                    iterator *= spoons.getPower()
                    iterator += values[i]
                }
            }

            if (values.sum() == spoons) {
                val savedlist: MutableList<Int> = mutableListOf()
                savedlist.addAll(values)
                val result = calculateResult(values)
                if (result != 0) {
                    val element = Pair(result, savedlist)
                    results.add(element)
                    println(element)
                }
            }

            if(values[0] == 67) {
                break
            }
            iterator++
        }
    }

    override fun executeGoal_1() {
        println(results.maxBy { it.first })
    }

    override fun executeGoal_2() {
        val resultsWith500Calories = results.filter { calculateCalories(it.second) == 500 }
        println(resultsWith500Calories.maxBy { it.first })
    }

    private fun calculateCalories (values: MutableList<Int>) : Int {
        var calories = 0
        for (i in 0 until ingredients.size) {
            calories += ingredients[i].calories * values[i]
        }
        return calories
    }

    private fun calculateResult(values: MutableList<Int>): Int {
        var capacity = 0
        var durability = 0
        var flavor = 0
        var texture = 0
        for (i in 0 until ingredients.size) {
            capacity += ingredients[i].capacity * values[i]
            durability += ingredients[i].durability * values[i]
            flavor += ingredients[i].flavor * values[i]
            texture += ingredients[i].texture * values[i]
        }
//        return capacity * durability * flavor * texture
        return if (capacity < 0) 0 else capacity * if (durability < 0) 0 else durability * if (flavor < 0) 0 else flavor * if (texture < 0) 0 else texture
    }


    infix fun Int.possibilities(num: Int): Long {
        val power = getPower()
        var result = 1L
        for (i in 1..num) {
            result *= power
            result += this
        }
        return result
    }

    private fun Int.getPower(): Int {
        val length = this.toString().length
        return 10.0.pow(length.toDouble()).toInt()
    }

    data class Ingredient(val name: String, val capacity: Int, val durability: Int, val flavor: Int, val texture: Int, val calories: Int) {
        override fun toString(): String {
            return "$name $capacity $durability $flavor $texture $calories"
        }
    }

    infix fun MatchResult.int(index: Int): Int {
        return this.groups[index]!!.value.toInt()
    }
}