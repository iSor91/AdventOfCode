package com.isor.aoc2021

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year
import kotlin.math.max
import kotlin.math.min

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
