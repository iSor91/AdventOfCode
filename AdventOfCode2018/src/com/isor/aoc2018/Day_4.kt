package com.isor.aoc2018

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

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
        val maxG = guards.entries.maxBy { e -> e.value.downTime() }!!.value
        println(maxG.id*maxG.array.indexOf(maxG.array.max()!!))
    }

    override fun executeGoal_2() {
    }


    class Guard(val id: Int) {
        val stateChanges : MutableList<StateChange> = mutableListOf()
        val array = IntArray(60){0}
        var downTime = -1

        override fun toString(): String {
            return """guard $id"""
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