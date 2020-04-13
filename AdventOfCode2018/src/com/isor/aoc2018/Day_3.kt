package com.isor.aoc2018

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.Year

/**
--- Day 3: No Matter How You Slice It ---
The Elves managed to locate the chimney-squeeze prototype fabric for Santa's suit (thanks to someone who helpfully wrote its box IDs on the wall of the warehouse in the middle of the night). Unfortunately, anomalies are still affecting them - nobody can even agree on how to cut the fabric.

The whole piece of fabric they're working on is a very large square - at least 1000 inches on each side.

Each Elf has made a claim about which area of fabric would be ideal for Santa's suit. All claims have an ID and consist of a single rectangle with edges parallel to the edges of the fabric. Each claim's rectangle is defined as follows:

The number of inches between the left edge of the fabric and the left edge of the rectangle.
The number of inches between the top edge of the fabric and the top edge of the rectangle.
The width of the rectangle in inches.
The height of the rectangle in inches.
A claim like #123 @ 3,2: 5x4 means that claim ID 123 specifies a rectangle 3 inches from the left edge, 2 inches from the top edge, 5 inches wide, and 4 inches tall. Visually, it claims the square inches of fabric represented by # (and ignores the square inches of fabric represented by .) in the diagram below:

...........
...........
...#####...
...#####...
...#####...
...#####...
...........
...........
...........
The problem is that many of the claims overlap, causing two or more claims to cover part of the same areas. For example, consider the following claims:

#1 @ 1,3: 4x4
#2 @ 3,1: 4x4
#3 @ 5,5: 2x2
Visually, these claim the following areas:

........
...2222.
...2222.
.11XX22.
.11XX22.
.111133.
.111133.
........
The four square inches marked with X are claimed by both 1 and 2. (Claim 3, while adjacent to the others, does not overlap either of them.)

If the Elves all proceed with their own plans, none of them will have enough fabric. How many square inches of fabric are within two or more claims?

Your puzzle answer was 110891.

--- Part Two ---
Amidst the chaos, you notice that exactly one claim doesn't overlap by even a single square inch of fabric with any other claim. If you can somehow draw attention to it, maybe the Elves will be able to make Santa's suit after all!

For example, in the claims above, only claim 3 is intact after all claims are made.

What is the ID of the only claim that doesn't overlap?

Your puzzle answer was 297.
 */

fun main(){
    Day_3().executeGoals()
}

@Year(2018)
class Day_3 : AOC_Runner() {

    private val occupations: MutableList<Occupation>

    init {
        occupations = mutableListOf()
        this.allLines.forEach{line ->
            occupations.add(
                    line.process()
            )}
    }

    override fun executeGoal_1() {
        val positionOccupationMap : MutableMap<Position, Int> = mutableMapOf()
        occupations.forEach { o -> o.getAllPositions().forEach{ p -> positionOccupationMap.increaseOrCreate(p)}}
        val filtered = positionOccupationMap.entries.filter { e -> e.value > 1}
        println(filtered.size)
    }

    override fun executeGoal_2() {
        val positionOccupationMap : MutableMap<Position, Occupation> = mutableMapOf()
        occupations.forEach {
            o ->
                o.getAllPositions().forEach{ p ->
                    val a = positionOccupationMap.occupy(p, o)
                    if(a != null) {
                        o.overLapping()
                        a.overLapping()
                    }
                }
        }
        val filtered = occupations.filter { o -> o.notOverlapping }
        filtered.forEach(System.out::println)
    }

    private fun String.process() : Occupation{
        val split = this.split("@",":","x")
        return Occupation(split[1].trim().toPosition(),split[2].trim().toInt(), split[3].trim().toInt(), split[0].trim().substring(1).toInt())
    }

    private fun String.toPosition() : Position {
        val coordinates = this.split(",")
        return Position(coordinates[0].toInt(), coordinates[1].toInt())
    }

    private fun MutableMap<Position, Occupation>.occupy(key: Position, occ: Occupation) : Occupation? {
        val value = this[key]
        if(value == null) {
            this[key] = occ
            return null
        }
        return value
    }

    private fun MutableMap<Position, Int>.increaseOrCreate(key: Position) {
        val value = this[key]
        if(value == null) {
            this[key] = 1
        } else {
            this[key] = value+1
        }
    }

    class Occupation(private val p: Position, private val w: Int, private val h: Int, val id: Int) {

        var notOverlapping : Boolean = true

        fun overLapping() {
            this.notOverlapping = false
        }

        fun getAllPositions(): List<Position> {
            val list = mutableListOf<Position>()
            for(i in 0 until h) {
                for(j in 0 until w) {
                    list.add(Position(p.x+j,p.y+i))
                }
            }
            return list
        }

        override fun toString(): String {
            return """id = $id $p w=$w h=$h"""
        }
    }

    data class Position(val x: Int, val y: Int) {
        override fun toString(): String {
            return "x=$x y=$y"
        }
    }

}