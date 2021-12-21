package com.isor.aoc2021

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year

fun main() {
    Day21().executeGoals()
}

@Year(2021)
//@TestResources
class Day21: AOC_Runner() {

    val playerRegex = "Player (?<id>\\d+) starting position: (?<start>\\d+)".toRegex()

    data class Player(val id: Int, val initialPos: Int, var pos: Int, var score : Int = 0, var next: Player? = null)
    {
        override fun toString(): String {
            return "$id - $pos - $score"
        }

        constructor(other: Player): this(other.id,other.initialPos, other.pos, other.score)
    }

    val players = allLines.map {
        val find = playerRegex.find(it)

        val id = find!!.groupValues[1].toInt()
        val start = find!!.groupValues[2].toInt()
        Player(id, start, start)
    }

    override fun executeGoal_1() {
        val playerCopies = copyPlayers(players)
        val positions = IntArray(10){if(it == 0) 10 else it}
        val deterministicDie = IntArray(100){if(it == 0) 100 else it}

        var dieRolls = 0
        var currentPlayer = playerCopies.first()
        while (!playerCopies.any { it.score >= 1000 }) {
            val dieRollSum = deterministicDie[(dieRolls+1)%100] + deterministicDie[(dieRolls+2)%100] + deterministicDie[(dieRolls+3)%100]
            dieRolls+=3
            currentPlayer.pos = positions[(currentPlayer.pos + dieRollSum)%10]
            currentPlayer.score += currentPlayer.pos
            currentPlayer = currentPlayer.next!!
        }

        playerCopies.forEach{ println(it) }

        println(dieRolls * playerCopies.minByOrNull { it.score }!!.score)
    }

    private fun copyPlayers(players: List<Player>): List<Player> {
        val playerCopies = players.map { Player(it) }
        playerCopies.forEachIndexed { i, p ->
            if (i == playerCopies.size - 1) p.next = playerCopies.first() else p.next = playerCopies[i + 1]
        }
        return playerCopies
    }

    var firstWonGames = 0L
    var secondWonGames = 0L

    override fun executeGoal_2() {
        val rolls = mutableListOf<Int>()
        (1..3).forEach { first ->
            (1..3).forEach { second ->
                (1..3).forEach { third ->
                    rolls.add(first + second + third)
                }
            }
        }
        val rollSumCount = rolls.groupingBy { it }.eachCount()

        val positions = IntArray(10){if(it == 0) 10 else it}
        val die = rollSumCount.keys


        var games = listOf<Pair<Long, List<Player>>>(Pair(1, copyPlayers(players)))


        var round = 0
        while (games.isNotEmpty()) {
            games = oneRound(games, die, round, positions, rollSumCount)
            games = games.filter { it.second.none { p -> p.score >= 21 } }
            println(games.size)
            println("$firstWonGames vs $secondWonGames")
            println()
            round++
        }


    }

    private fun oneRound(
        games: List<Pair<Long, List<Player>>>,
        die: Set<Int>,
        round: Int,
        positions: IntArray,
        rollSumCount: Map<Int, Int>
    ): MutableList<Pair<Long, List<Player>>> {
        val newGames = mutableListOf<Pair<Long, List<Player>>>()
        games.forEach {
            die.forEach { roll ->
                val copiedPlayers = copyPlayers(it.second)
                val currentPlayer = copiedPlayers[round % 2]
                currentPlayer.pos = positions[(currentPlayer.pos + roll) % 10]
                currentPlayer.score += currentPlayer.pos
                if(currentPlayer.score >= 21) {
                    when (round%2) {
                        0 -> firstWonGames += (it.first * rollSumCount[roll]!!)
                        else -> secondWonGames += (it.first * rollSumCount[roll]!!)
                    }
                } else {
                    newGames.add(Pair(it.first * rollSumCount[roll]!!, copiedPlayers))
                }
            }
        }
        return newGames
    }
}