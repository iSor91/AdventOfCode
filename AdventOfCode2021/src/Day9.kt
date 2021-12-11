package com.isor.aoc2021

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year
import kotlin.math.max
import kotlin.math.min

/**
--- Day 9: Smoke Basin ---
These caves seem to be lava tubes. Parts are even still volcanically active; small hydrothermal vents release smoke into the caves that slowly settles like rain.

If you can model how the smoke flows through the caves, you might be able to avoid it and be that much safer. The submarine generates a heightmap of the floor of the nearby caves for you (your puzzle input).

Smoke flows to the lowest point of the area it's in. For example, consider the following heightmap:

2199943210
3987894921
9856789892
8767896789
9899965678
Each number corresponds to the height of a particular location, where 9 is the highest and 0 is the lowest a location can be.

Your first goal is to find the low points - the locations that are lower than any of its adjacent locations. Most locations have four adjacent locations (up, down, left, and right); locations on the edge or corner of the map have three or two adjacent locations, respectively. (Diagonal locations do not count as adjacent.)

In the above example, there are four low points, all highlighted: two are in the first row (a 1 and a 0), one is in the third row (a 5), and one is in the bottom row (also a 5). All other locations on the heightmap have some lower adjacent location, and so are not low points.

The risk level of a low point is 1 plus its height. In the above example, the risk levels of the low points are 2, 1, 6, and 6. The sum of the risk levels of all low points in the heightmap is therefore 15.

Find all of the low points on your heightmap. What is the sum of the risk levels of all low points on your heightmap?

Your puzzle answer was 577.

--- Part Two ---
Next, you need to find the largest basins so you know what areas are most important to avoid.

A basin is all locations that eventually flow downward to a single low point. Therefore, every low point has a basin, although some basins are very small. Locations of height 9 do not count as being in any basin, and all other locations will always be part of exactly one basin.

The size of a basin is the number of locations within the basin, including the low point. The example above has four basins.

The top-left basin, size 3:

2199943210
3987894921
9856789892
8767896789
9899965678
The top-right basin, size 9:

2199943210
3987894921
9856789892
8767896789
9899965678
The middle basin, size 14:

2199943210
3987894921
9856789892
8767896789
9899965678
The bottom-right basin, size 9:

2199943210
3987894921
9856789892
8767896789
9899965678
Find the three largest basins and multiply their sizes together. In the above example, this is 9 * 14 * 9 = 1134.

What do you get if you multiply together the sizes of the three largest basins?

Your puzzle answer was 1069200.
 */

fun main() {
    Day9().executeGoals()
}

//@TestResources
@Year(2021)
class Day9: AOC_Runner() {

    data class Height(val original: Int, val surroundingSum: Int, val coord: Pair<Int,Int>, var inbasin: Boolean)

    val map = allLines.map { it.chunked(1).map { s -> s.toInt() } }

    val map2 = mutableMapOf<Pair<Int,Int>,Height>()

    override fun executeGoal_1() {
        (map.indices).forEach{
            repeat((map[it].indices).count()) { i->
                if(map[it][i] == 9){
                    map2[Pair(it, i)] = Height(map[it][i], 9, Pair(it, i), false)
                    return@repeat
                }
                var sorround: Int = -2
                (max(i-1,0)..min(i+1, map[it].size-1)).forEach { f ->
                    sorround +=when(map[it][f]) {
                        in 0 .. map[it][i] -> 1
                        else -> -1
                    }
                }
                (max(it-1,0)..min(it+1, map.size-1)).forEach { f ->
                    sorround +=when(map[f][i]) {
                        in 0 .. map[it][i] -> 1
                        else -> -1
                    }
                }
                if(it == 0 || it == map.size-1) sorround += -1
                if(i == 0 || i == map[it].size-1) sorround += -1

                map2[Pair(it, i)] = Height(map[it][i], sorround, Pair(it, i), false)
            }
        }
        println(map2.filter { it.value.surroundingSum == -4 }.values.sumOf { it.original + 1 })
    }

    override fun executeGoal_2() {
        val localMins = map2.filter { it.value.surroundingSum == -4 }

        val basins : MutableList<Set<Height>> = mutableListOf()
        localMins.forEach{
            val base = it.value
            val basin = it.key.getAdjacentBasinHeights(map2)
            basin.add(base)
            basins.add(basin)
        }

        println(basins.sortedBy { it.size }.map { it.size }.takeLast(3).reduce{a,b->a*b})
    }
}

private fun Pair<Int, Int>.getAdjacentBasinHeights(map : Map<Pair<Int,Int>, Day9.Height>) : MutableSet<Day9.Height> {
    val listOf = listOf(
            Pair(this.first - 1, this.second),
            Pair(this.first + 1, this.second),
            Pair(this.first, this.second - 1),
            Pair(this.first, this.second + 1)
    )
    val set = mutableSetOf<Day9.Height>()

    listOf.forEach{
        val height = map[it]
        if(height == null || height.inbasin || height.original==9) return@forEach
        set.add(height)
        height.inbasin=true
        set.addAll(it.getAdjacentBasinHeights(map))
    }

    return set
}
