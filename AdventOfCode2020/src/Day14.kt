import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.Year

fun main() {
    Day14().executeGoals()
}

@Year(2020)
class Day14 : AOC_Runner() {

    lateinit var mask: String

    val maxBit = 68719476736L

    val maskRegex = Regex("mask = ([0,1,X]{36})")
    val memRegex = Regex("mem\\[(\\d+)\\] = ([\\d]+)")

    override fun executeGoal_1() {
        val memory = mutableMapOf<Int, String>()

        allLines.forEach {
            val maskMatch = maskRegex.matches(it)
            val memMatch = memRegex.matches(it)

            if(maskMatch) {
                mask = maskRegex.find(it)!!.groupValues[1]
            } else {
                val memValues = memRegex.find(it)!!.groupValues
                val memAddress = memValues[1]
                val toWrite = memValues[2]
                val longValue = toWrite.toLong() + maxBit
                val substring = longValue.toString(2).substring(1).mask(mask)
                memory[memAddress.toInt()] = substring
            }
        }
        val sum = memory.values.map { it.toLong(2) }.sum()
        println(memory)
        println(sum)
    }

    fun String.mask(mask: String) : String {
        val newval = CharArray(36)
        for(i in 0 until this.length) {
            val c = mask[i]
            if(c == 'X') {
                newval[i] = this[i]
            } else {
                newval[i] = mask[i]
            }
        }
        return newval.joinToString(separator = "")
    }

    override fun executeGoal_2() {
        val memory = mutableMapOf<Long, String>()
        allLines.forEach {
            val maskMatch = maskRegex.matches(it)
            val memMatch = memRegex.matches(it)

            if (maskMatch) {
                mask = maskRegex.find(it)!!.groupValues[1]
            } else {
                val memValues = memRegex.find(it)!!.groupValues
                val memAddress = memValues[1]
                val toWrite = memValues[2]

                val memVal = memAddress.toLong() + maxBit
                val decode = memVal.toString(2).substring(1).decode(mask)

                decode.forEach { memory[it] = toWrite }

            }
        }
        val sum = memory.values.map { it.toLong() }.sum()
        println(memory)
        println(sum)
    }

    fun String.decode(mask: String): List<Long> {
        val addresses = mutableListOf<CharArray>()
        if(addresses.isEmpty()) {
            addresses.add(CharArray(36){'0'})
        }
        mask.forEachIndexed { i, c ->
            if(c == 'X') {
                val toAdd = mutableListOf<CharArray>()
                addresses.forEach {
                    val copyAddress = it.copyOf()
                    copyAddress[i] = '1'
                    toAdd.add(copyAddress)
                    it[i] = '0'
                }
                addresses.addAll(toAdd)
            } else {
                addresses.forEach { it[i] = if(c == '1') '1' else this[i] }
            }
        }
        return addresses.map { it.joinToString("").trim().toLong(2) }
    }
}