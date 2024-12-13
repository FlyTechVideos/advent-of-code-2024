package day13

import InputReader
import java.util.*

class Day13 {

    private val input = InputReader.readInput(13, test = false, split = false)[0]

    data class Machine(val targetX: Long, val targetY: Long, val buttonA: Button, val buttonB: Button) {
        companion object {
            fun from(input: String, targetOffset: Long): Machine {
                val inputRows = input.split(System.lineSeparator())
                val buttonAValues = inputRows[0].substring(12).split(", Y+").map { it.toLong() }
                val buttonBValues = inputRows[1].substring(12).split(", Y+").map { it.toLong() }
                val targetValues = inputRows[2].substring(9).split(", Y=").map { it.toLong() }

                return Machine(
                    targetX = targetValues[0] + targetOffset,
                    targetY = targetValues[1] + targetOffset,
                    buttonA = Button(buttonAValues[0], buttonAValues[1], 3),
                    buttonB = Button(buttonBValues[0], buttonBValues[1], 1)
                )
            }
        }
    }
    data class Button(val xIncrement: Long, val yIncrement: Long, val cost: Long)

    data class State(val x: Long, val y: Long, val cost: Long, val pathLength: Long) : Comparable<State> {
        override fun compareTo(other: State): Int = if (this.cost > other.cost) 1 else if (this.cost < other.cost) -1 else 0
    }

    fun solve1() {
        input.split("${System.lineSeparator()}${System.lineSeparator()}")
            .map { Machine.from(it, targetOffset = 0) }
            .sumOf { findCheapestWayNaivelyWithAPriorityQueue(it) }
            .let { println(it) }
    }

    private fun findCheapestWayNaivelyWithAPriorityQueue(machine: Machine): Long {
        val priorityQueue = PriorityQueue<State>()
        priorityQueue.add(State(0, 0, 0, 0))
        val visited = mutableSetOf<String>()

        while (priorityQueue.isNotEmpty()) {
            val (x, y, cost, pathLength) = priorityQueue.poll()
            val stateKey = "$x,$y"

            if (x == machine.targetX && y == machine.targetY) {
                return cost
            }

            if (visited.contains(stateKey)) {
                continue
            }
            if (x > machine.targetX || y > machine.targetY) {
                continue
            }

            visited.add(stateKey)

            val nextStates = listOf(
                State(x + machine.buttonA.xIncrement, y + machine.buttonA.yIncrement, cost + 3, pathLength + 1),
                State(x + machine.buttonB.xIncrement, y + machine.buttonB.yIncrement, cost + 1, pathLength + 1)
            )

            for (nextState in nextStates) {
                if (!visited.contains("${nextState.x},${nextState.y}")) {
                    priorityQueue.add(nextState)
                }
            }
        }

        return 0 // If the prize cannot be reached
    }


    fun solve2() {
        input.split("${System.lineSeparator()}${System.lineSeparator()}")
            .map { Machine.from(it, targetOffset = 10000000000000L) }
            .sumOf { calculateCostMathematicallyInsteadOfUsingBruteforce(it) }
            .let { println(it) }
    }

    private fun calculateCostMathematicallyInsteadOfUsingBruteforce(machine: Machine): Long {
        val xA = machine.buttonA.xIncrement
        val yA = machine.buttonA.yIncrement

        val xB = machine.buttonB.xIncrement
        val yB = machine.buttonB.yIncrement

        val x0 = machine.targetX
        val y0 = machine.targetY

        val a = (x0 * yB - y0 * xB).toDouble() / (xA * yB - xB * yA).toDouble()
        val aIsNotWhole = a % 1 != 0.0
        if (aIsNotWhole) {
            return 0
        }
        val b = (y0 - a * yA) / yB
        val bIsNotWhole = b % 1 != 0.0
        if (bIsNotWhole) {
            return 0
        }
        return 3 * a.toLong() + b.toLong()
    }
}