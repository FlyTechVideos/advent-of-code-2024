package day25

import InputReader

class Day25 {

    private val input = InputReader.readInput(25, test = false, split = false)[0]

    private enum class Type {
        KEY, LOCK
    }

    private data class Element(val type: Type, val v0: Int, val v1: Int, val v2: Int, val v3: Int, val v4: Int) {
        private val MAX = 5

        override fun toString(): String {
            return "Element($type: $v0,$v1,$v2,$v3,$v4)"
        }

        fun fits(other: Element): Boolean {
            return this.type != other.type &&
                    this.v0 + other.v0 <= MAX &&
                    this.v1 + other.v1 <= MAX &&
                    this.v2 + other.v2 <= MAX &&
                    this.v3 + other.v3 <= MAX &&
                    this.v4 + other.v4 <= MAX
        }
    }

    fun solve1() {
        val entries = input.split("\n\n")
            .map {
                val type = if (it.startsWith("#####")) Type.LOCK else Type.KEY
                readElement(type, it)
            }.groupBy { it.type }

        val keys = entries[Type.KEY]!!
        val locks = entries[Type.LOCK]!!

        var fittingPairs = 0
        for (key in keys) {
            for (lock in locks) {
                if (key.fits(lock)) {
                    fittingPairs++
                }
            }
        }
        println(fittingPairs)
    }

    private fun readElement(type: Type, rawElement: String): Element {
        val trimmedElement = rawElement.replace("\n", "").trim()
        val v0 = (5..29 step 5).map { trimmedElement[it] }.count { it == '#' }
        val v1 = (6..29 step 5).map { trimmedElement[it] }.count { it == '#' }
        val v2 = (7..29 step 5).map { trimmedElement[it] }.count { it == '#' }
        val v3 = (8..29 step 5).map { trimmedElement[it] }.count { it == '#' }
        val v4 = (9..29 step 5).map { trimmedElement[it] }.count { it == '#' }
        return Element(type, v0, v1, v2, v3, v4)
    }

    fun solve2() {
        println("Merry Christmas! 🎅🎄")
    }
}
