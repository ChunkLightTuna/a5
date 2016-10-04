import java.io.FileInputStream
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

    val reader = System.`in`.bufferedReader()

    var line = reader.readLine()

    while (line[0] == 'c') {
        line = reader.readLine()
    }

    val split = line.trim().split(" ")

    println("header! $line | var=${split[2].toInt()} cla=${split[3].toInt()}")
    val domain = Domain(numVariables = split[2].toInt(), numClauses = split[3].toInt())

    var i = reader.read()
    val clause: MutableCollection<Int> = mutableListOf()

    while (i != -1) {
        val ch = i.toChar()

        if (ch == 'c') {            //skip comments
            reader.readLine()

        } else if (ch == '0') {      //next
            domain.add(clause)
            clause.clear()

        } else if (ch == '-') {  //add
            clause.add(Character.getNumericValue(reader.read().toChar()) * -1)

        } else if (ch.isDigit()) {  //add
            clause.add(Character.getNumericValue(ch))
        }

        i = reader.read()
    }
    domain.assert()

    val sol = alg()

}


class Domain(val numVariables: Int, val numClauses: Int, val clauses: HashSet<Collection<Int>> = hashSetOf(), private val variables: HashSet<Int> = hashSetOf()) {
    fun add(clause: Collection<Int>) {
        assert(clause.size > 0 && clause.size <= 3, { ->
            "added clause of incorrect size. Expected 1-3, but found ${clause.size}"
        })
        clauses.add(clause)
        clause.forEach { variables.add(Math.abs(it)) }
    }

    fun assert() {
        kotlin.assert(numClauses == clauses.size, { ->
            "wrong # of clauses. Expected $numClauses, but found ${clauses.size}"
        })
        kotlin.assert(numVariables == variables.size, { ->
            "wrong # of variables.  Expected $numVariables, but found ${variables.size}"
        })

        println("V Expected: $numVariables V Actual: ${variables.size}")
        println("C Expected: $numClauses Actual: ${clauses.size}")

        variables.clear()
    }
}

data class State(val a: Int) {
}

fun mcv() {
}
