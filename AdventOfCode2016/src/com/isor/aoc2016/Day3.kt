package com.isor.aoc2016

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.Year

/**
--- Day 3: Squares With Three Sides ---
Now that you can think clearly, you move deeper into the labyrinth of hallways and office furniture that makes up this part of Easter Bunny HQ. This must be a graphic design department; the walls are covered in specifications for triangles.

Or are they?

The design document gives the side lengths of each triangle it describes, but... 5 10 25? Some of these aren't triangles. You can't help but mark the impossible ones.

In a valid triangle, the sum of any two sides must be larger than the remaining side. For example, the "triangle" given above is impossible, because 5 + 10 is not larger than 25.

In your puzzle input, how many of the listed triangles are possible?

Your puzzle answer was 862.

--- Part Two ---
Now that you've helpfully marked up their design documents, it occurs to you that triangles are specified in groups of three vertically. Each set of three numbers in a column specifies a triangle. Rows are unrelated.

For example, given the following specification, numbers with the same hundreds digit would be part of the same triangle:

101 301 501
102 302 502
103 303 503
201 401 601
202 402 602
203 403 603
In your puzzle input, and instead reading by columns, how many of the listed triangles are possible?

Your puzzle answer was 1577.
 */

fun main() {
    Day3().executeGoals()
}

//@TestResources(1)
@Year(2016)
class Day3: AOC_Runner() {

    private val triangles = allLines
        .map { it.split(Regex("[\\s]+")).filterNot { s -> s.isEmpty() }
            .map { s -> s.toInt() }
        }

    override fun executeGoal_1() {
        println(triangles.map { it.sorted() }.count { (it[0] + it[1]) > it[2] })
    }

    override fun executeGoal_2() {
        val chunked = triangles.chunked(3).map{
            listOf(
                listOf(it[0][0],it[1][0],it[2][0]),
                listOf(it[0][1],it[1][1],it[2][1]),
                listOf(it[0][2],it[1][2],it[2][2])
            )
        }.flatten()
        println(chunked.map { it.sorted() }.count { (it[0] + it[1]) > it[2] })
    }
}