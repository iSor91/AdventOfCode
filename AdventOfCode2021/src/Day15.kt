package com.isor.aoc2021

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year

fun main() {
    Day15().executeGoals()
}

//@TestResources
@Year(2021)
class Day15: AOC_Runner() {

    data class Chiton(val pos: Pair<Int,Int>, val risk: Int)

    val averageCost = allLines.map { it.chunked(1) }.flatten().map { it.toInt() }.average().toInt()

    val chitons = allLines.mapIndexed {
            i, s -> s.chunked(1).mapIndexed() {
                j,c ->
                    Chiton(Pair(j,i), c.toInt() )
                }
    }.flatten()

    val chitonMap = chitons.associateBy { it.pos }.toMutableMap()

    override fun executeGoal_1() {
//        val start = chitons.first()
//        val end = chitons.last()
//
//        val fScore = mutableMapOf<Chiton, Int>()
//        val gScore = mutableMapOf<Chiton, Int>()
//        val cameFrom = mutableMapOf<Chiton, Chiton>()
//        val openSet = mutableSetOf(start)
//
//        chitons.forEach {
//            fScore[it] = Int.MAX_VALUE
//            gScore[it] = Int.MAX_VALUE
//        }
//
//        gScore[start] = 0
//        fScore[start] = getCostToGoal(start, end)
//
//        while (openSet.isNotEmpty()) {
//            val current = openSet.minByOrNull { fScore[it]!! }!!
//
//            if(current == end) {
//                val reconstructPath = reconstructPath(cameFrom, end)
//                println(reconstructPath.joinToString("\n"))
//                println(reconstructPath.filter { it != start }.sumOf { it.risk })
//                return
//            }
//
//            openSet.remove(current)
//
//            val neighbors = getNeighbors(current, chitonMap)
//            for (i in neighbors) {
//               val tentativeG = gScore[current]!! + i.risk
//               if(tentativeG < gScore[i]!!) {
//                   cameFrom[i] = current
//                   gScore[i] = tentativeG
//                   fScore[i] = tentativeG + getCostToGoal(i, end)
//                   openSet.add(i)
//               }
//            }
//        }
    }

    private fun reconstructPath(cameFrom: MutableMap<Chiton, Chiton>, current: Chiton): List<Chiton> {
        var curr = current
        val totalPath = mutableSetOf<Chiton>(current)
        while (cameFrom.keys.contains( curr)) {
            curr = cameFrom[curr]!!
            totalPath.add(curr)
        }
        return totalPath.reversed()

    }

    private fun getNeighbors(current: Chiton, map: Map<Pair<Int, Int>, Chiton>) : Set<Chiton>{
        val pos = current.pos
        val of = setOf(
            Pair(pos.first - 1, pos.second),
            Pair(pos.first + 1, pos.second),
            Pair(pos.first, pos.second - 1),
            Pair(pos.first, pos.second + 1)
        )
        return map.filter { of.contains(it.key) }.map { it.value }.toSet()
    }

    private fun getCostToGoal(start: Chiton, end: Chiton) =
        ((end.pos.first - start.pos.first) + (end.pos.second - start.pos.second))

    override fun executeGoal_2() {
        val extendedChitons = chitons.toMutableList()
        val width = chitons.maxOf { it.pos.first } + 1
        val height = chitons.maxOf { it.pos.second } + 1
        chitons.forEach {
            (0..4).forEach {
                i ->
                (0..4).forEach inner@{
                    j ->
                    if(i == 0 && j == 0) {
                        return@inner
                    }
                    val risk = (it.risk + i + j) % 10 + ((it.risk + i + j) /10)
                    val newChiton = Chiton(Pair(it.pos.first + i * width, it.pos.second + j * height), risk)
                    extendedChitons.add(newChiton)
                }
            }
        }
        println(extendedChitons.filter { it.risk>9 })
        val extendedChitonMap = extendedChitons.associateBy { it.pos }.toMap()

//        val maxY = extendedChitons.maxOf { it.pos.first }
//        val maxX = extendedChitons.maxOf { it.pos.second }
//
//        (0..maxY).forEach { i ->
//            (0..maxX).forEach { j ->
//                print(extendedChitonMap[Pair(j,i)]!!.risk)
//            }
//            println()
//        }


//
        val start = extendedChitons.first()
        val end = extendedChitons.last()

        val fScore = mutableMapOf<Chiton, Int>()
        val gScore = mutableMapOf<Chiton, Int>()
        val cameFrom = mutableMapOf<Chiton, Chiton>()
        val openSet = mutableSetOf(start)

        extendedChitons.forEach {
            fScore[it] = Int.MAX_VALUE
            gScore[it] = Int.MAX_VALUE
        }

        gScore[start] = 0
        fScore[start] = getCostToGoal(start, end)

        while (openSet.isNotEmpty()) {
            val current = openSet.minByOrNull { fScore[it]!! }!!

            if(current == end) {
                val reconstructPath = reconstructPath(cameFrom, end)
//                println(reconstructPath.joinToString("\n"))
                println(reconstructPath.filter { it != start }.sumOf { it.risk })
                return
            }

            openSet.remove(current)

            val neighbors = getNeighbors(current, extendedChitonMap)
            for (i in neighbors) {
                val tentativeG = gScore[current]!! + i.risk
                if(tentativeG < gScore[i]!!) {
                    cameFrom[i] = current
                    gScore[i] = tentativeG
                    fScore[i] = tentativeG + getCostToGoal(i, end)
                    openSet.add(i)
                }
            }
        }
    }
}