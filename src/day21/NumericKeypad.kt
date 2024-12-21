package day21

class NumericKeypad : Keypad {
    override val positionMap = mutableMapOf(
        "7" to Position(0, 0),
        "8" to Position(1, 0),
        "9" to Position(2, 0),
        "4" to Position(0, 1),
        "5" to Position(1, 1),
        "6" to Position(2, 1),
        "1" to Position(0, 2),
        "2" to Position(1, 2),
        "3" to Position(2, 2),
        "0" to Position(1, 3),
        "A" to Position(2, 3),
    )
    override val forbidden = Position(0, 3)
}