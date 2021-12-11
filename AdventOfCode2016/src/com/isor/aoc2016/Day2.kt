package com.isor.aoc2016

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.Year

/**
--- Day 2: Bathroom Security ---
You arrive at Easter Bunny Headquarters under cover of darkness. However, you left in such a rush that you forgot to use the bathroom! Fancy office buildings like this one usually have keypad locks on their bathrooms, so you search the front desk for the code.

"In order to improve security," the document you find says, "bathroom codes will no longer be written down. Instead, please memorize and follow the procedure below to access the bathrooms."

The document goes on to explain that each button to be pressed can be found by starting on the previous button and moving to adjacent buttons on the keypad: U moves up, D moves down, L moves left, and R moves right. Each line of instructions corresponds to one button, starting at the previous button (or, for the first line, the "5" button); press whatever button you're on at the end of each line. If a move doesn't lead to a button, ignore it.

You can't hold it much longer, so you decide to figure out the code as you walk to the bathroom. You picture a keypad like this:

1 2 3
4 5 6
7 8 9
Suppose your instructions are:

ULL
RRDDD
LURDL
UUUUD
You start at "5" and move up (to "2"), left (to "1"), and left (you can't, and stay on "1"), so the first button is 1.
Starting from the previous button ("1"), you move right twice (to "3") and then down three times (stopping at "9" after two moves and ignoring the third), ending up with 9.
Continuing from "9", you move left, up, right, down, and left, ending with 8.
Finally, you move up four times (stopping at "2"), then down once, ending with 5.
So, in this example, the bathroom code is 1985.

Your puzzle input is the instructions from the document you found at the front desk. What is the bathroom code?

Your puzzle answer was 65556.

--- Part Two ---
You finally arrive at the bathroom (it's a several minute walk from the lobby so visitors can behold the many fancy conference rooms and water coolers on this floor) and go to punch in the code. Much to your bladder's dismay, the keypad is not at all like you imagined it. Instead, you are confronted with the result of hundreds of man-hours of bathroom-keypad-design meetings:

    1
  2 3 4
5 6 7 8 9
  A B C
    D
You still start at "5" and stop when you're at an edge, but given the same instructions as above, the outcome is very different:

You start at "5" and don't move at all (up and left are both edges), ending at 5.
Continuing from "5", you move right twice and down three times (through "6", "7", "B", "D", "D"), ending at D.
Then, from "D", you move five more times (through "D", "B", "C", "C", "B"), ending at B.
Finally, after five more moves, you end at 3.
So, given the actual keypad layout, the code would be 5DB3.

Using the same instructions in your puzzle input, what is the correct bathroom code?

Your puzzle answer was CB779.
 */

fun main() {
    Day2().executeGoals()
}

//@TestResources
@Year(2016)
class Day2: AOC_Runner() {

    private val keyPad = mapOf(Pair(0,0) to "1", Pair(0,1) to "2", Pair(0,2) to "3",
                    Pair(1,0) to "4", Pair(1,1) to "5", Pair(1,2) to "6",
                    Pair(2,0) to "7", Pair(2,1) to "8", Pair(2,2) to "9")

    private val keyPad2 = mapOf(Pair(0,2) to "1",
                        Pair(1,1) to "2", Pair(1,2) to "3", Pair(1,3) to "4",
                        Pair(2,0) to "5", Pair(2,1) to "6", Pair(2,2) to "7", Pair(2,3) to "8", Pair(2,4) to "9",
                        Pair(3,1) to "A", Pair(3,2) to "B", Pair(3,3) to "C",
                        Pair(4,2) to "D"
    )

    private val instructions = mapOf("L" to (0 to -1), "R" to (0 to 1), "U" to (-1 to 0), "D" to (1 to 0))

    override fun executeGoal_1() {
        println(doInstructions(keyPad))
    }

    override fun executeGoal_2() {
        println(doInstructions(keyPad2))
    }

    private fun doInstructions(keyPad: Map<Pair<Int, Int>, String>): String {
        var pos = keyPad.filter { it.value == "5" }.keys.first()

        return allLines.joinToString("") {
            it.chunked(1).forEach { s ->
                val instruction = instructions[s]!!
                val element = pos + instruction
                if (keyPad.keys.contains(element))
                    pos = element
            }
            keyPad[pos]!!
        }
    }
}