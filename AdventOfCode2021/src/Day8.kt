package com.isor.aoc2021

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year

/**
--- Day 8: Seven Segment Search ---
You barely reach the safety of the cave when the whale smashes into the cave mouth, collapsing it. Sensors indicate another exit to this cave at a much greater depth, so you have no choice but to press on.

As your submarine slowly makes its way through the cave system, you notice that the four-digit seven-segment displays in your submarine are malfunctioning; they must have been damaged during the escape. You'll be in a lot of trouble without them, so you'd better figure out what's wrong.

Each digit of a seven-segment display is rendered by turning on or off any of seven segments named a through g:

0:      1:      2:      3:      4:
aaaa    ....    aaaa    aaaa    ....
b    c  .    c  .    c  .    c  b    c
b    c  .    c  .    c  .    c  b    c
....    ....    dddd    dddd    dddd
e    f  .    f  e    .  .    f  .    f
e    f  .    f  e    .  .    f  .    f
gggg    ....    gggg    gggg    ....

5:      6:      7:      8:      9:
aaaa    aaaa    aaaa    aaaa    aaaa
b    .  b    .  .    c  b    c  b    c
b    .  b    .  .    c  b    c  b    c
dddd    dddd    ....    dddd    dddd
.    f  e    f  .    f  e    f  .    f
.    f  e    f  .    f  e    f  .    f
gggg    gggg    ....    gggg    gggg

So, to render a 1, only segments c and f would be turned on; the rest would be off. To render a 7, only segments a, c, and f would be turned on.

The problem is that the signals which control the segments have been mixed up on each display. The submarine is still trying to display numbers by producing output on signal wires a through g, but those wires are connected to segments randomly. Worse, the wire/segment connections are mixed up separately for each four-digit display! (All of the digits within a display use the same connections, though.)

So, you might know that only signal wires b and g are turned on, but that doesn't mean segments b and g are turned on: the only digit that uses two segments is 1, so it must mean segments c and f are meant to be on. With just that information, you still can't tell which wire (b/g) goes to which segment (c/f). For that, you'll need to collect more information.

For each display, you watch the changing signals for a while, make a note of all ten unique signal patterns you see, and then write down a single four digit output value (your puzzle input). Using the signal patterns, you should be able to work out which pattern corresponds to which digit.

For example, here is what you might see in a single entry in your notes:

acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab |
cdfeb fcadb cdfeb cdbaf
(The entry is wrapped here to two lines so it fits; in your notes, it will all be on a single line.)

Each entry consists of ten unique signal patterns, a | delimiter, and finally the four digit output value. Within an entry, the same wire/segment connections are used (but you don't know what the connections actually are). The unique signal patterns correspond to the ten different ways the submarine tries to render a digit using the current wire/segment connections. Because 7 is the only digit that uses three segments, dab in the above example means that to render a 7, signal lines d, a, and b are on. Because 4 is the only digit that uses four segments, eafb means that to render a 4, signal lines e, a, f, and b are on.

Using this information, you should be able to work out which combination of signal wires corresponds to each of the ten digits. Then, you can decode the four digit output value. Unfortunately, in the above example, all of the digits in the output value (cdfeb fcadb cdfeb cdbaf) use five segments and are more difficult to deduce.

For now, focus on the easy digits. Consider this larger example:

be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb |
fdgacbe cefdb cefbgd gcbe
edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec |
fcgedb cgb dgebacf gc
fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef |
cg cg fdcagb cbg
fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega |
efabcd cedba gadfec cb
aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga |
gecf egdcabf bgf bfgea
fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf |
gebdcfa ecba ca fadegcb
dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf |
cefg dcbef fcge gbcadfe
bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd |
ed bcgafe cdgba cbgef
egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg |
gbdfcae bgc cg cgb
gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc |
fgae cfgab fg bagce
Because the digits 1, 4, 7, and 8 each use a unique number of segments, you should be able to tell which combinations of signals correspond to those digits. Counting only digits in the output values (the part after | on each line), in the above example, there are 26 instances of digits that use a unique number of segments (highlighted above).

In the output values, how many times do digits 1, 4, 7, or 8 appear?

Your puzzle answer was 504.

--- Part Two ---
Through a little deduction, you should now be able to determine the remaining digits. Consider again the first example above:

acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab |
cdfeb fcadb cdfeb cdbaf
After some careful analysis, the mapping between signal wires and segments only make sense in the following configuration:

dddd
e    a
e    a
ffff
g    b
g    b
cccc
So, the unique signal patterns would correspond to the following digits:

acedgfb: 8
cdfbe: 5
gcdfa: 2
fbcad: 3
dab: 7
cefabd: 9
cdfgeb: 6
eafb: 4
cagedb: 0
ab: 1
Then, the four digits of the output value can be decoded:

cdfeb: 5
fcadb: 3
cdfeb: 5
cdbaf: 3
Therefore, the output value for this entry is 5353.

Following this same process for each entry in the second, larger example above, the output value of each entry can be determined:

fdgacbe cefdb cefbgd gcbe: 8394
fcgedb cgb dgebacf gc: 9781
cg cg fdcagb cbg: 1197
efabcd cedba gadfec cb: 9361
gecf egdcabf bgf bfgea: 4873
gebdcfa ecba ca fadegcb: 8418
cefg dcbef fcge gbcadfe: 4548
ed bcgafe cdgba cbgef: 1625
gbdfcae bgc cg cgb: 8717
fgae cfgab fg bagce: 4315
Adding all of the output values in this larger example produces 61229.

For each entry, determine all of the wire/segment connections and decode the four-digit output values. What do you get if you add up all of the output values?

Your puzzle answer was 1073431.
 */

