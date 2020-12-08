import com.isor.aoc.common.AOC_Runner
import com.isor.aoc.common.TestResources
import com.isor.aoc.common.Year
import java.util.*

fun main() {
    Day8().executeGoals()
}

//@TestResources
@Year(2020)
class Day8 :AOC_Runner() {

    override fun executeGoal_1() {
        val executed : MutableSet<Int> = mutableSetOf()
        var acc = 0
        var index = 0

        while (!executed.contains(index)) {
            executed.add(index)
            val s = allLines[index]
            if(s.startsWith("acc")) {
                acc += s.split(" ")[1].toInt()
                index++
            } else if(s.startsWith("jmp")){
                index += s.split(" ")[1].toInt()
            } else {
                index ++
            }
        }
        println(acc)
    }

    override fun executeGoal_2() {
        var executed : MutableList<Int> = mutableListOf()
        var acc = 0
        var index = 0
        val changeableInstructions = linkedMapOf<Int,Int>()
        val keepExecuted = linkedMapOf<Int,Int>()
        var changed = false

        while (index != allLines.size) {
            executed.add(index)
            var originalIndex = index
            val s = allLines[index]
            var instruction = s.split(" ")[0]
            var argument = s.split(" ")[1].toInt()
            if(instruction == "acc") {
                acc += argument
                index++
            } else if(instruction =="jmp"){
                if(!changed) {
                    changeableInstructions[index] = acc
                    keepExecuted[index] = executed.size
                }
                index += argument
            } else {
                if(!changed) {
                    changeableInstructions[index] = acc
                    keepExecuted[index] = executed.size
                }
                index ++
            }
            if(executed.contains(index) && !changed) {
                changed = true
                if(instruction == "jmp") {
                    index= originalIndex+1
                } else if(instruction!="acc"){
                    index = originalIndex+argument
                }
            }else if(executed.contains(index) && changed){
                val lastIndex = changeableInstructions.toList().last()
                changeableInstructions.remove(lastIndex.first)
                originalIndex = lastIndex.first
                acc = lastIndex.second
                executed = executed.slice(0..keepExecuted[originalIndex]!!).toMutableList()
                val s1 = allLines[originalIndex]
                instruction = s1.split(" ")[0]
                argument = s1.split(" ")[1].toInt()
                if(instruction == "jmp") {
                    index= originalIndex+1
                } else if(instruction!="acc"){
                    index = originalIndex+argument
                }
            }
        }
        println(acc)
    }

}