import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year
import java.util.stream.Collectors

fun main() {
    Day5().executeGoals()
}

//@TestResources
@Year(2020)
class Day5 : AOC_Runner() {
    private val seats: MutableList<Pair<Int, Int>> = mutableListOf()

    init {
        val translationMap = mapOf('F' to "0", 'B' to "1", 'L' to "0", 'R' to "1")
        allLines.forEach {
            val row = it.subSequence(0, 7).translateToBinaryString(translationMap)
            val column = it.substring(7).translateToBinaryString(translationMap)
            seats.add(Pair(row, column))
        }
    }

    override fun executeGoal_1() {
        val max = seats.maxBy { it.first * 8 + it.second }
        println("$max - ${max!!.first * 8 + max!!.second}")
    }

    override fun executeGoal_2() {
        val sortedMap = seats.groupBy { it.first }.toSortedMap(Comparator { o1, o2 -> o1.compareTo(o2) })
        val validRows = sortedMap.filter { it.value.size == 7 && it.key != 1 && it.key != seats.size}
        val occupiedSeats = validRows.values.first().map { it.second }
        val row = validRows.entries.first().key
        val column = (0..7).toList().filter { !occupiedSeats.contains(it) }[0]
        println("$row - $column ${row * 8 + column}")
    }
}
