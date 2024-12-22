package day22

import InputReader

class Day22 {

    private val input = InputReader.readIntLineInput(22, test = false)

    data class Sequence(
        val v1: Long,
        val v2: Long,
        val v3: Long,
        val v4: Long,
    ) {
        override fun toString(): String {
            return "Sequence($v1,$v2,$v3,$v4)"
        }

        companion object {
            fun from(list: List<Long>): Pair<Sequence, Long> {
                return Pair(
                    Sequence(
                        list[1] - list[0],
                        list[2] - list[1],
                        list[3] - list[2],
                        list[4] - list[3],
                    ),
                    list[4]
                )
            }
        }
    }

    fun solve1() {
        println(input.sumOf { followSecretSequence(it).last() })
    }

    private fun followSecretSequence(initial: Int, retainOnlyLastDigit: Boolean = false): List<Long> {
        val sequenceEvolutions = mutableListOf<Long>()
        var secret: Long = initial.toLong()
        for (i in 0..<2000) {
            val r1 = secret * 64
            secret = mixInto(r1, secret)
            secret = prune(secret)

            val r2 = secret / 32
            secret = mixInto(r2, secret)
            secret = prune(secret)

            val r3 = secret * 2048
            secret = mixInto(r3, secret)
            secret = prune(secret)

            sequenceEvolutions.add(secret)
        }
        return sequenceEvolutions.map {
            if (retainOnlyLastDigit) it % 10 else it
        }
    }

    private fun prune(r: Long): Long {
        return r % 16777216
    }

    private fun mixInto(r3: Long, secret: Long): Long {
        return r3 xor secret
    }

    fun solve2() {
        val sequences = input.associateWith { followSecretSequence(it, retainOnlyLastDigit = true) }
        val sequenceCounter = mutableMapOf<Sequence, Long>()
        sequences.forEach { (startingValue, list) ->
            val extendedList = listOf(startingValue.toLong() % 10) + list
            val localSequenceCounter = mutableMapOf<Sequence, Long>()
            extendedList.windowed(5).forEach {
                val (sequence, price) = Sequence.from(it)
                if (sequence !in localSequenceCounter) {
                    localSequenceCounter[sequence] = price
                }
            }
            localSequenceCounter.forEach { (sequence, value) ->
                sequenceCounter[sequence] = (sequenceCounter[sequence] ?: 0) + value
            }
        }
        println(sequenceCounter.values.max())
    }

}
