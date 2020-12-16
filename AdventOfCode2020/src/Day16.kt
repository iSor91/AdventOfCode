import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year

fun main() {
    Day16().executeGoals()
}

//@TestResources
@Year(2020)
class Day16 :AOC_Runner() {

    val tickets = mutableMapOf<Int, List<Int>>()

    val rules = mutableMapOf<String, Pair<IntRange, IntRange>>()

    val ruleRegex = Regex("([a-z ]+): (\\d+)-(\\d+) or (\\d+)-(\\d+)")
    lateinit var myTicket: List<Int>

    init {
        var i = 0
        while (!allLines[i].isEmpty()) {
            val rule = ruleRegex.find(allLines[i])!!
            val groupValues = rule.groupValues
            rules.put(groupValues[1], Pair(IntRange(groupValues[2].toInt(), groupValues[3].toInt()), IntRange(groupValues[4].toInt(), groupValues[5].toInt())))
            i++
        }
        i++
        while(!allLines[i++].isEmpty()) {
            myTicket = allLines[i++].split(",").map { it.toInt() }
        }
        for (index in ++i until allLines.size) {
            tickets.putIfAbsent(index-i, allLines[index].split(",").map { it.toInt() })
        }
    }

    private fun isInvalidNum(num: Int) : Boolean = rules.none { r -> r.value.first.contains(num) or r.value.second.contains(num) }

    override fun executeGoal_1() {
        var count = 0
        tickets.forEach { it.value.forEach{num -> count += if (isInvalidNum(num)) num else 0} }
        println(count)
    }

    override fun executeGoal_2() {
        val validTickets = tickets.filter { it.value.none { num -> isInvalidNum(num) } }

        val ruleMap = mutableMapOf<Int,MutableSet<String>>()
        rules.forEach { rule ->
            for( i in 0 until rules.size)
                if(validTickets.all { rule.value.first.contains(it.value[i]) || rule.value.second.contains(it.value[i]) }) {
                    val orDefault = ruleMap.getOrPut(i, { mutableSetOf<String>()})
                    orDefault.add(rule.key)
                }
        }

        while(!ruleMap.all { it.value.size == 1 }) {
            val unambigouosRules = ruleMap.values.filter { it.size == 1 }
            unambigouosRules.forEach { uRule ->
                ruleMap.values.forEach{r ->
                    if(r != uRule) r.remove(uRule.first()) }}
        }

        ruleMap.entries.forEach { println("${it.key} - ${it.value}") }

        val filter = ruleMap.filter { it.value.first().startsWith("departure") }.keys

        var result = 1L
        filter.forEach {
            result *= myTicket[it]
        }
        println(result)
    }

}