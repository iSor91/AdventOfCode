package com.isor.aoc2021

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.Year
import java.lang.Long.max

/**
--- Day 21: Dirac Dice ---
There's not much to do as you slowly descend to the bottom of the ocean. The submarine computer challenges you to a nice game of Dirac Dice.

This game consists of a single die, two pawns, and a game board with a circular track containing ten spaces marked 1 through 10 clockwise. Each player's starting space is chosen randomly (your puzzle input). Player 1 goes first.

Players take turns moving. On each player's turn, the player rolls the die three times and adds up the results. Then, the player moves their pawn that many times forward around the track (that is, moving clockwise on spaces in order of increasing value, wrapping back around to 1 after 10). So, if a player is on space 7 and they roll 2, 2, and 1, they would move forward 5 times, to spaces 8, 9, 10, 1, and finally stopping on 2.

After each player moves, they increase their score by the value of the space their pawn stopped on. Players' scores start at 0. So, if the first player starts on space 7 and rolls a total of 5, they would stop on space 2 and add 2 to their score (for a total score of 2). The game immediately ends as a win for any player whose score reaches at least 1000.

Since the first game is a practice game, the submarine opens a compartment labeled deterministic dice and a 100-sided die falls out. This die always rolls 1 first, then 2, then 3, and so on up to 100, after which it starts over at 1 again. Play using this die.

For example, given these starting positions:

Player 1 starting position: 4
Player 2 starting position: 8
This is how the game would go:

Player 1 rolls 1+2+3 and moves to space 10 for a total score of 10.
Player 2 rolls 4+5+6 and moves to space 3 for a total score of 3.
Player 1 rolls 7+8+9 and moves to space 4 for a total score of 14.
Player 2 rolls 10+11+12 and moves to space 6 for a total score of 9.
Player 1 rolls 13+14+15 and moves to space 6 for a total score of 20.
Player 2 rolls 16+17+18 and moves to space 7 for a total score of 16.
Player 1 rolls 19+20+21 and moves to space 6 for a total score of 26.
Player 2 rolls 22+23+24 and moves to space 6 for a total score of 22.
...after many turns...

Player 2 rolls 82+83+84 and moves to space 6 for a total score of 742.
Player 1 rolls 85+86+87 and moves to space 4 for a total score of 990.
Player 2 rolls 88+89+90 and moves to space 3 for a total score of 745.
Player 1 rolls 91+92+93 and moves to space 10 for a final score, 1000.
Since player 1 has at least 1000 points, player 1 wins and the game ends. At this point, the losing player had 745 points and the die had been rolled a total of 993 times; 745 * 993 = 739785.

Play a practice game using the deterministic 100-sided die. The moment either player wins, what do you get if you multiply the score of the losing player by the number of times the die was rolled during the game?

Your puzzle answer was 900099.

--- Part Two ---
Now that you're warmed up, it's time to play the real game.

A second compartment opens, this time labeled Dirac dice. Out of it falls a single three-sided die.

As you experiment with the die, you feel a little strange. An informational brochure in the compartment explains that this is a quantum die: when you roll it, the universe splits into multiple copies, one copy for each possible outcome of the die. In this case, rolling the die always splits the universe into three copies: one where the outcome of the roll was 1, one where it was 2, and one where it was 3.

The game is played the same as before, although to prevent things from getting too far out of hand, the game now ends when either player's score reaches at least 21.

Using the same starting positions as in the example above, player 1 wins in 444356092776315 universes, while player 2 merely wins in 341960390180808 universes.

Using your given starting positions, determine every possible outcome. Find the player that wins in more universes; in how many universes does that player win?

Your puzzle answer was 306719685234774.
 */

fun main() {
    Day21().executeGoals()
}

@Year(2021)
//@TestResources
class Day21: AOC_Runner() {

    companion object {
        const val WIN_CONDITION_1 = 1000
        const val WIN_CONDITION_2 = 21
    }

    val tablePositions = IntArray(10){if(it == 0) 10 else it}
    val playerRegex = "Player (?<id>\\d+) starting position: (?<start>\\d+)".toRegex()
    val players = allLines.map {
        val find = playerRegex.find(it)

        val id = find!!.groupValues[1].toInt()
        val start = find!!.groupValues[2].toInt()
        Player(id, start, start)
    }
    val playerCount = players.size

    val deterministicDie = IntArray(100){if(it == 0) 100 else it}
    val threeSidedDieRollSums =
        (1..3).map { first ->
            (1..3).map { second ->
                (1..3).map { third ->
                    first + second + third
                }
            }.flatten()
        }.flatten()
    val rollSumCount = threeSidedDieRollSums.groupingBy { it }.eachCount()
    val threeSidedDie = rollSumCount.keys

    override fun executeGoal_1() {
        val playerCopies = players.map { Player(it) }

        var dieRolls = 0
        while (!playerCopies.any { it.score >= WIN_CONDITION_1 }) {
            val dieRollSum =
                deterministicDie[(++dieRolls) % 100] + deterministicDie[(++dieRolls) % 100] + deterministicDie[(++dieRolls) % 100]
            playerRound(playerCopies[(dieRolls/3 - 1)%playerCount], dieRollSum)
        }

        playerCopies.forEach { println(it) }
        println(dieRolls * playerCopies.minByOrNull { it.score }!!.score)
    }

    val wonGames = LongArray(playerCount){0L}

    override fun executeGoal_2() {
        //list representing each active games with their weight as how many are in the same state
        var games = listOf<Pair<Long, List<Player>>>(Pair(1, players.map { Player(it) }))
        var round = 0
        var maxSimultanGames = 0L
        while (games.isNotEmpty()) {
            games = playQuantumRound(games, round)
            games = games.filter { it.second.none { p -> p.score >= 21 } }
            println(games.size)
            maxSimultanGames = max(games.size.toLong(), maxSimultanGames)
            round++
        }
        println(wonGames.joinToString(" vs "))
        println(maxSimultanGames)
    }

    private fun playQuantumRound(games: List<Pair<Long, List<Player>>>, round: Int): MutableList<Pair<Long, List<Player>>> {
        val newGames = mutableListOf<Pair<Long, List<Player>>>()
        games.forEach {
            threeSidedDie.forEach { roll ->
                val copiedPlayers = it.second.map { Player(it) }
                val currentPlayer = copiedPlayers[round % playerCount]
                playerRound(currentPlayer, roll)
                val weight = it.first * rollSumCount[roll]!!
                if (currentPlayer.score >= WIN_CONDITION_2) {
                    wonGames[round % playerCount] += weight
                } else {
                    newGames.add(Pair(weight, copiedPlayers))
                }
            }
        }
        return newGames
    }

    private fun playerRound(player: Player, dieRollSum: Int) {
        player.pos = tablePositions[(player.pos + dieRollSum) % 10]
        player.score += player.pos
    }

    data class Player(val id: Int, val initialPos: Int, var pos: Int, var score : Int = 0)
    {
        override fun toString(): String {
            return "$id - $pos - $score"
        }

        constructor(other: Player): this(other.id,other.initialPos, other.pos, other.score)
    }
}