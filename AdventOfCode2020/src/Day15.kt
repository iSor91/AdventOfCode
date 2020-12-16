import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.Year

fun main() {
    Day15().executeGoals()
}

//@TestResources
@Year(2020)
class Day15 :AOC_Runner() {

    val numberMap = mutableMapOf<Int,MutableList<Int>>()

    override fun executeGoal_1() {
        allLines.forEach { solve(it, 2020)}
    }

    private fun solve(it: String, countTo: Int) {
        numberMap.clear()
        val split = it.split(",")
        var index = 1
        var lastNum = -1;
        for (s in split) {
            lastNum = s.toInt()
            createNewList(lastNum, index++)
        }
        for (i in index..countTo) {
            val occurrences = numberMap[lastNum]!!
            lastNum = if (occurrences.size == 1) 0 else occurrences.last() - occurrences[occurrences.size - 2]
            createNewList(lastNum, i)
        }
        println(lastNum)
    }

    private fun createNewList(lastNum: Int, i: Int) {
        val newOccurrences = getOccurences(lastNum)
        newOccurrences.add(i)
        numberMap.putIfAbsent(lastNum,newOccurrences)
    }

    private fun getOccurences(lastNum: Int): MutableList<Int> {
        var list = numberMap[lastNum]
        if (list == null) list = mutableListOf<Int>()
        return list!!
    }

    override fun executeGoal_2() {
        allLines.forEach { solve(it, 30000000) }
    }

}