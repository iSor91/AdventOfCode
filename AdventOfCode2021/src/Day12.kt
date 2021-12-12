package com.isor.aoc2021

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year

/**
--- Day 12: Passage Pathing ---
With your submarine's subterranean subsystems subsisting suboptimally, the only way you're getting out of this cave anytime soon is by finding a path yourself. Not just a path - the only way to know if you've found the best path is to find all of them.

Fortunately, the sensors are still mostly working, and so you build a rough map of the remaining caves (your puzzle input). For example:

start-A
start-b
A-c
A-b
b-d
A-end
b-end
This is a list of how all of the caves are connected. You start in the cave named start, and your destination is the cave named end. An entry like b-d means that cave b is connected to cave d - that is, you can move between them.

So, the above cave system looks roughly like this:

start
/   \
c--A-----b--d
\   /
end
Your goal is to find the number of distinct paths that start at start, end at end, and don't visit small caves more than once. There are two types of caves: big caves (written in uppercase, like A) and small caves (written in lowercase, like b). It would be a waste of time to visit any small cave more than once, but big caves are large enough that it might be worth visiting them multiple times. So, all paths you find should visit small caves at most once, and can visit big caves any number of times.

Given these rules, there are 10 paths through this example cave system:

start,A,b,A,c,A,end
start,A,b,A,end
start,A,b,end
start,A,c,A,b,A,end
start,A,c,A,b,end
start,A,c,A,end
start,A,end
start,b,A,c,A,end
start,b,A,end
start,b,end
(Each line in the above list corresponds to a single path; the caves visited by that path are listed in the order they are visited and separated by commas.)

Note that in this cave system, cave d is never visited by any path: to do so, cave b would need to be visited twice (once on the way to cave d and a second time when returning from cave d), and since cave b is small, this is not allowed.

Here is a slightly larger example:

dc-end
HN-start
start-kj
dc-start
dc-HN
LN-dc
HN-end
kj-sa
kj-HN
kj-dc
The 19 paths through it are as follows:

start,HN,dc,HN,end
start,HN,dc,HN,kj,HN,end
start,HN,dc,end
start,HN,dc,kj,HN,end
start,HN,end
start,HN,kj,HN,dc,HN,end
start,HN,kj,HN,dc,end
start,HN,kj,HN,end
start,HN,kj,dc,HN,end
start,HN,kj,dc,end
start,dc,HN,end
start,dc,HN,kj,HN,end
start,dc,end
start,dc,kj,HN,end
start,kj,HN,dc,HN,end
start,kj,HN,dc,end
start,kj,HN,end
start,kj,dc,HN,end
start,kj,dc,end
Finally, this even larger example has 226 paths through it:

fs-end
he-DX
fs-he
start-DX
pj-DX
end-zg
zg-sl
zg-pj
pj-he
RW-he
fs-DX
pj-RW
zg-RW
start-pj
he-WI
zg-he
pj-fs
start-RW
How many paths through this cave system are there that visit small caves at most once?

Your puzzle answer was 3708.

--- Part Two ---
After reviewing the available paths, you realize you might have time to visit a single small cave twice. Specifically, big caves can be visited any number of times, a single small cave can be visited at most twice, and the remaining small caves can be visited at most once. However, the caves named start and end can only be visited exactly once each: once you leave the start cave, you may not return to it, and once you reach the end cave, the path must end immediately.

Now, the 36 possible paths through the first example above are:

start,A,b,A,b,A,c,A,end
start,A,b,A,b,A,end
start,A,b,A,b,end
start,A,b,A,c,A,b,A,end
start,A,b,A,c,A,b,end
start,A,b,A,c,A,c,A,end
start,A,b,A,c,A,end
start,A,b,A,end
start,A,b,d,b,A,c,A,end
start,A,b,d,b,A,end
start,A,b,d,b,end
start,A,b,end
start,A,c,A,b,A,b,A,end
start,A,c,A,b,A,b,end
start,A,c,A,b,A,c,A,end
start,A,c,A,b,A,end
start,A,c,A,b,d,b,A,end
start,A,c,A,b,d,b,end
start,A,c,A,b,end
start,A,c,A,c,A,b,A,end
start,A,c,A,c,A,b,end
start,A,c,A,c,A,end
start,A,c,A,end
start,A,end
start,b,A,b,A,c,A,end
start,b,A,b,A,end
start,b,A,b,end
start,b,A,c,A,b,A,end
start,b,A,c,A,b,end
start,b,A,c,A,c,A,end
start,b,A,c,A,end
start,b,A,end
start,b,d,b,A,c,A,end
start,b,d,b,A,end
start,b,d,b,end
start,b,end
The slightly larger example above now has 103 paths through it, and the even larger example now has 3509 paths through it.

Given these new rules, how many paths through this cave system are there?

Your puzzle answer was 93858.
 */

fun main() {
    Day12().executeGoals()
}

@TestResources(2)
@Year(2021)
class Day12: AOC_Runner() {

    data class Cave(val name: String, val connections: MutableSet<Cave>, val isSmall: Boolean) {
        override fun equals(other: Any?): Boolean = other is Cave && this.name == other.name
        override fun hashCode(): Int = this.name.hashCode()
        override fun toString(): String {
            return "$name - $isSmall"
        }
    }

    private val caveMap: MutableMap<String, Cave> = mutableMapOf()

    init {
        allLines.forEach {
            val twoCaves = it.split(Regex("-"))
            val cave1 =
                caveMap.getOrPut(twoCaves[0]) { Cave(twoCaves[0], mutableSetOf(), twoCaves[0].matches(Regex("[a-z]+"))) }
            val cave2 =
                caveMap.getOrPut(twoCaves[1]) { Cave(twoCaves[1], mutableSetOf(), twoCaves[1].matches(Regex("[a-z]+"))) }
            cave1.connections.add(cave2)
            cave2.connections.add(cave1)
        }
    }

    override fun executeGoal_1() {
        findPaths(false)
    }

    override fun executeGoal_2() {
        findPaths(true)
    }

    private fun findPaths(allowedVisitTwice: Boolean) {
        val startCave = caveMap["start"]!!
        val allPaths = mutableListOf<List<Cave>>()

        startCave.connections.forEach {
            val pathStart = mutableListOf(startCave, it)
            if(allowedVisitTwice)
                allPaths.addAll(visitCaves2(it, pathStart, false))
            else
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

    private fun visitCaves2(startCave: Cave, path: MutableList<Cave>, smallCaveVisited: Boolean): List<List<Cave>>{
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

}

