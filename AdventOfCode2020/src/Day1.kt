import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year

/**
 * --- Day 1: Report Repair ---
After saving Christmas five years in a row, you've decided to take a vacation at a nice resort on a tropical island. Surely, Christmas will go on without you.

The tropical island has its own currency and is entirely cash-only. The gold coins used there have a little picture of a starfish; the locals just call them stars. None of the currency exchanges seem to have heard of them, but somehow, you'll need to find fifty of these coins by the time you arrive so you can pay the deposit on your room.

To save your vacation, you need to get all fifty stars by December 25th.

Collect stars by solving puzzles. Two puzzles will be made available on each day in the Advent calendar; the second puzzle is unlocked when you complete the first. Each puzzle grants one star. Good luck!

Before you leave, the Elves in accounting just need you to fix your expense report (your puzzle input); apparently, something isn't quite adding up.

Specifically, they need you to find the two entries that sum to 2020 and then multiply those two numbers together.

For example, suppose your expense report contained the following:

1721
979
366
299
675
1456
In this list, the two entries that sum to 2020 are 1721 and 299. Multiplying them together produces 1721 * 299 = 514579, so the correct answer is 514579.

Of course, your expense report is much larger. Find the two entries that sum to 2020; what do you get if you multiply them together?

Your puzzle answer was 145875.

--- Part Two ---
The Elves in accounting are thankful for your help; one of them even offers you a starfish coin they had left over from a past vacation. They offer you a second one if you can find three numbers in your expense report that meet the same criteria.

Using the above example again, the three entries that sum to 2020 are 979, 366, and 675. Multiplying them together produces the answer, 241861950.

In your expense report, what is the product of the three entries that sum to 2020?

Your puzzle answer was 69596112.
 */

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