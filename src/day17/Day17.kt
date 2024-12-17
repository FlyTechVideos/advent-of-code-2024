package day17

import InputReader
import kotlin.math.max

class Day17 {

    private val test = false
    private val input = InputReader.readInput(17, test = test, split = false)[0]

    enum class OperandType {
        LITERAL,
        COMBO,
        IGNORE;
    }

    enum class Instruction(val operandType: OperandType) {
        // opcode = index
        ADV(OperandType.COMBO),
        BXL(OperandType.LITERAL),
        BST(OperandType.COMBO),
        JNZ(OperandType.LITERAL),
        BXC(OperandType.IGNORE),
        OUT(OperandType.COMBO),
        BDV(OperandType.COMBO),
        CDV(OperandType.COMBO);

        companion object {
            fun fromByte(byte: Byte): Instruction {
                return entries[byte.toInt()]
            }
        }
    }

    data class Computer(
        private var regA: Long,
        private var regB: Long,
        private var regC: Long,
        val program: List<Byte>,
        val output: MutableList<Long> = mutableListOf(),
        private var pointer: Int = 0,
    ) {
        companion object {
            fun fromInput(input: String): Computer {
                (
                        "Register A: (\\d+)\r?\n" +
                                "Register B: (\\d+)\r?\n" +
                                "Register C: (\\d+)\r?\n\r?\n" +
                                "Program: ([\\d,]+)"
                        ).toRegex().find(input)!!.let { str ->
                        val (regA, regB, regC, data) = str.destructured
                        return Computer(
                            regA.toLong(),
                            regB.toLong(),
                            regC.toLong(),
                            data.split(",").map { it.toByte() }
                        )
                    }
            }
        }

        override fun toString(): String {
            val regString = "A: $regA, B: $regB, C: $regC"
            val instrString = program.joinToString(",")
            val outputString = output.joinToString(",").ifEmpty { "No output" }

            val maxWidth = maxOf(regString.length, instrString.length, outputString.length)
            val topFrame = "┌" + "─".repeat(maxWidth + 2) + "┐\n"
            val bottomFrame = "└" + "─".repeat(maxWidth + 2) + "┘"
            val divider = "│${"─".repeat(maxWidth + 2)}│\n"

            return topFrame +
                    "│ $regString " + " ".repeat(maxWidth - regString.length) + "│\n" +
                    divider +
                    "│ $instrString " + " ".repeat(maxWidth - instrString.length) + "│\n" +
                    "│ " + " ".repeat(pointer * 2) + "^" + " ".repeat(max(maxWidth - pointer * 2, 0) ) + "│\n" +
                    divider +
                    "│ $outputString " + " ".repeat(maxWidth - outputString.length) + "│\n" +
                    bottomFrame
        }

        fun run(replaceRegA: Long = regA) {
            regA = replaceRegA
            while (hasNext()) {
                cycle()
            }
        }

        private fun hasNext(): Boolean {
            return pointer < program.size - 1
        }

        private fun cycle() {
            val instruction = Instruction.fromByte(program[pointer])
            val operand = when (instruction.operandType) {
                OperandType.LITERAL -> program[pointer + 1].toLong()
                OperandType.COMBO -> mapComboOperand(program[pointer + 1].toInt())
                else -> -1
            }

            when (instruction) {
                Instruction.ADV -> {
                    regA = regA shr operand.toInt()
                }
                Instruction.BXL -> {
                    regB = regB xor operand
                }
                Instruction.BST -> {
                    regB = operand and 0b111
                }
                Instruction.JNZ -> {
                    if (regA != 0L) {
                        pointer = operand.toInt()
                        return
                    }
                }
                Instruction.BXC -> {
                    regB = regB xor regC
                }
                Instruction.OUT -> {
                    output.add(operand and 0b111)
                }
                Instruction.BDV -> {
                    regB = regA shr operand.toInt()
                }
                Instruction.CDV -> {
                    regC = regA shr operand.toInt()
                }
            }
            pointer += 2
        }

        private fun mapComboOperand(operand: Int): Long {
            return when (operand) {
                0, 1, 2, 3 -> operand.toLong()
                4 -> regA
                5 -> regB
                6 -> regC
                7 -> throw IllegalArgumentException("RESERVED / Something special about 7 ?????")
                else -> throw IllegalArgumentException("Invalid operand $operand")
            }
        }
    }

    fun solve1() {
        val computer = Computer.fromInput(input)
        computer.run()
        println(computer.output.joinToString(","))
        if (!test) {
            assert(computer.output.joinToString(",") == "2,1,0,1,7,2,5,0,3")
            println("VERIFIED (\u2713)")
        }
    }

    fun solve2() {
        val computer = Computer.fromInput(input)
        val targetRegisterA = calculateRegisterARecursively(computer, "", 0)
        println(targetRegisterA)
        computer.run(targetRegisterA!!)
        println(computer.toString())
    }

    private fun calculateRegisterARecursively(computer: Computer, previousNumber: String, currentIndex: Int): Long? {
        if (currentIndex > computer.program.size) {
            return previousNumber.toLong(2)
        }

        val tryRange = (if (currentIndex == 0) 0..1023 else 0..7)

        for (i in tryRange) {
            val paddedCurrent = if (currentIndex == 0) i.toString(2).padStart(8, '0') else i.toString(2).padStart(3, '0')
            val currentNumber = previousNumber + paddedCurrent
            if (!numberComputesToTarget(currentNumber.toLong(2), currentIndex)) {
                continue
            }

            val nextLevel = calculateRegisterARecursively(computer, currentNumber, currentIndex + 1) ?: continue
            return nextLevel
        }

        return null
    }

    private fun numberComputesToTarget(number: Long, currentIndex: Int): Boolean {
        val computer = Computer.fromInput(input)
        computer.run(number)
        return computer.output.size >= currentIndex && computer.output.map { it.toByte() } == computer.program.takeLast(computer.output.size)
    }

}
