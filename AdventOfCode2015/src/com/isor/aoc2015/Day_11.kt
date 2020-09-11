package com.isor.aoc2015

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year
import java.lang.StringBuilder

/**
 * --- Day 11: Corporate Policy ---
    Santa's previous password expired, and he needs help choosing a new one.

    To help him remember his new password after the old one expires, Santa has devised a method of coming up with a password based on the previous one. Corporate policy dictates that passwords must be exactly eight lowercase letters (for security reasons), so he finds his new password by incrementing his old password string repeatedly until it is valid.

    Incrementing is just like counting with numbers: xx, xy, xz, ya, yb, and so on. Increase the rightmost letter one step; if it was z, it wraps around to a, and repeat with the next letter to the left until one doesn't wrap around.

    Unfortunately for Santa, a new Security-Elf recently started, and he has imposed some additional password requirements:

    Passwords must include one increasing straight of at least three letters, like abc, bcd, cde, and so on, up to xyz. They cannot skip letters; abd doesn't count.
    Passwords may not contain the letters i, o, or l, as these letters can be mistaken for other characters and are therefore confusing.
    Passwords must contain at least two different, non-overlapping pairs of letters, like aa, bb, or zz.
    For example:

    hijklmmn meets the first requirement (because it contains the straight hij) but fails the second requirement requirement (because it contains i and l).
    abbceffg meets the third requirement (because it repeats bb and ff) but fails the first requirement.
    abbcegjk fails the third requirement, because it only has one double letter (bb).
    The next password after abcdefgh is abcdffaa.
    The next password after ghijklmn is ghjaabcc, because you eventually skip all the passwords that start with ghi..., since i is not allowed.
    Given Santa's current password (your puzzle input), what should his next password be?

    Your puzzle answer was vzbxxyzz.

    --- Part Two ---
    Santa's password expired again. What's the next one?

    Your puzzle answer was vzcaabcc.

    Both parts of this puzzle are complete! They provide two gold stars: **

    At this point, you should return to your Advent calendar and try another puzzle.

    Your puzzle input was vzbxkghb.
 */
fun main() {
    Day_11().executeGoals()
}

@Year(2015)
//@TestResources
class Day_11 : AOC_Runner() {

    override fun executeGoal_1() {
        allLines.forEach { l ->
            var pass = l
            val twoDifferendGroups = Regex("([a-z])\\1")
            val incrementingChars = Regex("(abc|bcd|cde|def|efg|fgh|ghi|hij|ijk|jkl|klm|lmn|mno|nop|opq|pqr|qrs|rst|stu|tuv|uvw|vwx|wxy|xyz)")
            val invalidchars = Regex("(i|o|l)")
            while (pass.contains(invalidchars) || !pass.contains(incrementingChars) || !pass.matchesTwoDifferentGroups(twoDifferendGroups)) {
                pass = incrementPassWord(pass)
            }
            println("$l -> $pass")
        }
    }

    override fun executeGoal_2() {
    }

    infix fun String.matchesTwoDifferentGroups(r:Regex):Boolean {
        val find = r.findAll(this)
        val mutableSetOf: MutableSet<String> = mutableSetOf<String>()
        find.forEach { r ->
            mutableSetOf.add(r.value)
        }
        return mutableSetOf.size>=2
    }

    fun incrementPassWord( oldPassword : String ) : String {
        var newPassword: StringBuilder = StringBuilder()
        var increment: Boolean = true
        for (i in oldPassword.length-1 downTo  0) {
            increment = incrementChar(oldPassword[i], increment, newPassword)
        }

        return newPassword.reverse().toString()
    }

    private fun incrementChar(char: Char, increment: Boolean, newPassword: StringBuilder) : Boolean {
        var newChar = char
        if (increment) {
            if( char == 'z' ) {
                newPassword.append('a'.toString())
                return true
            }
            newChar = char + 1
        }
        newPassword.append(newChar.toString())
        return false
    }

}