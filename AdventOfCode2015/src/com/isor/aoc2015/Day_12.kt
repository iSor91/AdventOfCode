package com.isor.aoc2015

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.Year

/**
 * --- Day 12: JSAbacusFramework.io ---
    Santa's Accounting-Elves need help balancing the books after a recent order. Unfortunately, their accounting software uses a peculiar storage format. That's where you come in.

    They have a JSON document which contains a variety of things: arrays ([1,2,3]), objects ({"a":1, "b":2}), numbers, and strings. Your first job is to simply find all of the numbers throughout the document and add them together.

    For example:

    [1,2,3] and {"a":2,"b":4} both have a sum of 6.
    [[[3]]] and {"a":{"b":4},"c":-1} both have a sum of 3.
    {"a":[-1,1]} and [-1,{"a":1}] both have a sum of 0.
    [] and {} both have a sum of 0.
    You will not encounter any strings containing numbers.

    What is the sum of all numbers in the document?

    Your puzzle answer was 156366.

    --- Part Two ---
    Uh oh - the Accounting-Elves have realized that they double-counted everything red.

    Ignore any object (and all of its children) which has any property with the value "red". Do this only for objects ({...}), not arrays ([...]).

    [1,2,3] still has a sum of 6.
    [1,{"c":"red","b":2},3] now has a sum of 4, because the middle object is ignored.
    {"d":"red","e":[1,2,3,4],"f":5} now has a sum of 0, because the entire structure is ignored.
    [1,"red",5] has a sum of 6, because "red" in an array has no effect.
    Your puzzle answer was 96852.
 */

fun main() {
    Day_12().executeGoals()
}

@Year(2015)
//@TestResources
class Day_12 : AOC_Runner() {
    override fun executeGoal_1() {

        val regex = Regex("\\d+|-\\d+")
        var sum : Int = 0
        allLines.forEach{
            val findAll = regex.findAll(it)
            findAll.forEach {
                sum += it.value.toInt()
                //println("${it.value} -> $sum")
            }
        }
        println(sum)
    }

    override fun executeGoal_2() {

        var level : Int = 0
        var root = MyJsonObject(0, null, true)
        var current : MyJsonObject = root
        var inArray : Boolean = false
        for (line in allLines) {
            if (line.contains("{") || line.contains("[")) {
                inArray = false
                level++
                val newJsonObject = MyJsonObject(level, current, line.contains("["))
                current.children.add(newJsonObject)
                current = newJsonObject
            } else if (line.contains("}") || line.contains("]")) {
                level--
                current = current.parent!!
            } else {
                val split = line.split(":")
                var s = split[0].trim()
                if(split.size == 2) {
                     s = split[1].trim()
                }
                s = s.replace("\"", "")
                s = s.replace(",","")
                try {
                    current.integers.add(s.toInt())
                } catch (e:NumberFormatException) {
                    if(!inArray)
                        current.strings.add(s)
                }
            }

        }
        println(root)
        val sum = sumObjects(root)
        println()
        println(sum)
    }

    private fun sumObjects(current: MyJsonObject): Int {
        var sum = 0
        if (current.level > 0 && current.strings.contains("red") && !current.array ) {
            return sum;
        } else {
            for (child in current.children) {
                sum += sumObjects(child)
            }
        }
        return sum + current.integers.sum()
    }


    data class MyJsonObject (val level : Int, val parent : MyJsonObject?, val array: Boolean) {
        val strings : MutableSet<String> = mutableSetOf()
        val integers : MutableList<Int> = mutableListOf()
        val children : MutableList<MyJsonObject> = mutableListOf()

        override fun toString(): String {
            return "\n${".".repeat(level)} $level $strings - $integers $children"
        }
    }

}