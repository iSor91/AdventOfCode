package com.isor.aoc2015

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year

/**
 * --- Day 10: Elves Look, Elves Say ---
 * Today, the Elves are playing a game called look-and-say. They take turns making sequences by reading aloud the previous sequence and using that reading as the next sequence. For example, 211 is read as "one two, two ones", which becomes 1221 (1 2, 2 1s).
 *
 * Look-and-say sequences are generated iteratively, using the previous value as input for the next step. For each step, take the previous value, and replace each run of digits (like 111) with the number of digits (3) followed by the digit itself (1).
 *
 * For example:
 *
 * 1 becomes 11 (1 copy of digit 1).
 * 11 becomes 21 (2 copies of digit 1).
 * 21 becomes 1211 (one 2 followed by one 1).
 * 1211 becomes 111221 (one 1, one 2, and two 1s).
 * 111221 becomes 312211 (three 1s, two 2s, and one 1).
 * Starting with the digits in your puzzle input, apply this process 40 times. What is the length of the result?
 *
 * Your puzzle answer was 252594.
 *
 * --- Part Two ---
 * Neat, right? You might also enjoy hearing John Conway talking about this sequence (that's Conway of Conway's Game of Life fame).
 *
 * Now, starting again with the digits in your puzzle input, apply this process 50 times. What is the length of the new result?
 *
 * Your puzzle answer was 3579328.
 *
 */

fun main() {
    Day_10().executeGoals()
}

@Year(2015)
//@TestResources
class Day_10 : AOC_Runner() {

    override fun executeGoal_1() {
        var input = allLines.first()
        for (i in 0..39) {
            input = convert(input)
        }
        println("after 40: ${input.length.toString().apply(::println)}")

    }

    override fun executeGoal_2() {
        var input = allLines.first()
        for (i in 0..49) {
            input = convert(input)
        }
        println("after 50: ${input.length.toString().apply(::println)}")

    }

    private fun convert(input: String) : String {

        var currentChar : Char = input[0]
        var count : Int = 1
        var sb : StringBuilder = java.lang.StringBuilder()
        for (i in 1..input.length-1) {
            if(input[i] == currentChar) {
                count++
            } else {
                sb.append(count,currentChar)
                currentChar=input[i]
                count=1
            }
        }
        sb.append(count, currentChar)
        return sb.toString()

    }
}