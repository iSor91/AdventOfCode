package com.isor.aoc2021

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year

fun main() {
    Day22().executeGoals()
}

@TestResources(2)
@Year(2021)
class Day22: AOC_Runner() {

    val commandRegex = "(on|off) x=(-?\\d+)..(-?\\d+),y=(-?\\d+)..(-?\\d+),z=(-?\\d+)..(-?\\d+)".toRegex()

    val commands = allLines.map {
        val find = commandRegex.find(it)
        val groupValues = find!!.groupValues
        val isOn = groupValues[1]
        Pair(
            "on"==isOn,
            Triple(groupValues[2].toInt()..groupValues[3].toInt(),
                groupValues[4].toInt()..groupValues[5].toInt(),
                groupValues[6].toInt()..groupValues[7].toInt()))
    }

    fun IntRange.f1(): List<Int> {
        return this.filter{ x-> x > -51 && x < 51 }
    }

    fun IntRange.f2(): List<Int> {
        return this.toList()
    }

    override fun executeGoal_1() {
        val cubes = mutableSetOf<Triple<Int,Int,Int>>()
//
//        commands.forEachIndexed { i,command ->
//            val cubeCommands = command.position
//            cubeCommands.first.f1().forEach { x ->
//                cubeCommands.second.f1().forEach { y ->
//                    cubeCommands.third.f1().forEach { z ->
//                        if(command.isOn) {
//                            cubes.add(Triple(x,y,z))
//                        } else {
//                            cubes.remove(Triple(x,y,z))
//                        }
//                    }
//                }
//            }
//        }
        println(cubes.size)
    }

    override fun executeGoal_2() {
        var grid = mutableListOf<Triple<IntRange, IntRange, IntRange>>()
        commands.forEachIndexed { i, cube ->
            val newGrid = mutableListOf<Triple<IntRange, IntRange, IntRange>>()
            if(!cube.first) {
                (grid.indices).forEach {
                    val overLapper = cube.second
                    val overLappee = grid[it]
                    val calculateOverLap: Set<Triple<IntRange, IntRange, IntRange>> = overLapper.calculateOverLap(overLappee)
                    val elements = calculateOverLap.filter { c -> !overLapper.fullOverLap(c) }
                    newGrid.addAll(elements)
                }
                grid = newGrid
            }
            if(cube.first) {
                grid.add(cube.second)
            }
            println("$i - ${grid.size}")
        }

        val allBoxes = grid.sortedWith{t1,t2 ->
            val x = t1.first.first - t2.first.first
            val y = t1.second.first - t2.second.first
            val z = t1.third.first - t2.third.first
            if (x == 0) if(y==0) z else y else x
        }

        var sum = 0L
        val common: MutableSet<Triple<IntRange,IntRange,IntRange>> = mutableSetOf()
        allBoxes.forEachIndexed { i,c ->
            sum += c.size()
            allBoxes.forEach { other ->
                if(other != c) {
                    val commonPart = c.calculateCommonPart(other)
                    if (commonPart != null) {
                        if(common.contains(commonPart)) {
                            sum -= commonPart.size()
                            common.remove(commonPart)
                        } else {
                            common.add(commonPart)
                        }
                    }
                }
            }
            println("${grid.size} / $i")
        }
        println(sum)
    }

    fun Triple<IntRange, IntRange, IntRange>.size(): Long {
        return first.chunked(1).size.toLong() * second.chunked(1).size * third.chunked(1).size
    }

    fun Triple<IntRange, IntRange, IntRange>.fullOverLap(other: Triple<IntRange, IntRange, IntRange>) : Boolean {
        return other.first.first in first
                && other.first.last in first
                && other.second.first in second
                && other.second.last in second
                && other.third.first in third
                && other.third.last in third
    }


    fun Triple<IntRange, IntRange, IntRange>.calculateCommonPart(overLappee: Triple<IntRange, IntRange, IntRange>): Triple<IntRange, IntRange, IntRange>? {
        val x = overLapRanges(first, overLappee.first).filter { !it.first.isEmpty() && it.second == COMMON }
        val y = overLapRanges(second, overLappee.second).filter { !it.first.isEmpty() && it.second == COMMON }
        val z = overLapRanges(third, overLappee.third).filter { !it.first.isEmpty() && it.second == COMMON }

        if(x.size > 1 || y.size > 1 || z.size > 1) {
            throw IllegalStateException("ez nem lehet...")
        } else if(x.isEmpty() || y.isEmpty() || z.isEmpty()) {
            return null
        }
        return Triple(x.first().first, y.first().first, z.first().first)
    }

    fun Triple<IntRange, IntRange, IntRange>.calculateOverLap(overLappee: Triple<IntRange, IntRange, IntRange>): Set<Triple<IntRange, IntRange, IntRange>> {
        val x = overLapRanges(first, overLappee.first).filter { !it.first.isEmpty() }
        val y = overLapRanges(second, overLappee.second).filter { !it.first.isEmpty() }
        val z = overLapRanges(third, overLappee.third).filter { !it.first.isEmpty() }

        val splittedCubes = mutableSetOf<Triple<IntRange, IntRange, IntRange>>()
        x.forEach { xr ->
            y.forEach { yr ->
                z.forEach { zr ->
                    splittedCubes.add(Triple(xr.first, yr.first, zr.first))
                }
            }
        }
        return splittedCubes
    }

    private fun overLapRanges(overLapper: IntRange, overLappee: IntRange): Array<Pair<IntRange, Int>> {

        val x11 = overLapper.first()
        val x12 = overLapper.last()
        val x21 = overLappee.first()
        val x22 = overLappee.last()

        return if(x11 < x21 && x12 in x21..x22) {
            //before
            arrayOf((x21..x12) to COMMON,(x12+1..x22) to OVERLAPPEE)
        } else if(x11 in x21..x22 && x12 > x22) {
            //after
            arrayOf((x21 until x11) to OVERLAPPEE,(x11..x22) to COMMON)
        } else if(x11 in x21..x22 && x12 in x21..x22) {
            //overlapper inside
            arrayOf((x21 until x11) to OVERLAPPEE,(x11..x12) to COMMON,(x12+1..x22) to OVERLAPPEE)
        } else {
            //no overlap or full overlap
            arrayOf((x21..x22) to OVERLAPPEE)
        }
    }

    companion object {
        const val COMMON = 0
        const val OVERLAPPEE = 2
    }

}
