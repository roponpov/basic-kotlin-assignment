package assignments

/**
 * 1. Create variable using var and val keyword and have specific data type as below
 *  - Byte
 *  - Short
 *  - Int
 *  - Long
 *  - Float
 *  - Double
 *  - Char
 *  - String
 */
class ExerciseOne {

    /**
     * Demonstrates how to declare variables in Kotlin using `val`.
     *
     * This method:
     * - Declares variables for every common Kotlin data type (Byte, Short, Int, Long, Float, Double, Char, String).
     * - Shows how to store literal values inside each type correctly.
     * - Combines all variables into a formatted multiline string using string interpolation.
     * - Prints the final merged result to the console.
     *
     * Basically… it’s a quick flex of Kotlin's primitive and reference types in one function.
     */
    fun createVariableUsingVarAndVal(){
        val variableTypeByte: Byte = 123
        val variableTypeShort: Short = 32767
        val variableTypeInt: Int = 2147483647
        val variableTypeLong: Long = 9223372036854775807
        val variableTypeFloat: Float = 1.68f
        val variableTypeDouble: Double = 3.14
        val variableTypeChar: Char = 'A'
        val variableTypeString: String = "Morning"

        val mergeAsString: String = """
        - Byte   : $variableTypeByte
        - Short  : $variableTypeShort
        - Int    : $variableTypeInt
        - Long   : $variableTypeLong
        - Float  : $variableTypeFloat
        - Double : $variableTypeDouble
        - Char   : $variableTypeChar
        - String : $variableTypeString
    """.trimIndent()

        println(mergeAsString)
    }
}