import com.sun.org.apache.xpath.internal.operations.Bool
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

/**
 * Assignment 4: Graph Coloring
 *
 * wikipedia.org/wiki/Rapidly-exploring_random_tree
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
    while (line.startsWith('c')) {
        line = reader.readLine()
    }
    val split = reader.readLine().trim().split(" ")

    val domain = Domain(numVariables = split[2].toInt(), numClauses = split[3].toInt())

    var i = reader.read()
    val clause:MutableCollection<Int> = mutableListOf()
    while (i != -1) {
        val ch = i.toChar()
        if (ch == 'c') {
            reader.readLine()
        }
        if (ch == '0') {
            //next
            domain.add(clause)
            clause.clear()

        } else if (ch.isDigit()) {
            //add
            clause.add(ch.toInt())
        }
        i = reader.read()
    }


    val sol = alg()

}

data class Three (var clause: Collection<Int>) {

    fun contains(i: Int): Boolean {
        return clause.contains(i)
    }

    fun containsN(i: Int): Boolean {
        return clause.contains(-i)
    }

    fun remove(i: Int) {
        clause = clause.minus(i)
    }
}


class Domain(val numVariables: Int, val numClauses: Int, val clauses: HashSet<Collection<Int>> = hashSetOf()) {
    fun add(clause: Collection<Int>) {
        clauses.add(clause)
    }
}

data class State(val a: Int) {
}

fun mcv() {
}
