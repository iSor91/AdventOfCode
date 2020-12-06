import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year

fun main() {
    Day6().executeGoals()
}

@TestResources
@Year(2020)
class Day6 : AOC_Runner(){

    override fun executeGoal_1() {
        val groups : MutableList<Set<Char>> = mutableListOf()
        var group: MutableSet<Char> = mutableSetOf()
        allLines.forEach{
            if(it.isEmpty()) {
                groups.add(group)
                group = mutableSetOf()
            } else {
                val elements = it.toCharArray().toList()
                group.addAll(elements)
            }
        }
        groups.add(group)
        println(groups.sumBy { it.size })
    }

    override fun executeGoal_2() {
        val fullArray = ('a'..'z').toList().toCharArray()
        val groups : MutableList<Set<Char>> = mutableListOf()
        var group: CharArray = fullArray
        allLines.forEach{
            if(it.isEmpty()) {
                groups.add(group.toSet())
                group = fullArray
            } else {
                val elements = it.toCharArray()
                group = group.and(elements)
            }
        }
        groups.add(group.toSet())
        println(groups.sumBy { it.size })


    }

    private infix fun CharArray.and(other:CharArray) : CharArray {
        return other.filter { this.contains(it) }.toCharArray()
    }

}