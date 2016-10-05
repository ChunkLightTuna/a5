import java.io.FileInputStream
import java.io.InputStream
import java.util.*

/**
 * Assignment 5: SAT Solver
 */

fun main(args: Array<String>) {
    var alg = ::dll

    args.forEach {
        if (it.endsWith(".cnf")) {
            System.setIn(FileInputStream(it))
//        } else if (it == "dll") {
//            alg = ::dll
        }
    }

    val formula = buildFormula(System.`in`)
//    println(formula)

    val sol = alg(formula)

    println(sol.print(formula))

}

private data class Formula(val clauses: HashSet<ArrayList<Int>>, val numVariables: Int, val numClauses: Int) {
    override fun toString(): String {
        return "$numVariables $numClauses\n"
    }

    fun valid(assignment: Assignment): Boolean {
        return false
    }

    fun complete(assignment: Assignment): Boolean {
        return assignment.variables.size == numVariables
    }
}

//private data class Clause(val clause: ArrayList<Int>)

private class Assignment(val variables: BooleanArray, val clauses: BooleanArray) {
    constructor(formula: Formula) : this(BooleanArray(formula.numVariables), BooleanArray(formula.numClauses))

    fun print(formula: Formula): String {
        val sb = StringBuilder("s cnf ${if (formula.valid(this)) "1" else "0"} $formula\n")

        variables.forEachIndexed { i, b ->
            sb.append("v ${if (!b) "-" else ""}${i + 1}\n")
        }

        return sb.toString()
    }
}

private fun dll(formula: Formula): Assignment {


    fun inner(assignment: Assignment): Assignment {
        return if (formula.complete(assignment)) {
            assignment
        } else if (formula.valid(assignment)) {
            //branch!
//            assignment.variables[assignment.variables]
            Assignment(formula)
        } else {
            assignment
        }
    }

    return inner(Assignment(formula))
}

private fun buildFormula(inputStream: InputStream): Formula {

    val clauses: HashSet<ArrayList<Int>> = hashSetOf()
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
                        clauses.add(clause)
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