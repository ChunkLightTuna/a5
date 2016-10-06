//
//
//private class Formula2 private constructor(
//        private val clauses: Array<List<Int>>, //0 indexed
//        val numVariables: Int,
//        val numClauses: Int,
//        val entangledClauses: Array<List<Int>>)    //0 is blank! 1 indexed
//{
//    constructor(clauses: Array<List<Int>>, numVariables: Int) : this(clauses, numVariables, clauses.size, genLookUpTable(clauses, numVariables)) {
//    }
//
//    override fun toString(): String {
//        return "$numVariables ${clauses.size}\n"
//    }
//
//    fun log(): Boolean {
//        println("entangledClauses:")
//
//        entangledClauses.forEachIndexed { i, listOfClauses ->
//            if (i != 0) {
//                println("v $i in c $listOfClauses")
//            }
//        }
//        return true
//    }
//
//    fun getClause(clause: Int): List<Int> {
//        return clauses[clause - 1]
//    }
//
//    companion object {
//        private fun genLookUpTable(clauses: Array<List<Int>>, numVariables: Int): Array<List<Int>> {
//            val mutable = Array(numVariables + 1, { x -> mutableListOf<Int>() })
//            clauses.forEachIndexed { i, list ->
//                list.forEach {
//                    mutable[Math.abs(it)].add(i + 1)
//                }
//            }
//
//            return Array(mutable.size, { i -> mutable[i].toList() })
//        }
//    }
//
//
//}
//
//private class Assignment(
//        private val vSign: BooleanArray, //index 0 is unused!
//        private val vAssigned: BooleanArray, //index 0 is unused!
//        private val clauseSat: BooleanArray) //index 0 is unused!
//{
//    constructor(formula: Formula) : this(BooleanArray(formula.numVariables + 1), BooleanArray(formula.numVariables + 1), BooleanArray(formula.numClauses + 1))
//
//
//    fun print(formula: Formula): String {
//        val sb = StringBuilder("s cnf ${if (complete()) "1" else "0"} $formula")
//        vSign.drop(1).forEachIndexed { i, b ->
//            sb.append("v ${if (!b) "-" else ""}${i + 1}\n")
//        }
//
//        return sb.toString()
//    }
//
//    fun assignVariable(formula: Formula, variable: Int, sign: Boolean): Boolean {
//        assert(variable != 0 && variable <= vAssigned.size - 1)
//
//        vSign[variable] = sign
//        vAssigned[variable] = true
//
//        val entagledClauses = formula.entangledClauses[variable]
//
//        entagledClauses.forEach {
//            val clause = it
//            if (!clauseSat[clause]) {
//                formula.getClause(clause).forEach {
//                    val match = if (sign) variable else -1 * variable
//                    if (match == it) {
//                        clauseSat[clause] = true
//                    }
//                }
//            }
//        }
//
//        //indices of unsatisfied
//        clauseSat.filterIndexed { i, b -> b && i != 0 }
//
//        return false
//    }
//
//    fun allVariabledAreAssigned(): Boolean {
//        vAssigned.drop(1).forEach {
//            if (it == false) return false
//        }
//        return true
//    }
//
//    fun complete(): Boolean {
//        clauseSat.drop(1).forEach {
//            if (it == false) return false
//        }
//        return true
//    }
//
//    fun clauseIsUnit(clause: List<Int>): Boolean {
//        var assigned = 0
//        clause.forEach {
//            if (vAssigned[it]) {
//                assigned++
//            }
//        }
//        return assigned + 1 == clause.size
//    }
//}