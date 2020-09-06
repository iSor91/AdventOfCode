package com.isor.aoc2015

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year
import java.lang.StringBuilder

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