fun main() {
    Day8().executeGoals()
}

@Year(2021)
@TestResources
class Day8 : AOC_Runner() {

    data class Display(val tries: List<Set<String>>, val digits: List<Set<String>>)

    private val displays: List<Display> = allLines.map {
        val digits = "\\w+".toRegex().findAll(it).toList().map { f -> f.value.chunked(1).toSet() }
        Display(digits.take(10), digits.takeLast(4))
    }

    override fun executeGoal_1() {
        println(displays.sumOf { it.digits.count { d -> (2..4).contains(d.size) or (d.size == 7) } })
    }

    override fun executeGoal_2() {
        println(displays.sumOf {
            it.digits.map { s -> deductValue(s, calculateWiring(it)) }.joinToString("").toInt()
        })
    }

    /**
     * Calculates the possible wiring of the segments, and returns a map.
     * The map contains the possible wire representation of segments indexes
     * according to the below 7 segment display as keys. The values are List<String> e.g. {"a","b","d"}.
     *
     *  0000
     * 1    2
     * 1    2
     *  3333
     * 4    5
     * 4    5
     *  6666
     *
     *  The results will be such, that the
     *  - 2/5 have the same value
     *  - 4/6 have the same value
     *  - 1/3 have the same value
     *  - 0 has one value
     */
    private fun calculateWiring(display: Display): Map<Int, Set<String>> {

        val digits = mutableMapOf<Int, Set<String>>()
        val oneSegments = getSegments(display.tries, 2)
        digits[2] = oneSegments
        val sevenSegments = getSegments(display.tries, 3)
        digits[0] = sevenSegments - oneSegments
        val fourSegments = getSegments(display.tries, 4)
        digits[1] = fourSegments - oneSegments
        val eightSegments = getSegments(display.tries, 7)
        digits[4] = eightSegments - fourSegments - sevenSegments

        return digits
    }

    private fun getSegments(allDigits: List<Set<String>>, length: Int) =
        allDigits.filter { it.size == length }.flatten().toSet()

    /**
     * Deducts the intended value of the String according these rules:
     * according length 1,4,7,8 can ben determined
     *
     * in other cases more elaborate deduction is required, using the calculateWiring's result
     *
     *  0000
     * 1    2
     * 1    2
     *  3333
     * 4    5
     * 4    5
     *  6666
     *
     * if the length is 5, the possible values are 2,3,5
     *  - if the values of segments 1/3 are both present, but only one from the 2/5, it is a 2
     *  - if the values of segments 4/6 are both present, but only one from the 2/5, it is a 5
     *  - 3 otherwise
     *
     * if the length is 6, the possible values are 0,6,9
     *  - if only one value of the 1/3 segments is present, it is a 0
     *  - if only one value of the 4/6 segments is present, it is a 9
     *  - 6 otherwise
     */
    private fun deductValue(s: Set<String>, segments: Map<Int, Set<String>>): Int {
        return when (s.size) {
            2 -> 1
            3 -> 7
            4 -> 4
            7 -> 8
            5 -> {
                if (segments[2]!!.intersect(s).size == 2) return 3
                if (segments[1]!!.intersect(s).size == 1) return 2
                return 5
            }
            else -> {
                if (segments[1]!!.intersect(s).size == 1) return 0
                if (segments[4]!!.intersect(s).size == 1) return 9
                return 6
            }
        }
    }

}