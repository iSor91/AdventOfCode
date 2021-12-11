package com.isor.aoc2018

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.Year
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
--- Day 4: Repose Record ---
You've sneaked into another supply closet - this time, it's across from the prototype suit manufacturing lab. You need to sneak inside and fix the issues with the suit, but there's a guard stationed outside the lab, so this is as close as you can safely get.

As you search the closet for anything that might help, you discover that you're not the first person to want to sneak in. Covering the walls, someone has spent an hour starting every midnight for the past few months secretly observing this guard post! They've been writing down the ID of the one guard on duty that night - the Elves seem to have decided that one guard was enough for the overnight shift - as well as when they fall asleep or wake up while at their post (your puzzle input).

For example, consider the following records, which have already been organized into chronological order:

[1518-11-01 00:00] Guard #10 begins shift
[1518-11-01 00:05] falls asleep
[1518-11-01 00:25] wakes up
[1518-11-01 00:30] falls asleep
[1518-11-01 00:55] wakes up
[1518-11-01 23:58] Guard #99 begins shift
[1518-11-02 00:40] falls asleep
[1518-11-02 00:50] wakes up
[1518-11-03 00:05] Guard #10 begins shift
[1518-11-03 00:24] falls asleep
[1518-11-03 00:29] wakes up
[1518-11-04 00:02] Guard #99 begins shift
[1518-11-04 00:36] falls asleep
[1518-11-04 00:46] wakes up
[1518-11-05 00:03] Guard #99 begins shift
[1518-11-05 00:45] falls asleep
[1518-11-05 00:55] wakes up
Timestamps are written using year-month-day hour:minute format. The guard falling asleep or waking up is always the one whose shift most recently started. Because all asleep/awake times are during the midnight hour (00:00 - 00:59), only the minute portion (00 - 59) is relevant for those events.

Visually, these records show that the guards are asleep at these times:

Date   ID   Minute
000000000011111111112222222222333333333344444444445555555555
012345678901234567890123456789012345678901234567890123456789
11-01  #10  .....####################.....#########################.....
11-02  #99  ........................................##########..........
11-03  #10  ........................#####...............................
11-04  #99  ....................................##########..............
11-05  #99  .............................................##########.....
The columns are Date, which shows the month-day portion of the relevant day; ID, which shows the guard on duty that day; and Minute, which shows the minutes during which the guard was asleep within the midnight hour. (The Minute column's header shows the minute's ten's digit in the first row and the one's digit in the second row.) Awake is shown as ., and asleep is shown as #.

Note that guards count as asleep on the minute they fall asleep, and they count as awake on the minute they wake up. For example, because Guard #10 wakes up at 00:25 on 1518-11-01, minute 25 is marked as awake.

If you can figure out the guard most likely to be asleep at a specific time, you might be able to trick that guard into working tonight so you can have the best chance of sneaking in. You have two strategies for choosing the best guard/minute combination.

Strategy 1: Find the guard that has the most minutes asleep. What minute does that guard spend asleep the most?

In the example above, Guard #10 spent the most minutes asleep, a total of 50 minutes (20+25+5), while Guard #99 only slept for a total of 30 minutes (10+10+10). Guard #10 was asleep most during minute 24 (on two days, whereas any other minute the guard was asleep was only seen on one day).

While this example listed the entries in chronological order, your entries are in the order you found them. You'll need to organize them before they can be analyzed.

What is the ID of the guard you chose multiplied by the minute you chose? (In the above example, the answer would be 10 * 24 = 240.)

Your puzzle answer was 60438.

--- Part Two ---
Strategy 2: Of all guards, which guard is most frequently asleep on the same minute?

In the example above, Guard #99 spent minute 45 asleep more than any other guard or minute - three times in total. (In all other cases, any guard spent any minute asleep at most twice.)

What is the ID of the guard you chose multiplied by the minute you chose? (In the above example, the answer would be 99 * 45 = 4455.)

Your puzzle answer was 47989.
 */
fun main() {
    Day_4().executeGoals()
}

operator fun  LocalDateTime.minus(toSubStract: LocalDateTime) : Int {
    if(this.year != toSubStract.year) return 61
    if(this.month != toSubStract.month) return 61
    if(this.dayOfMonth != toSubStract.dayOfMonth) return 61
    return this.minute - toSubStract.minute
}

