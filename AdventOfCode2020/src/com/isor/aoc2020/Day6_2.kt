import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year

/**
 * Second solution for day 6 by modifying the input to improve code quality.
 */
fun main() {
    Day6_2().executeGoals()
}

/**
 * Modified input: find&replace "(.+)\n" lines to "(.+) "
 */
@Year(2020)
class Day6_2 : AOC_Runner(){

    override fun executeGoal_1() {
        val groups = allLines.map { it.sort().trim().toSet() }
        println(groups.sumBy { it.size })
    }

    override fun executeGoal_2() {
        var sum = 0
        allLines.forEach {line ->
            val groupSize = line.countChar(' ')
            sum += ('a'..'z').count { line.countChar(it) == groupSize+1 }
        }
        println(sum)
    }

}