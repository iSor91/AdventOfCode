package com.isor.aoc2021

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year
import java.lang.IllegalArgumentException

fun main() {
    Day25().executeGoals()
}

//@TestResources
@Year(2021)
class Day25: AOC_Runner() {

    val initialState = allLines.mapIndexed {
        y, s ->
            s.chunked(1).mapIndexed {
                x,cucumba ->
                Pair(x,y) to cucumba
            }
    }.flatten().toMap()

    val maxX = initialState.keys.maxByOrNull { it.first }!!.first
    val maxY = initialState.keys.maxByOrNull { it.second }!!.second

    val cucumbaListByFacing  = mutableMapOf(
        ">" to initialState.filter { it.value == ">" }.map { it.key }.toMutableList(),
        "v" to initialState.filter { it.value == "v" }.map { it.key }.toMutableList()
    )

    override fun executeGoal_1() {
        var cucumbaMap = initialState.toMutableMap()
//        printCucumbaMap(cucumbaMap)
//        println()
        var moving = true
        var i = 0
        while(moving) {
            val eastMoved = cucumbaStep(cucumbaMap, ">")
            val southMoved = cucumbaStep(eastMoved, "v")
            if(southMoved == cucumbaMap) {
                moving = false
            } else {
                cucumbaMap = southMoved
            }
            i++

            println(i)
//            printCucumbaMap(cucumbaMap)
//            println()
        }

    }

    fun printCucumbaMap(map: MutableMap<Pair<Int,Int>,String>) {
        (0..maxY).forEach { y ->
            (0..maxX).forEach { x ->
                print(map[Pair(x,y)] ?: ".")
            }
            println()
        }
    }

    fun cucumbaStep(map: MutableMap<Pair<Int,Int>,String>,type: String): MutableMap<Pair<Int,Int>,String> {
        val movingCucumbas = map.filter { it.value == type }
        val stationareCucumbas = map.filter { it.value != type && it.value!="." }
        if(movingCucumbas.isEmpty()) {
            return map
        }
        val newMap = mutableMapOf<Pair<Int,Int>,String>()
        movingCucumbas.forEach {
            val nextPos = when(type) {
                ">" -> it.key.first.boundedPlus(1,0,maxX) to it.key.second
                "v" -> it.key.first to it.key.second.boundedPlus(1,0,maxY)
                else -> throw IllegalArgumentException("ezek nem mozognak $type!!!")
            }
            if(map[nextPos] == null || map[nextPos] == ".") {
                newMap[nextPos] = it.value
            } else {
                newMap[it.key] = it.value
            }
        }
        stationareCucumbas.forEach {
            newMap[it.key] = it.value
        }
        return newMap
    }

    override fun executeGoal_2() {
    }
}