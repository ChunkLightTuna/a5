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
//    println(formula)

    val sol = alg(formula)

    println(sol.print(formula))

}

data class Formula(val clauses: HashSet<Clause>, private val numVariables: Int, val numClauses: Int) {
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

data class Clause(val clause: ArrayList<Int>, var satisfied: Int = -1) {

    //satisfied is the index, but we probably want the value in most cases
    override fun toString(): String {
        return "${if (satisfied < 0 || satisfied > clause.size) 0 else clause[satisfied]}$clause"
    }
}

data class Assignment(val variables: ArrayList<Boolean> = arrayListOf()/*, val valid: Boolean = false*/) {

    fun print(formula: Formula): String {
        val sb = StringBuilder("s cnf ${if (formula.valid(this)) "1" else "0"} $formula\n")

        variables.forEachIndexed { i, b ->
            sb.append("v ${if (!b) "-" else ""}${i + 1}\n")
        }

        return sb.toString()
    }
}

fun mcv(formula: Formula): Assignment {


    fun inner(assignment: Assignment): Assignment {
        return if (formula.complete(assignment)) {
            assignment
        } else if (formula.valid(assignment)) {
            //branch!
            Assignment()
        } else {
            assignment
        }
    }

    return inner(Assignment())
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