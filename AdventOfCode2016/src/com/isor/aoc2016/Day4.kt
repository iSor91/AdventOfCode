package com.isor.aoc2016

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.Year

/**
--- Day 4: Security Through Obscurity ---
Finally, you come across an information kiosk with a list of rooms. Of course, the list is encrypted and full of decoy data, but the instructions to decode the list are barely hidden nearby. Better remove the decoy data first.

Each room consists of an encrypted name (lowercase letters separated by dashes) followed by a dash, a sector ID, and a checksum in square brackets.

A room is real (not a decoy) if the checksum is the five most common letters in the encrypted name, in order, with ties broken by alphabetization. For example:

aaaaa-bbb-z-y-x-123[abxyz] is a real room because the most common letters are a (5), b (3), and then a tie between x, y, and z, which are listed alphabetically.
a-b-c-d-e-f-g-h-987[abcde] is a real room because although the letters are all tied (1 of each), the first five are listed alphabetically.
not-a-real-room-404[oarel] is a real room.
totally-real-room-200[decoy] is not.
Of the real rooms from the list above, the sum of their sector IDs is 1514.

What is the sum of the sector IDs of the real rooms?

Your puzzle answer was 158835.

--- Part Two ---
With all the decoy data out of the way, it's time to decrypt this list and get moving.

The room names are encrypted by a state-of-the-art shift cipher, which is nearly unbreakable without the right software. However, the information kiosk designers at Easter Bunny HQ were not expecting to deal with a master cryptographer like yourself.

To decrypt a room name, rotate each letter forward through the alphabet a number of times equal to the room's sector ID. A becomes B, B becomes C, Z becomes A, and so on. Dashes become spaces.

For example, the real name for qzmt-zixmtkozy-ivhz-343 is very encrypted name.

What is the sector ID of the room where North Pole objects are stored?

Your puzzle answer was 993.
 */
fun main() {
    Day4().executeGoals()
}

//@TestResources
@Year(2016)
class Day4: AOC_Runner() {

    data class Room(val name: String, val sectorId: Int, val checksum: String)

    private val regex = "^([[a-z]+-]+)(\\d+)\\[([a-z]+)\\]$".toRegex()
    private val rooms = allLines.map {
        val regexResult = regex.find(it)
        val groupValues = regexResult!!.groupValues
        Room(groupValues[1], groupValues[2].toInt(), groupValues[3])
    }

    lateinit var realRooms: List<Room>

    override fun executeGoal_1() {
        realRooms = rooms.filter { it ->
            val result = it.name.chunked(1)
                .groupingBy { c -> c }.eachCount()
                .entries.filter { c -> c.key != "-" }
                .sortedWith(compareBy({e->-e.value}, {e->e.key})).take(5).joinToString("") { it.key }
            it.checksum == result
        }

        println(realRooms.sumOf { it.sectorId })
    }

    override fun executeGoal_2() {
        val charMap = ('a'..'z').mapIndexed{ i, a -> i to a.toString() }.toMap()
        val indexMap = ('a'..'z').mapIndexed{ i, a -> a.toString() to i }.toMap()

        val deciphered = realRooms.map {
            it.name.replace("-", " ")
                .chunked(1).joinToString("") { c ->
                    if (c == " ") c
                    else charMap[(indexMap[c]!! + it.sectorId) % indexMap.size].toString()
                } + "- ${it.sectorId}"
        }

        println(deciphered.sorted().joinToString("\n"))

    }
}