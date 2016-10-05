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

    formula.log()
    val sol = alg(formula)

    println(sol.print(formula))

}

private class Formula private constructor(
        private val clauses: Array<List<Int>>,
        val numVariables: Int,
        val numClauses: Int,
        private val entangledClauses: Array<List<Int>>) {
    constructor(clauses: Array<List<Int>>, numVariables: Int) : this(clauses, numVariables, clauses.size, genLookUpTable(clauses, numVariables)) {
    }

    override fun toString(): String {
        return "$numVariables ${clauses.size}\n"
    }

    fun log() {
        println("   clauses:\n$clauses\n\n\n   entangledClauses:")

        entangledClauses.forEachIndexed { i, listOfClauses ->
            if (i != 0) {
                println("variable $i is in the following clauses $listOfClauses")
            }
        }
    }

    fun getEntangledClauses(variable: Int): List<Int> {
        assert(variable > 0 && variable <= numVariables)
        return entangledClauses[variable]
    }

    companion object {
        private fun genLookUpTable(clauses: Array<List<Int>>, numVariables: Int): Array<List<Int>> {
            val mutable = Array(numVariables + 1, { x -> mutableListOf<Int>() })
            clauses.forEachIndexed { i, list ->
                list.forEach {
                    mutable[Math.abs(it)].add(i + 1)
                }
            }

            return Array(mutable.size, { i -> mutable[i].toList() })
        }
    }
}

//private data class Clause(val clause: ArrayList<Int>)

private class Assignment(private val vSign: BooleanArray, private val vAssigned: BooleanArray, private val clauseSat: BooleanArray) {
    constructor(formula: Formula) : this(BooleanArray(formula.numVariables + 1), BooleanArray(formula.numVariables + 1), BooleanArray(formula.numClauses + 1))


    fun print(formula: Formula): String {
        val sb = StringBuilder("s cnf ${if (complete()) "1" else "0"} $formula")
        vSign.drop(1).forEachIndexed { i, b ->
            sb.append("v ${if (!b) "-" else ""}${i + 1}\n")
        }

        return sb.toString()
    }

    fun assign(formula: Formula, variable: Int) {
        assert(variable != 0)

        //indices of unsatisfied
        clauseSat.filterIndexed { i, b -> b && i != 0 }

        val sign = variable > 0
    }

    fun complete(): Boolean {
        clauseSat.drop(1).forEach {
            if (it == false) return false
        }
        return true
    }

}

private fun dll(formula: Formula): Assignment {


    fun inner(assignment: Assignment): Assignment {
        return if (assignment.complete()) {
            assignment
//        } else if (formula.valid(assignment)) {
//            //branch!
//            assignment.variables[assignment.variables]
//            Assignment(formula)
        } else {
            assignment
        }
    }

    return inner(Assignment(formula))
}

private fun buildFormula(inputStream: InputStream): Formula {

    val clauses = arrayListOf<List<Int>>()
    var clause = mutableListOf<Int>()
    var numVariables = 0
//    var numClauses = 0

    inputStream.bufferedReader().forEachLine {
        if (!it.startsWith('c', false)) {
            val split = it.trim().split(Regex(" +"))
            if (split[0] == "p") {
                numVariables = split[2].toInt()
//                numClauses = split[3].toInt()
            } else {
                split.forEach {
                    if (it == "0") {
                        clauses.add(clause)
                        clause = mutableListOf()
                    } else {
                        clause.add(it.toInt())
                    }
                }
            }
        }
    }

    return Formula(clauses.toTypedArray(), numVariables)
}