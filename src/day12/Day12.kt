package day12

import InputReader

class Day12 {

    private val input = InputReader.readInput(12, test = false)

    data class Node(
        private val x: Int,
        private val y: Int,
        val value: String,
        var left: Node? = null,
        var right: Node? = null,
        var top: Node? = null,
        var bottom: Node? = null,
        var visited: Boolean = false,
    ) {
        override fun toString(): String {
            return "Node{$value@$x $y / L${left?.value ?: '_'}, R${right?.value ?: '_'}, T${top?.value ?: '_'}, B${bottom?.value ?: '_'}}"
        }
    }

    data class PerimeterAndArea(val perimeter: Int, val area: Int) {
        operator fun plus(other: PerimeterAndArea): PerimeterAndArea {
            return PerimeterAndArea(perimeter + other.perimeter, area + other.area)
        }

        fun reduceSameSidePerimeter(): PerimeterAndArea {
            return PerimeterAndArea(perimeter - 1, area)
        }
    }

    fun solve1() {
        val nodes = parseNodes()

        var nextNode: Node? = nodes[0][0]
        val zones = mutableListOf<Pair<String, PerimeterAndArea>>()
        while (nextNode != null) {
            zones.add(Pair(nextNode.value, calculatePerimeterAndArea(nextNode.value, nextNode)))
            nextNode = nodes.firstOrNull { it.any { node -> !node.visited } }?.firstOrNull { !it.visited }
        }
        println(zones.sumOf { it.second.perimeter * it.second.area })
    }

    private fun parseNodes(): List<List<Node>> {
        val nodes = input.mapIndexed { y, i -> i.trim().split("").drop(1).dropLast(1).mapIndexed { x, j -> Node(x, y, j) } }
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
        return nodes
    }

    private fun calculatePerimeterAndArea(currentValue: String, node: Node?): PerimeterAndArea {
        if (node == null || node.value != currentValue) {
            return PerimeterAndArea(1, 0)
        }
        if (node.visited) {
            return PerimeterAndArea(0, 0)
        }
        node.visited = true
        return PerimeterAndArea(0, 1) +
                calculatePerimeterAndArea(currentValue, node.left) +
                calculatePerimeterAndArea(currentValue, node.right) +
                calculatePerimeterAndArea(currentValue, node.top) +
                calculatePerimeterAndArea(currentValue, node.bottom)
    }

    fun solve2() {
        val nodes = parseNodes()

        var nextNode: Node? = nodes[0][0]
        val zones = mutableListOf<Pair<String, PerimeterAndArea>>()
        while (nextNode != null) {
            zones.add(Pair(nextNode.value, calculateSidesAndArea(nextNode.value, nextNode)))
            nextNode = nodes.firstOrNull { it.any { node -> !node.visited } }?.firstOrNull { !it.visited }
        }
        println(zones.sumOf { it.second.perimeter * it.second.area })
    }

    private fun calculateSidesAndArea(currentValue: String, node: Node?): PerimeterAndArea {
        if (node == null || node.value != currentValue) {
            return PerimeterAndArea(1, 0)
        }
        if (node.visited) {
            return PerimeterAndArea(0, 0)
        }
        node.visited = true

        var current = PerimeterAndArea(0, 1)

        val left = calculateSidesAndArea(currentValue, node.left)
        val right = calculateSidesAndArea(currentValue, node.right)
        val top = calculateSidesAndArea(currentValue, node.top)
        val bottom = calculateSidesAndArea(currentValue, node.bottom)

        current += left
        current += right
        current += top
        current += bottom

        if (left == PerimeterAndArea(1, 0)) {
            if (node.bottom?.value == currentValue && node.bottom?.left?.value != currentValue) {
                current = current.reduceSameSidePerimeter()
            }
        }

        if (right == PerimeterAndArea(1, 0)) {
            if (node.top?.value == currentValue && node.top?.right?.value != currentValue) {
                current = current.reduceSameSidePerimeter()
            }
        }

        if (top == PerimeterAndArea(1, 0)) {
            if (node.left?.value == currentValue && node.left?.top?.value != currentValue) {
                current = current.reduceSameSidePerimeter()
            }
        }

        if (bottom == PerimeterAndArea(1, 0)) {
            if (node.right?.value == currentValue && node.right?.bottom?.value != currentValue) {
                current = current.reduceSameSidePerimeter()
            }
        }

        return current
    }
}