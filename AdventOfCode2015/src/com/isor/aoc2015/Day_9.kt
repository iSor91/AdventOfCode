package com.isor.aoc2015

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year

/**
 * 
 * --- Day 9: All in a Single Night ---
 * Every year, Santa manages to deliver all of his presents in a single night.
 * 
 * This year, however, he has some new locations to visit; his elves have provided him the distances between every pair of locations. He can start and end at any two (different) locations he wants, but he must visit each location exactly once. What is the shortest distance he can travel to achieve this?
 * 
 * For example, given the following distances:
 * 
 * London to Dublin = 464
 * London to Belfast = 518
 * Dublin to Belfast = 141
 * The possible routes are therefore:
 * 
 * Dublin -> London -> Belfast = 982
 * London -> Dublin -> Belfast = 605
 * London -> Belfast -> Dublin = 659
 * Dublin -> Belfast -> London = 659
 * Belfast -> Dublin -> London = 605
 * Belfast -> London -> Dublin = 982
 * The shortest of these is London -> Dublin -> Belfast = 605, and so the answer is 605 in this example.
 * 
 * What is the distance of the shortest route?
 *
 * --- Part Two ---
 * The next year, just to show off, Santa decides to take the route with the longest distance instead.
 * He can still start and end at any two (different) locations he wants, and he still must visit each location exactly once.
 * For example, given the distances above, the longest route would be 982 via (for example) Dublin -> London -> Belfast.
 * What is the distance of the longest route?
 */


fun main() {
    Day_9().init().executeGoals()
}

@Year(2015)
//@TestResources
class Day_9: AOC_Runner() {

    private val allPaths:ArrayList<Path> = ArrayList()
    private val townSet: HashSet<Town> = HashSet()

    fun init():Day_9 {
        for (l in allLines) {
            val split = l.split("=")
            val distance = split[1].trim()
            val towns = split[0].split("to")
            val town1 = townSet.get(towns[0].trim())
            val town2 = townSet.get(towns[1].trim())
            town1.addEdge(Edge(town2, distance.toInt()))
            town2.addEdge(Edge(town1, distance.toInt()))
        }
        for (it in townSet) {
            val path: LinkedHashSet<Town> = linkedSetOf(it)
            allPaths.addAll(findDistance(it, path, 0))
        }
        return this
    }

    override fun executeGoal_1() {
        allPaths.filter { it.path.size == townSet.size }.sortedBy { it.distance }.first().apply (::println)
    }

    override fun executeGoal_2() {
        allPaths.filter { it.path.size == townSet.size }.sortedBy { it.distance }.last().apply (::println)
    }

    private fun findDistance(town: Town, path: MutableSet<Town>, sum: Int) : ArrayList<Path> {
        val tried: MutableSet<Edge> = mutableSetOf()
        val paths = arrayListOf<Path>()
        for (edge in town.edges) {
            if(!path.contains(edge.town) && !tried.contains(edge)) {
                var tmpSum = sum + edge.distance
                tried.add(edge)
                val pathCopy = path.toMutableSet()
                pathCopy.add(edge.town)
                paths.addAll(findDistance(edge.town, pathCopy, tmpSum))
            }
        }
        paths.add(Path(path, sum))
        return paths
    }

    infix fun HashSet<Town>.get(name: String) : Town {
        var find = this.find { town -> town.name == name }
        if (find == null) {
            find = Town(name)
            this.add(find)
        }
        return find
    }

    data class Town(val name: String) {

        val edges: ArrayList<Edge> = ArrayList()

        fun addEdge(e: Edge) {
            edges.add(e)
        }

        override fun equals(other: Any?): Boolean {
            return other is Town && this.name == other.name
        }

        override fun hashCode(): Int {
            return this.name.hashCode()
        }

        override fun toString(): String {
            return "$name"
        }
    }

    data class Edge(val town: Town, val distance: Int) {
        override fun toString(): String {
            return "to ${town.name} $distance"
        }

        override fun equals(other: Any?): Boolean {
            return other is Edge && other.town == this.town
        }

        override fun hashCode(): Int {
            return this.town.hashCode()
        }
    }

    data class Path(val path:Set<Town>, val distance: Int) {
        override fun toString(): String {
            return "$path $distance"
        }
    }

}