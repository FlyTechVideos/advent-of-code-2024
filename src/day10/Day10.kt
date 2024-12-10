package day10

import InputReader

class Day10 {

    private val input = InputReader.readInput(10, test = false)

    data class Node(
        private val x: Int,
        private val y: Int,
        val value: Int,
        var left: Node? = null,
        var right: Node? = null,
        var top: Node? = null,
        var bottom: Node? = null,
        var lastVisitedBy: Int? = null,
    ) {
        override fun toString(): String {
            return "Node{$value@$x $y / L${left?.value ?: '_'}, R${right?.value ?: '_'}, T${top?.value ?: '_'}, B${bottom?.value ?: '_'}}"
        }
    }

    fun solve2() {
        // Fun fact: I first had this exact solution for part 1, it was wrong
        // Turned out to be the correct solution for part 2 :D
        val nodes = input.mapIndexed { y, i -> i.split("").drop(1).dropLast(1).mapIndexed { x, j -> Node(x, y, j.toInt()) } }
        for (y in nodes.indices) {
            for (x in nodes[y].indices) {
                val node = nodes[y][x]
                if (x > 0) {
                    node.left = nodes[y][x - 1]
                }
                if (x < nodes[y].size - 1) {
                    node.right = nodes[y][x + 1]
                }
                if (y > 0) {
                    node.top = nodes[y - 1][x]
                }
                if (y < nodes.size - 1) {
                    node.bottom = nodes[y + 1][x]
                }
            }
        }
        nodes.flatMap {
            it.filter { node -> node.value == 0 }
        }.sumOf {
            countPathsToReachable9s(it)
        }.let { println(it) }
    }

    private fun countPathsToReachable9s(node: Node?, previousNode: Node? = null): Int {
        if (node == null) {
            return 0
        }
        if (previousNode != null && node.value != previousNode.value + 1) {
            return 0
        }
        if (node.value == 9) {
            return 1
        }

        return countPathsToReachable9s(node.left, node) +
            countPathsToReachable9s(node.right, node) +
            countPathsToReachable9s(node.top, node) +
            countPathsToReachable9s(node.bottom, node)
    }

    fun solve1() {
        val nodes = input.mapIndexed { y, i -> i.split("").drop(1).dropLast(1).mapIndexed { x, j -> Node(x, y, j.toInt()) } }
        for (y in nodes.indices) {
            for (x in nodes[y].indices) {
                val node = nodes[y][x]
                if (x > 0) {
                    node.left = nodes[y][x - 1]
                }
                if (x < nodes[y].size - 1) {
                    node.right = nodes[y][x + 1]
                }
                if (y > 0) {
                    node.top = nodes[y - 1][x]
                }
                if (y < nodes.size - 1) {
                    node.bottom = nodes[y + 1][x]
                }
            }
        }
        nodes.flatMap {
            it.filter { node -> node.value == 0 }
        }.mapIndexed { index, it ->
            countReachable9s(it, runIndex = index)
        }.sum().let { println(it) }
    }

    private fun countReachable9s(node: Node?, previousNode: Node? = null, runIndex: Int): Int {
        if (node == null) {
            return 0
        }
        if (previousNode != null && node.value != previousNode.value + 1) {
            return 0
        }
        if (node.value == 9) {
            if (node.lastVisitedBy == runIndex) {
                return 0
            }
            node.lastVisitedBy = runIndex
            return 1
        }

        return countReachable9s(node.left, node, runIndex) +
                countReachable9s(node.right, node, runIndex) +
                countReachable9s(node.top, node, runIndex) +
                countReachable9s(node.bottom, node, runIndex)
    }

}
