import com.sun.org.apache.xpath.internal.operations.Bool
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStream
import java.util.*

/**
 * Assignment 5: SAT Solver
 */

fun main(args: Array<String>) {
    var alg = ::mcv

    args.forEach {
        if (it.endsWith(".cnf")) {
            System.setIn(FileInputStream(it))
        } else if (it == "mcv") {
            alg = ::mcv
        }
    }

    val formula = buildFormula(System.`in`)

    val sol = alg(formula)

    println(sol.print(formula))

}

data class Formula(val clauses: HashSet<Clause>, val numVariables: Int, val numClauses: Int) {
    override fun toString(): String {
        return "$numVariables, $numClauses\n$clauses"
    }

    fun solved(assignment: Assignment): Boolean {
        return false
    }
}

data class Clause(val clause: ArrayList<Int>, var satisfied: Int = 0) {
    override fun toString(): String {
        return "$satisfied$clause"
    }
}

data class Assignment(val assignments: ArrayList<Boolean>) {

    fun print(formula: Formula): String {
        val sb = StringBuilder("s cnf ${if (formula.solved(this)) "1" else "0"} ${formula.numVariables}${formula.numClauses}\n")
        assignments.forEachIndexed { i, b ->
            sb.append("v ${if (!b) "-" else ""}${i + 1}\n")
        }
        return sb.toString()
    }
}

fun mcv(clauses: Formula): Assignment {
    fun inner(assignment: Assignment): Assignment {
        return assignment
    }
    return Assignment(arrayListOf<Boolean>())
}

private fun buildFormula(inputStream: InputStream): Formula {

    val clauses: HashSet<Clause> = hashSetOf()
    var clause = arrayListOf<Int>()
    var numVariables = 0
    var numClauses = 0


    inputStream.bufferedReader().forEachLine {
        if (!it.startsWith('c', false)) {
            val split = it.trim().split(Regex(" +"))
            if (split[0] == "p") {
                numVariables = split[2].toInt()
                numClauses = split[3].toInt()
            } else {
                split.forEach {
                    if (it == "0") {
                        clauses.add(Clause(clause))
                        clause = arrayListOf<Int>()
                    } else {
                        clause.add(it.toInt())
                    }
                }
            }
        }
    }

    return Formula(clauses, numVariables, numClauses)
}