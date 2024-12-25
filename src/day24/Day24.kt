package day24

import InputReader

class Day24 {

    private val input = InputReader.readInput(24, test = false, split = false)[0]

    enum class OperationType {
        AND, OR, XOR;

        fun operate(o1: Boolean, o2: Boolean): Boolean {
            return when (this) {
                AND -> o1 && o2
                OR -> o1 || o2
                XOR -> o1 xor o2
            }
        }
    }

    private data class Operation(
        val wire1: String,
        val wire2: String,
        val target: String,
        val operationType: OperationType,
        private val numberMap: MutableMap<String, Boolean>,
    ) {
        fun execute(): Boolean {
            val o1 = numberMap[wire1] ?: return false
            val o2 = numberMap[wire2] ?: return false
            numberMap[target] = operationType.operate(o1, o2)
            return true
        }
    }

    fun solve1() {
        val numberMap = mutableMapOf<String, Boolean>()
        val operations = parseOperations(numberMap)
        executeOperations(operations)

        val result = convertOutputWiresToNumber(numberMap)
        println(result)
    }

    private fun parseOperations(numberMap: MutableMap<String, Boolean>): List<Operation> {
        val parts = input.trim().split("\n\n")

        parts[0].split("\n")
            .map { it.split(": ") }
            .forEach { numberMap[it[0]] = it[1] == "1" }

        val operations = parts[1].split("\n")
            .map {
                val split = it.split(" ")
                Operation(
                    split[0],
                    split[2],
                    split[4],
                    OperationType.valueOf(split[1].uppercase()),
                    numberMap,
                )
            }
        return operations
    }

    private fun convertOutputWiresToNumber(numberMap: Map<String, Boolean>) =
        numberMap.filter { it.key.startsWith("z") }
            .entries
            .sortedByDescending { it.key }
            .joinToString("") { if (it.value) "1" else "0" }
            .toLong(2)

    private fun executeOperations(operations: List<Operation>) {
        val operationsQueue = operations.toMutableList()
        while (operationsQueue.isNotEmpty()) {
            val operation = operationsQueue.removeFirst()
            if (!operation.execute()) {
                operationsQueue.add(operation)
            }
        }
    }

    fun solve2() {
        // I did something based on staring at debugger values though I have no idea
        // if this code is working generally or just for my input. Does not matter...

        val operations = parseOperations(mutableMapOf())
        // First gate manually confirmed to be correct
        val carryOutOfFirstGate = findOperation(operations, "x00", "y00", OperationType.AND)!!.target
        var lastCarryOut = carryOutOfFirstGate

        val invalidOutputs = mutableSetOf<String>()
        for (i in 1..44) {
            val xWire = "x${i.toString().padStart(2, '0')}"
            val yWire = "y${i.toString().padStart(2, '0')}"
            val expectedTargetWire = "z${i.toString().padStart(2, '0')}"

            var xor1 = findOperation(operations, xWire, yWire, OperationType.XOR)!!
            var and1 = findOperation(operations, xWire, yWire, OperationType.AND)!!

            val xor2 = findOperation(operations, xor1.target, lastCarryOut, OperationType.XOR)
            var and2 = findOperation(operations, xor1.target, lastCarryOut, OperationType.AND)

            var or = findOperation(operations, and1.target, and2?.target, OperationType.OR)

            if (xor2 == null && and2 == null && or == null) {
                invalidOutputs.add(xor1.target)
                invalidOutputs.add(and1.target)

                xor1 = findOperation(operations, xWire, yWire, OperationType.AND)!!
                and1 = findOperation(operations, xWire, yWire, OperationType.XOR)!!

                and2 = findOperation(operations, xor1.target, lastCarryOut, OperationType.AND)!!
                or = findOperation(operations, and1.target, and2.target, OperationType.OR)
            } else if (xor2?.target != expectedTargetWire) {
                if (or?.target == expectedTargetWire) {
                    invalidOutputs.add(xor2!!.target)
                    invalidOutputs.add(or.target)

                    or = xor2
                } else if (and1.target == expectedTargetWire) {
                    invalidOutputs.add(xor2!!.target)
                    invalidOutputs.add(and1.target)

                    or = findOperation(operations, xor2.target, and2?.target, OperationType.OR)
                } else if (and2?.target == expectedTargetWire) {
                    invalidOutputs.add(xor2!!.target)
                    invalidOutputs.add(and2.target)

                    or = findOperation(operations, xor2.target, and1.target, OperationType.OR)
                }
            }

            lastCarryOut = or!!.target
        }
        println(invalidOutputs.sorted().joinToString(","))
    }

    private fun findOperation(operations: List<Operation>, xWire: String?, yWire: String?, operationType: OperationType): Operation? {
        if (xWire == null || yWire == null) {
            return null
        }

        val expectedList = listOf(xWire, yWire)
        return operations.find {
            expectedList.contains(it.wire1) &&
                    expectedList.contains(it.wire2) &&
                    it.operationType == operationType
        }
    }
}
