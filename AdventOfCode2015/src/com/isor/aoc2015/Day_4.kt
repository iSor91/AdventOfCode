package com.isor.aoc2015

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.Year
import java.math.BigInteger
import java.security.MessageDigest

/**
 * --- Day 4: The Ideal Stocking Stuffer ---
Santa needs help mining some AdventCoins (very similar to bitcoins) to use as gifts for all the economically forward-thinking little girls and boys.

To do this, he needs to find MD5 hashes which, in hexadecimal, start with at least five zeroes. The input to the MD5 hash is some secret key (your puzzle input, given below) followed by a number in decimal. To mine AdventCoins, you must find Santa the lowest positive number (no leading zeroes: 1, 2, 3, ...) that produces such a hash.

For example:

If your secret key is abcdef, the answer is 609043, because the MD5 hash of abcdef609043 starts with five zeroes (000001dbbfa...), and it is the lowest such number to do so.
If your secret key is pqrstuv, the lowest number it combines with to make an MD5 hash starting with five zeroes is 1048970; that is, the MD5 hash of pqrstuv1048970 looks like 000006136ef....
Your puzzle answer was 117946.

--- Part Two ---
Now find one that starts with six zeroes.

Your puzzle answer was 3938038.
 */

fun main() {
    Day_4().executeGoals()
}

@Year(2015)
//@TestResources
class Day_4 : AOC_Runner() {

    private val key : String = allLines[0]

    override fun executeGoal_1() {
        val md5 = MessageDigest.getInstance("MD5")
        println(md5.findHashThatStartsWith("00000"))
    }

    override fun executeGoal_2() {
        val md5 = MessageDigest.getInstance("MD5")
        println(md5.findHashThatStartsWith("000000"))
    }

    private infix fun MessageDigest.findHashThatStartsWith(prefix : String) : Int {
        println(key)
        var i = 0;
        while (!"$key${i++}".digest(this).startsWith(prefix));
        return i - 1
    }

    private infix fun String.digest(md5 : MessageDigest) : String {
        return BigInteger(1, md5.digest(this.toByteArray())).toString(16).padStart(32,'0')
    }

}