import java.io.FileInputStream
import java.io.InputStream
import java.util.HashSet

/**
 * Assignment 5: SAT Solver
 */

fun main(args: Array<String>) {
    args.forEach {
        if (it.endsWith(".cnf")) {
            System.setIn(FileInputStream(it))
        }
    }

    val container = buildFormula(System.`in`)
    System.`in`.close()

    println(dll(container))
}

private data class Formula(var clauses: List<MutableList<Int>>, val assignment: HashSet<Int> = hashSetOf()) {

    //data classes normal copy constructor does not work urrrrrrrrrrrrgh
    constructor(φ: Formula) : this(
            clauses = Array(φ.clauses.size, { i -> mutableListOf<Int>() }).toList(),
            assignment = hashSetOf()) {

        φ.clauses.forEachIndexed { i, mutableSet ->
            mutableSet.forEach {
                val int = it
                clauses[i].add(int)
            }
        }
        assignment.addAll(φ.assignment)

    }

    fun gameOverMan(): Boolean {
        clauses.forEach {
            if (it.size == 0) {
                return true
            }
        }
        return false
    }

    fun assign(cur: Int) {
        val partitioned = clauses.partition { it.contains(cur) }
        if (partitioned.first.size > 0) {
            clauses = partitioned.second.toMutableList()
            assignment.add(cur)
        }

        clauses.forEach {
            it.remove(-1 * cur)
        }
    }

    fun allTheSingleLadies(): List<Int> {
        return clauses.filter { it.size == 1 }.flatMap { it }
    }

    fun unitPropagate(): Int {
        var `Oh oh oh` = 0

        var allTheSingleLadies = allTheSingleLadies()
        while (allTheSingleLadies.count() > 0) {
            allTheSingleLadies.forEach { assign(it);`Oh oh oh`++ }
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
            If you liked it then you shoulda put a ring on it*/
        return `Oh oh oh`
    }
}

private data class Solution(
        val assignment: List<Int>,
        val satisfiable: Boolean,
        val count: Int,
        val numVariables: Int,
        val numClauses: Int) {

    override fun toString(): String {
        val sb = StringBuilder("s cnf ${if (satisfiable) "1" else "0"} $numVariables $numClauses\n")
        assignment.forEach { sb.append("v $it\n") }
        sb.append("$count branching nodes explored.")
        return sb.toString()
    }
}

private fun dll(container: Container): Solution {

    var count = 0
    val assignment = mutableListOf<Int>()

    fun inner(φ: Formula): Boolean {

        count += φ.unitPropagate()
        return if (φ.clauses.isEmpty()) {
            assignment.addAll(φ.assignment)
            true
        } else if (φ.gameOverMan()) {
            false
        } else {
            val head = φ.clauses.first().first()
            val negative = Formula(φ)

            count++
            φ.assign(head)
            if (inner(φ)) return true

            count++
            negative.assign(-1 * head)
            return inner(negative)
        }
    }

    val satisfiable = inner(container.formula)

    return Solution(
            assignment,
            satisfiable,
            count,
            container.numVariables,
            container.numClauses)
}

private data class Container(val formula: Formula, val numVariables: Int, val numClauses: Int)

private fun buildFormula(inputStream: InputStream): Container {

    val clauses = mutableListOf<MutableList<Int>>()
    val variables = hashSetOf<Int>()
    var clause = mutableListOf<Int>()
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
                        clause = mutableListOf()
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
    return Container(Formula(clauses), numVariables, numClauses)
}