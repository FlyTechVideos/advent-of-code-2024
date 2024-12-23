package day23

import InputReader

class Day23 {

    private val input = InputReader.readInput(23, test = false)

    private data class Computer(val id: String) {
        val connections = mutableSetOf<Computer>()

        override fun toString(): String {
            return id
        }

        fun triples(): Set<Set<Computer>> {
            val set = mutableSetOf<Set<Computer>>()
            for (c in connections) {
                for (c2 in c.connections) {
                    if (c2 != this && this.connections.contains(c2)) {
                        set.add(setOf(this, c, c2))
                    }
                }
            }
            return set
        }

        fun findClique(nodesSoFar: Set<Computer> = setOf(this)): Set<Computer> {
            for (c in connections) {
                if (c !in nodesSoFar && c.connections.containsAll(nodesSoFar)) {
                    val newNodes = nodesSoFar + c
                    return c.findClique(newNodes)
                }
            }
            return nodesSoFar
        }
    }

    fun solve1() {
        val network = parseNetwork()
        println(network
            .filter { it.id.startsWith("t") }
            .flatMap { it.triples() }
            .distinct()
            .count())
    }

    private fun parseNetwork(): List<Computer> {
        val map = mutableMapOf<String, Computer>()
        for (line in input) {
            val (s1, s2) = line.split("-")
            val n1 = map.computeIfAbsent(s1) { Computer(s1) }
            val n2 = map.computeIfAbsent(s2) { Computer(s2) }
            n1.connections.add(n2)
            n2.connections.add(n1)
        }
        return map.values.toList()
    }

    fun solve2() {
        val network = parseNetwork()
        println(network
            .map { it.findClique() }
            .maxBy { it.size }
            .sortedBy { it.id }
            .joinToString(","))
    }

}
