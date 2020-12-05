package com.isor.aoc2015

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.Year

fun main() {
    Day_13().executeGoals()
}

@Year(2015)
//@TestResources
class Day_13 : AOC_Runner() {

    val regex = "^(?:([A-Z][a-z]+) (?:would) (gain|lose) ([0-9]+) (?:happiness units by sitting next to) ([A-Z][a-z]+).)"

    override fun executeGoal_1() {
        val users: MutableMap<String, User> = mutableMapOf()
        val regex1 = Regex(regex)
        for (line in allLines) {
            val find = regex1.find(line)
            val groupValues = find!!.groupValues
            val firstUserName = groupValues[1]
            val secondUserName = groupValues[4]
            val amount = groupValues[3].toInt() * if("gain" == (groupValues[2])) 1 else -1
            val firstUser = users.getOrPut(firstUserName, { User(firstUserName) })
            val secondUser = users.getOrPut(secondUserName, { User(secondUserName) })

            firstUser.happiness[secondUser] = amount
        }
        val root = users.values.first()

        users.values.forEach {current ->
            current.happiness.keys.forEach {
                current.happiness[it] = current.happiness[it]!! + it.happiness[current]!!
            }
        }

        println("end")
    }

    override fun executeGoal_2() {
    }

    private fun findSumHappiness(user: User, order: MutableSet<User>, sum: Int) : ArrayList<SitOrder> {
        val tried: MutableSet<User> = mutableSetOf()
        val paths = arrayListOf<SitOrder>()
        for (happiness in user.happiness) {
            val current = happiness.key
            if(!order.contains(current) && !tried.contains(current)) {
                var tmpSum = sum + happiness.value
                tried.add(current)
                val pathCopy = order.toMutableSet()
                pathCopy.add(current)
                paths.addAll(findSumHappiness(current, pathCopy, tmpSum))
            }
        }
        paths.add(SitOrder(order, sum))
        return paths
    }


    data class User (val name:String) {

        val happiness : MutableMap<User, Int> = mutableMapOf()

        override fun equals(other: Any?): Boolean {
            return other is User && other.name == (this.name)
        }
    }

    data class SitOrder(val path:Set<User>, val distance: Int) {
        override fun toString(): String {
            return "$path $distance"
        }
    }
}