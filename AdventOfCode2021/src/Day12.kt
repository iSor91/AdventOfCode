package com.isor.aoc2021

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year

fun main() {
    Day12().executeGoals()
}

//@TestResources
@Year(2021)
class Day12: AOC_Runner() {

    data class Cave(val name: String, val connections: MutableSet<Cave>, val isSmall: Boolean) {
        override fun equals(other: Any?): Boolean = other is Cave && EssentialData(this) == EssentialData(other)
        override fun hashCode(): Int = EssentialData(this).hashCode()
        override fun toString(): String {
            return "$name - $isSmall"
        }

        fun toStringWithConnections(): String {
            return "${this.name} $isSmall - ${connections.map { it.name }.joinToString (",") }"
        }
    }


    data class EssentialData(val name: String) {
        constructor(cave: Cave) : this(name = cave.name)
    }

    val caveMap: MutableMap<String, Cave> = mutableMapOf()

    val caves = allLines.forEach {
        val twoCaves = it.split(Regex("-"))
        val cave1 =
            caveMap.getOrPut(twoCaves[0]) { Cave(twoCaves[0], mutableSetOf(), twoCaves[0].matches(Regex("[a-z]+"))) }
        val cave2 =
            caveMap.getOrPut(twoCaves[1]) { Cave(twoCaves[1], mutableSetOf(), twoCaves[1].matches(Regex("[a-z]+"))) }
        cave1.connections.add(cave2)
        cave2.connections.add(cave1)
    }

    override fun executeGoal_1() {
        val startCave = caveMap["start"]!!
        val allPaths = mutableListOf<List<Cave>>()

        startCave.connections.forEach {
            val pathStart = mutableListOf(startCave, it)
            allPaths.addAll(visitCaves(it, pathStart))
        }

//        println(allPaths.map { it.joinToString(",") { c -> c.name } }.sorted().joinToString ("\n"))
        println(allPaths.size)
    }

    private fun visitCaves(startCave: Cave, path: MutableList<Cave>): List<List<Cave>> {
        val allPaths = mutableListOf<List<Cave>>()
        startCave.connections.forEach {
            val extendedPath = path.copy
            extendedPath.add(it)
            if(it.name == "end") {
                allPaths.add(extendedPath)
                return@forEach
            }
            if(it.isSmall && path.contains(it))
                return@forEach
            allPaths.addAll(visitCaves(it, extendedPath))
        }
        return allPaths
    }

    fun visitCaves2(startCave: Cave, path: MutableList<Cave>, smallCaveVisited: Boolean): List<List<Cave>>{
        val allPaths = mutableListOf<List<Cave>>()
        startCave.connections.forEach {
            var scv = smallCaveVisited
            if(it.name == "start") {
                return@forEach
            }
            val extendedPath = path.copy
            extendedPath.add(it)
            if(it.name == "end") {
                allPaths.add(extendedPath)
                return@forEach
            }
            if(it.isSmall && path.contains(it) && scv)
                return@forEach
            if(it.isSmall && path.contains(it) && !scv)
                scv = true
            allPaths.addAll(visitCaves2(it, extendedPath, scv))
        }
        return allPaths
    }

    override fun executeGoal_2() {
        val startCave = caveMap["start"]!!
        val allPaths = mutableListOf<List<Cave>>()

        startCave.connections.forEach {
            val pathStart = mutableListOf(startCave, it)
            allPaths.addAll(visitCaves2(it, pathStart, false))
        }

//        println(allPaths.map { it.joinToString(",") { c -> c.name } }.sorted().joinToString ("\n"))
        println(allPaths.size)
    }
}