import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.Year
import kotlin.reflect.KFunction0

fun main() {
    Day14().executeGoals()
}

@Year(2020)
class Day14 : AOC_Runner() {

    lateinit var mask: String

    val maxBit = 68719476736L

    val maskRegex = Regex("mask = ([0,1,X]{36})")
    val memRegex = Regex("mem\\[(\\d+)\\] = ([\\d]+)")

    val memory1 = mutableMapOf<Long, String>()
    val memory2 = mutableMapOf<Long, String>()

    var memAddress: String = ""
    var toWrite: String = ""

    override fun executeGoal_1() {
        execute(memory1, this::writeToMem1)
    }

    override fun executeGoal_2() {
        execute(memory2, this::writeToMem2)
    }

    private fun execute(memory: MutableMap<Long, String>, writeFunction: KFunction0<Unit>) {
        allLines.forEach {
            val maskMatch = maskRegex.matches(it)
            if (maskMatch) {
                mask = maskRegex.find(it)!!.groupValues[1]
            } else {
                val memValues = memRegex.find(it)!!.groupValues
                memAddress = memValues[1]
                toWrite = memValues[2]

                writeFunction.call()
            }
        }
        println(memory.values.map { it.toLong(2) }.sum())
    }

    fun writeToMem1() {
        val longValue = toWrite.toLong() + maxBit
        memory1[memAddress.toLong()] = longValue.toString(2).substring(1).mask(mask)
    }

    fun String.mask(mask: String): String {
        val newval = CharArray(36)
        this.forEachIndexed { i, c ->
            when (mask[i]) {
                'X' -> newval[i] = c
                else -> newval[i] = mask[i]
            }
        }
        return newval.joinToString("")
    }

    fun writeToMem2() {
        val memVal = memAddress.toLong() + maxBit
        val decode = memVal.toString(2).substring(1).decode(mask)
        decode.forEach { memory2[it] = toWrite.toLong().toString(2) }
    }

    fun String.decode(mask: String): List<Long> {
        val addresses = mutableListOf<CharArray>()
        addresses.add(CharArray(36) { '0' })
        mask.forEachIndexed { i, c ->
            when (c) {
                'X' -> {
                    val toAdd = mutableListOf<CharArray>()
                    addresses.forEach {
                        val copyAddress = it.copyOf()
                        copyAddress[i] = '1'
                        toAdd.add(copyAddress)
                        it[i] = '0'
                    }
                    addresses.addAll(toAdd)
                }
                else -> addresses.forEach { it[i] = if (c == '1') '1' else this[i] }
            }
        }
        return addresses.map { it.joinToString("").toLong(2) }
    }
}