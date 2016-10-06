import java.io.FileInputStream
import java.io.InputStream
import java.util.HashSet

/**
 * Assignment 5: SAT Solver
 */

fun main(args: Array<String>) {
//    var alg = ::DLL

    args.forEach {
        if (it.endsWith(".cnf")) {
            System.setIn(FileInputStream(it))
//        } else if (it == "DLL") {
//            alg = ::DLL
        }
    }

    val formula = buildFormula(System.`in`)
    System.`in`.close()

    val sol = DLL(formula)

    sol.assignment.forEach(::println)
}

private data class Clause(val variables: MutableSet<Int>) {
    fun size(): Int {
        return variables.size
    }

    fun isEmpty(): Boolean {
        return variables.isEmpty()
    }

    override fun toString(): String {
        return variables.toString()
    }
}


private data class Formula(val clauses: List<Clause>, val assignment: HashSet<Int> = hashSetOf()) {
    fun gameOverMan(): Boolean {
        clauses.forEach {
            if (it.size() == 0) {
                return true
            }
        }
        return false
    }

    fun assign(value: Int) {
        clauses.forEach {
            val variables = it.variables
            variables.forEach inner@{
                if (value == it) {
                    assignment.add(value)
                } else if (value * -1 == it) {
                    assignment.add(value * -1)
                    variables.remove(value)
                }
            }
        }
    }

    fun weDidIt(): Boolean {
        return clauses.isEmpty()
    }

    fun nextOnTheChoppingBlock(): Int {
        return clauses.first().variables.first()
    }

    fun allTheSingleLadies(): List<Int> {
        return clauses.filter { it.size() == 1 }.flatMap { it.variables }
    }

    fun unitPropagate() {

        var allTheSingleLadies = allTheSingleLadies()
        while (allTheSingleLadies.isNotEmpty()) {
            allTheSingleLadies.forEach { assign(it) }
            allTheSingleLadies = allTheSingleLadies()
        }/* Now put your hands up, oh, oh, oh

            'Cause if you liked it then you should have put a ring on it
            If you liked it then you shoulda put a ring on it
            Don't be mad once you see that he want it
            If you liked it then you shoulda put a ring on it
            Oh, oh, oh

            If you liked it then you should have put a ring on it
            If you liked it then you shoulda put a ring on it
            Don't be mad once you see that he want it
            If you liked it then you shoulda put a ring on it
            Oh, oh, oh*/
    }
}

private data class Solution(val assignment: List<Int>, val satisfiable: Boolean, val count: Int)

private fun DLL(φ: Formula): Solution {
    var count = 0
    var assignment = mutableListOf<Int>()

    fun inner(φ: Formula): Boolean {

        φ.unitPropagate()

        return if (φ.weDidIt()) {
            true
        } else if (φ.gameOverMan()) {
            false
        } else {
            false
//            branch!
//            formula.variables[formula.variables]
//            Assignment(formula)
        }
    }

    val satisfiable = inner(φ)
    println("assignment:")
    assignment.forEach(::println)

    return Solution(assignment, satisfiable, count)

//    return Solution(inner(φ).assignment.toList(), count)
}


private fun buildFormula(inputStream: InputStream): Formula {

    val clauses = mutableListOf<Clause>()
    val variables = hashSetOf<Int>()
    var clause = mutableSetOf<Int>()
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
                        clause = mutableSetOf()
                    } else {
                        clause.add(it.toInt())
                        variables.add(Math.abs(it.toInt()))
                    }
                }
            }
        }
    }

    assert(numClauses == clauses.size)
    assert(numVariables == variables.size)
    return Formula(clauses)
}