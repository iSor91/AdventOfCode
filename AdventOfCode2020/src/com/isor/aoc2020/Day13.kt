package com.isor.aoc2020

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year

fun main() {
    Day13().executeGoals()
}

//@TestResources
@Year(2020)
class Day13: AOC_Runner() {


    val busIdRegex = Regex("(\\d+),?")
    val earliestDepartureDate = allLines[0].toInt()

    override fun executeGoal_1() {
        val findAll = busIdRegex.findAll(allLines[1])
        val busIds: MutableList<Int> = mutableListOf()
        findAll.forEach { busIds.add(it.groups[1]!!.value.toInt())}

        val minWaitingTimes = mutableListOf<Int>()
        busIds.forEach {minWaitingTimes.add(it - earliestDepartureDate % it) }
        val element = minWaitingTimes.minOrNull()
        val busId = busIds[minWaitingTimes.indexOf(element)]
        println("$busId * $element = ${busId * element!!}")
    }

    override fun executeGoal_2() {
        allLines.subList(1, allLines.size).forEach { calculate(it) }
    }

    private fun calculate( line: String ) {
        val buses = mutableListOf<Pair<Long, Long>>()
        val allBuses = line.split(",")
        allBuses.forEachIndexed { i, bus -> if (bus.matches(Regex("\\d+"))) buses.add(bus.toLong() to i.toLong()) }
        buses.forEach { println(it) }

        val firstBus = buses[0]
        buses.removeAt(0)
        val offsets = buses.map { it.first % it.second }
        val list = mutableListOf(firstBus.first)
        list.addAll(offsets)
        list.removeAll { it == 0L }
        var step = firstBus.first//lcm(list)
        println(step)
        val minimum = 100000000000000
        var current = minimum - (minimum % step)

        buses.forEach { (bus, offset) ->
            while((current + offset) % bus != 0L) {
                current += step
            }
            step *= bus
        }

        println(current)


//        searchLoop@ while (true) {
//            if (!buses.all { (current + it.second) % it.first == 0L  }) {
//    //                println("${current.toDouble()/Long.MAX_VALUE}")
////                    println(current)
//                current += step
//                println(current)
//                continue@searchLoop
//            }
//            println(current)
//            break@searchLoop
//        }
        println()
    }

}