package com.isor.aoc2015

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.Year
import java.util.*
import java.util.function.BiFunction
import kotlin.collections.HashMap

/**
 * --- Day 7: Some Assembly Required ---
This year, Santa brought little Bobby Tables a set of wires and bitwise logic gates! Unfortunately, little Bobby is a little under the recommended age range, and he needs help assembling the circuit.

Each wire has an identifier (some lowercase letters) and can carry a 16-bit signal (a number from 0 to 65535). A signal is provided to each wire by a gate, another wire, or some specific value. Each wire can only get a signal from one source, but can provide its signal to multiple destinations. A gate provides no signal until all of its inputs have a signal.

The included instructions booklet describes how to connect the parts together: x AND y -> z means to connect wires x and y to an AND gate, and then connect its output to wire z.

For example:

123 -> x means that the signal 123 is provided to wire x.
x AND y -> z means that the bitwise AND of wire x and wire y is provided to wire z.
p LSHIFT 2 -> q means that the value from wire p is left-shifted by 2 and then provided to wire q.
NOT e -> f means that the bitwise complement of the value from wire e is provided to wire f.
Other possible gates include OR (bitwise OR) and RSHIFT (right-shift). If, for some reason, you'd like to emulate the circuit instead, almost all programming languages (for example, C, JavaScript, or Python) provide operators for these gates.

For example, here is a simple circuit:

123 -> x
456 -> y
x AND y -> d
x OR y -> e
x LSHIFT 2 -> f
y RSHIFT 2 -> g
NOT x -> h
NOT y -> i
After it is run, these are the signals on the wires:

d: 72
e: 507
f: 492
g: 114
h: 65412
i: 65079
x: 123
y: 456
In little Bobby's kit's instructions booklet (provided as your puzzle input), what signal is ultimately provided to wire a?

Your puzzle answer was 956.

--- Part Two ---
Now, take the signal you got on wire a, override wire b to that signal, and reset the other wires (including wire a). What new signal is ultimately provided to wire a?

Your puzzle answer was 40149.
 */

fun main() {
    Day_7().executeGoals()
}

@Year(2015)
class Day_7 : AOC_Runner() {

    val functions = hashMapOf<String, BiFunction<UShort,UShort,UShort>>(
            "AND" to BiFunction { t, u ->  t and  u },
            "OR" to BiFunction { t, u  -> t or u},
            "NOT" to BiFunction { t, u -> t.inv() },
            "RSHIFT" to BiFunction { t, u ->
                val i = t.toInt() ushr u.toInt()
                i.toUShort()
            },
            "LSHIFT" to BiFunction { t, u ->
                val i = t.toInt() shl u.toInt()
                i.toUShort()
            }
    )

    val REGEX = Regex("[0-9]+")

    override fun executeGoal_1() {
        evaluate(allLines)
    }

    override fun executeGoal_2() {
        val indexOf = allLines.indexOf("14146 -> b")
        val mutableListOf = allLines.toMutableList()
        mutableListOf.removeAt(indexOf)
        mutableListOf.add(indexOf, "956 -> b")
        evaluate(mutableListOf)
    }

    private fun evaluate(lines : List<String>) {
        val values = hashMapOf<String, UShort>()
        while (values["a"] == null) {
            lines.forEach {
                val split = it.split(" -> ")
                val source = split[0].trim()
                val target = split[1].trim()
                val inputs = source.split(" ")
                var result: UShort? = null
                when (inputs.size) {
                    1 -> {
                        result = getValue(inputs[0], values)
                    }
                    2 -> {
                        val function = functions[inputs[0]]
                        val a = getValue(inputs[1], values)
                        val b: UShort = 0u
                        if (a != null) {
                            result = function?.apply(a, b)
                        }
                    }
                    3 -> {
                        val a = getValue(inputs[0], values)
                        val function = functions[inputs[1]]
                        var b: UShort?
                        b = getValue(inputs[2], values)
                        if (a != null && b != null) {
                            result = function?.apply(a, b)
                        }
                    }
                }
                if (result != null) {
//                    println("""$source  old value: ${values[target]} new value: $result""")
                    values[target] = result
                }
            }
        }
        println(values["a"])
    }

    private fun getValue(input: String, values: HashMap<String, UShort>): UShort? {
        return if (input.matches(REGEX))
            input.toUShort()
        else
            values[input]

    }
}