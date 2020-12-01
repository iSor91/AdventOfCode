import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year

fun main() {
    Day1().executeGoals()
}

//@TestResources
@Year(2020)
class Day1 : AOC_Runner() {

    var numbers: List<Int> = allLines.map { it.toInt() }.sorted()
    val sum = 2020

    override fun executeGoal_1() {
        var i = 0
        var j : Int = numbers.size-1
        while(numbers[i] + numbers[j] != sum && i != j) {
            if(numbers[i] + numbers[j] > sum) {
                j--
            } else {
                i++
            }
        }
        println("${numbers[i]} + ${numbers[j]} -> ${numbers[i] * numbers[j]}")
    }

    override fun executeGoal_2() {
        for( k in 0 until numbers.size-2) {
            var i = k+1
            var j = numbers.size-1

            while(numbers[i] + numbers[j] + numbers[k] != sum && i != j) {
                if(numbers[i] + numbers[j] + numbers[k] > sum) {
                    j--
                } else {
                    i++
                }
            }
            if(numbers[i] + numbers[j] + numbers[k] == sum) {
                println("${numbers[i]} + ${numbers[j]} + ${numbers[k]} = ${numbers[i] + numbers[j] + numbers[k]}")
                println("${numbers[i]} * ${numbers[j]} * ${numbers[k]} = ${numbers[i] * numbers[j] * numbers[k]}")
            }
        }

    }

}