val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm")

@Year(2018)
class Day_4 : AOC_Runner() {

    val guards: MutableMap<Int,Guard> = mutableMapOf()

    private val stateChanges : List<StateChange>

    init {
        val simpleStateChanges: MutableList<SimpleStateChange> = mutableListOf()
        this.allLines.forEach{
            line ->
               simpleStateChanges.add(line.process())
        }
        simpleStateChanges.sort()

        var currentGuard : Guard? = null
        var guardId : Int? = null
        stateChanges =  simpleStateChanges.map {
            s ->
                val newGuardId = s.message.newGuardId()
                if(newGuardId == null && guardId == null) {
                    throw RuntimeException("The first input line is not a guard starting.")
                } else if(newGuardId!=null){
                    guardId = newGuardId
                    currentGuard = guards[newGuardId]
                    if(currentGuard == null) {
                        guards[newGuardId] = Guard(newGuardId)
                    }
                }
                s.stateChange(guardId!!)
        }
        stateChanges.forEach{ s ->
            val currentGuard = guards[s.guardId]
            currentGuard!!.stateChanges.add(s)
        }
    }



    private fun SimpleStateChange.stateChange(guardId: Int) : StateChange{
        return StateChange(this.time, guardId, this.message.type())
    }

    private fun String.type() : GuardState {
        return if(this == "falls asleep") GuardState.DOWN else GuardState.UP
    }

    private fun String.newGuardId(): Int? {
        val idStart = this.indexOf("#")
        if(idStart!=-1) {
            return this.substring(idStart+1, this.indexOf(" ", idStart)).toInt()
        }
        return null
    }

    private fun String.process() : SimpleStateChange{
        val updated = this.replaceFirst(" ", "-")
        val split = updated.split(" ", limit = 2)
        var d = LocalDateTime.parse(split[0].substring(1,split[0].length-1), formatter)
        if(d.hour != 0) {
            val h = d.withHour(0)
            val m = h.withMinute(0)
            d = m.plusDays(1L)
        }
        return SimpleStateChange(d,split[1].trim())
    }

    override fun executeGoal_1() {
        val a = guards.entries.map { e -> e.value }
        Collections.sort(a) { o1, o2 ->  o1.downTime()-o2.downTime()}
        val maxG = guards.entries.maxByOrNull { e -> e.value.downTime() }!!.value
        println(maxG.id*maxG.array.indexOf(maxG.array.maxOrNull()!!))
    }

    override fun executeGoal_2() {
        val maxG = guards.entries.maxByOrNull { e -> e.value.maxMinute() }!!.value
        println("""$maxG ${maxG.maxMinute()} ${maxG.array.indexOf(maxG.maxMinute())}""")
        println(maxG.id * maxG.array.indexOf(maxG.maxMinute()))
    }


    class Guard(val id: Int) {
        val stateChanges : MutableList<StateChange> = mutableListOf()
        val array = IntArray(60){0}
        var downTime = -1

        override fun toString(): String {
            return """guard $id"""
        }

        fun maxMinute () : Int{
            return array.maxOrNull()!!
        }

        fun downTime(): Int {
            if(downTime!=-1) {
                return downTime
            }
            var currentState : GuardState
            for(i in 0 until stateChanges.size-1) {
                val startState = stateChanges[i]
                val endState = stateChanges[i+1]
                currentState = stateChanges[i].state
                if(currentState==GuardState.DOWN) {
                    val minDiff = endState.time - startState.time
                    var endM = 59
                    if(minDiff != 61) {
                        downTime += minDiff
                        endM = endState.time.minute-1
                    } else {
                        downTime += startState.time.minute - 59
                    }
                    for (j in startState.time.minute..endM) {
                        array[j]++
                    }
                }
            }

            return downTime
        }

    }

    enum class GuardState {UP, DOWN}

    data class SimpleStateChange(val time: LocalDateTime, val message:String) : Comparable<SimpleStateChange>{
        override fun compareTo(other: SimpleStateChange): Int {
            return this.time.compareTo(other.time)
        }

    }

    data class StateChange(val time: LocalDateTime, val guardId: Int, val state: GuardState) {
           override fun toString(): String {
            return """    ${time.format(formatter)} state: $state"""
        }
    }
}