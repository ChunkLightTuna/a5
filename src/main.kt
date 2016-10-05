import com.sun.org.apache.xpath.internal.operations.Bool
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStream
import java.util.HashSet

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

    val domain = buildState(System.`in`)

    assert(domain.assert())

    val sol = alg(domain)

    println(sol)

}

class State(
        val clauses: HashSet<Collection<Int>> = hashSetOf(),
        private val numVariables: Int,
        private val numClauses: Int,
        private val variables: HashSet<Int> = hashSetOf()) {

    fun add(clause: Collection<Int>) {
        assert(clause.size > 0 && clause.size <= 3, { ->
            "added clause of incorrect size. Expected 1-3, but found ${clause.size}"
        })
        clauses.add(clause)
        clause.forEach { variables.add(Math.abs(it)) }
    }

    fun assert(): Boolean {
        val bool = numClauses == clauses.size && numVariables == variables.size
        val sb = StringBuilder()
        clauses.forEach { sb.append(if (clauses.first() == it) it else " $it") }
        println(sb)
        if (!bool) {
            println("V Expected: $numVariables V Actual: ${variables.size}")
            println("C Expected: $numClauses Actual: ${clauses.size}")
        }
        variables.clear()
        return bool
    }
}

fun mcv(state: State): String {

    return state.toString()
}

private fun buildState(inputStream: InputStream): State {
    val reader = inputStream.bufferedReader()

    var line = reader.readLine()
    while (line[0] == 'c') {
        line = reader.readLine()
    }

    val split = line.trim().split(" ")
    val domain = State(numVariables = split[2].toInt(), numClauses = split[3].toInt())
    var clause: MutableCollection<Int> = mutableListOf()

    reader.forEachLine {
        if (!it.startsWith('c', false)) {
            it.trim().split(Regex(" +")).forEach {
                if (it != "0") {
                    clause.add(it.toInt())
                } else {
                    domain.add(clause)
                    clause = mutableListOf()
                }
            }
        }
    }

    return domain
}