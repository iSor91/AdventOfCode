package com.isor.aoc2015

import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.Year
import java.util.function.BiFunction

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
        val values = hashMapOf<String, UShort>()
        while (values["a"] == null) {
            allLines.forEach {
                val split = it.split(" -> ")
                val source = split[0].trim()
                val target = split[1].trim()
                val inputs = source.split(" ")
                var result: UShort? = null
                when (inputs.size) {
                    1 -> {
                        result = getValue(inputs[0],values)
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
                    println("""$source  old value: ${values[target]} new value: $result""")
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

    override fun executeGoal_2() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}