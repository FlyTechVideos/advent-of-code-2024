package day21

import InputReader

class Day21 {

    private val input = InputReader.readInput(21, test = false)
    private val cacheForPart2 = mutableMapOf<Pair<String, Int>, Long>()

    fun solve1() {
        val complexity = calculateComplexityNumber()
        println(complexity)
    }

    private fun calculateComplexityNumber(): Int {
        var keypadChain: Keypad = NumericKeypad()
        repeat(2) {
            keypadChain = DirectionalKeypad(keypadChain)
        }
        return input.mapIndexed { index, line ->
            val predecessor = if (index == 0) 'A' else input[index - 1].last()
            val string = (predecessor + line).windowed(2).joinToString("") {
                keypadChain.calculatePath(it[0].toString(), it[1].toString())
            }
            Pair(line, string)
        }.sumOf {
            println("${it.first}: ${it.second} (len: ${it.second.length})")
            it.first.dropLast(1).toInt() * it.second.length
        }
    }

    fun solve2() {
        // Part 1 could be solved with this as well, I keep the code above just to visualize my first approach
        println("#######\nSanity check: 126384 for test, 197560 for real")
        println("and the value is ${input.sumOf { line -> calculateComplexity(line, maxDepth = 3) }}")
        // at first I didn't clear, was surprised to learn that 197560 was the solution for part 2 as well :))
        cacheForPart2.clear()
        println("Now for real:")
        println(input.sumOf { line -> calculateComplexity(line, maxDepth = 26) })
    }

    private fun calculateComplexity(line: String, maxDepth: Int): Long {
        val length = calculateComplexity(line.dropLast(1), 0, maxDepth, NumericKeypad())
        return line.dropLast(1).toInt() * length
    }

    private fun calculateComplexity(line: String, depth: Int, maxDepth: Int, keypad: Keypad = StandaloneDirectionalKeypad()): Long {
        if (line.isEmpty() || depth == maxDepth) {
            return line.length + 1L
        }

        if (cacheForPart2.containsKey(Pair(line, depth))) {
            return cacheForPart2[Pair(line, depth)]!!
        }

        val nextIteration = ("A${line}A").windowed(2).joinToString("") {
            keypad.calculatePath(it[0].toString(), it[1].toString())
        }
        val value = nextIteration.dropLast(1).split("A")
            .sumOf {
                calculateComplexity(it, depth + 1, maxDepth)
            }
        cacheForPart2[Pair(line, depth)] = value
        return value
    }
}
