package com.isor.aoc2015

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year

/**
 * --- Day 14: Reindeer Olympics ---
This year is the Reindeer Olympics! Reindeer can fly at high speeds, but must rest occasionally to recover their energy. Santa would like to know which of his reindeer is fastest, and so he has them race.

Reindeer can only either be flying (always at their top speed) or resting (not moving at all), and always spend whole seconds in either state.

For example, suppose you have the following Reindeer:

Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.
Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.
After one second, Comet has gone 14 km, while Dancer has gone 16 km. After ten seconds, Comet has gone 140 km, while Dancer has gone 160 km. On the eleventh second, Comet begins resting (staying at 140 km), and Dancer continues on for a total distance of 176 km. On the 12th second, both reindeer are resting. They continue to rest until the 138th second, when Comet flies for another ten seconds. On the 174th second, Dancer flies for another 11 seconds.

In this example, after the 1000th second, both reindeer are resting, and Comet is in the lead at 1120 km (poor Dancer has only gotten 1056 km by that point). So, in this situation, Comet would win (if the race ended at 1000 seconds).

Given the descriptions of each reindeer (in your puzzle input), after exactly 2503 seconds, what distance has the winning reindeer traveled?

Your puzzle answer was 2640.

The first half of this puzzle is complete! It provides one gold star: *

--- Part Two ---
Seeing how reindeer move in bursts, Santa decides he's not pleased with the old scoring system.

Instead, at the end of each second, he awards one point to the reindeer currently in the lead. (If there are multiple reindeer tied for the lead, they each get one point.) He keeps the traditional 2503 second time limit, of course, as doing otherwise would be entirely ridiculous.

Given the example reindeer from above, after the first second, Dancer is in the lead and gets one point. He stays in the lead until several seconds into Comet's second burst: after the 140th second, Comet pulls into the lead and gets his first point. Of course, since Dancer had been in the lead for the 139 seconds before that, he has accumulated 139 points by the 140th second.

After the 1000th second, Dancer has accumulated 689 points, while poor Comet, our old champion, only has 312. So, with the new scoring system, Dancer would win (if the race ended at 1000 seconds).

Again given the descriptions of each reindeer (in your puzzle input), after exactly 2503 seconds, how many points does the winning reindeer have?

Your puzzle answer was 1102.

Both parts of this puzzle are complete! They provide two gold stars: **
 */

fun main() {
    Day_14().executeGoals()
}

//@TestResources
@Year(2015)
class Day_14 : AOC_Runner() {

    private val fullTime: Int = 2503
    private val reindeers = mutableListOf<Reindeer>()
    private val regex: Regex = Regex("([A-Z][a-z]+)(?: can fly )(\\d+)(?: km/s for )(\\d+)(?: seconds, but then must rest for )(\\d+)(?: seconds.)")

    init {
        allLines.forEach { find ->
            val findAll = regex.findAll(find)
            findAll.forEach { result ->
                this.reindeers.add(Reindeer(result.groups[1]!!.value, result.groups[2]!!.value.toInt(), result.groups[3]!!.value.toInt(), result.groups[4]!!.value.toInt()))
            }
        }
    }

    override fun executeGoal_1() {
        val reindeerDistances = mutableMapOf<String, Int>()
        reindeers.forEach {
            val fullCycles = fullTime / it.cycleTime()
            var remainingSeconds = fullTime % it.cycleTime()
            if(remainingSeconds > it.flyTime) remainingSeconds = it.flyTime
            reindeerDistances[it.name]= fullCycles * it.cycleDistance() + remainingSeconds * it.speed
        }
        printMap(reindeerDistances)
    }

    override fun executeGoal_2() {
        val reindeerDistances = mutableMapOf<String, Int>()
        val reindeerPoints = mutableMapOf<String, Int>()
        reindeers.forEach { reindeerDistances.putIfAbsent(it.name, 0) }
        reindeers.forEach { reindeerPoints.putIfAbsent(it.name, 0) }

        for (i:Int in 0..fullTime-1) {
            var debug: Boolean = false
            reindeers.forEach {
                if(i % it.cycleTime() < it.flyTime) {
                    reindeerDistances[it.name] = reindeerDistances[it.name]!! + it.speed
                    debug = true
                }
            }

            val leadDistance = reindeerDistances.values.maxOrNull()
            val leadReindeers = reindeerDistances.filter { it.value == leadDistance }
            leadReindeers.forEach{
                reindeerPoints[it.key] = reindeerPoints[it.key]!! + 1
            }
            if(debug) {
                println("Distances:")
                printMap(reindeerDistances)
                println("Lead: $leadDistance")
                printMap(reindeerPoints)
            }
        }

        printMap(reindeerPoints)

    }

    private fun printMap(reindeerMap: MutableMap<String, Int>) {
        println()
        val sortedList = reindeerMap.entries.sortedBy { it.value }
        sortedList.forEach {
            println("${it.key} - ${it.value}")
        }
    }

    data class Reindeer(val name: String, val speed: Int, val flyTime: Int, val restTime: Int) {

        fun cycleDistance(): Int {
            return speed * flyTime
        }

        fun cycleTime(): Int {
            return flyTime + restTime
        }

    }

}