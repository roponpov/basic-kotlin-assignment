import assignments.ExerciseOne
import assignments.ExerciseThree
import assignments.ExerciseTwo

fun main() {
    val exerciseOne: ExerciseOne = ExerciseOne()
    val exerciseTwo: ExerciseTwo = ExerciseTwo()
    val exerciseThree: ExerciseThree = ExerciseThree()

    ////////////// CALL METHOD EXERCISE ONE //////////////
//    exerciseOne.createVariableUsingVarAndVal()

    ////////////// CALL METHOD EXERCISE TWO //////////////

        exerciseTwo.guestTheNumber(randomMaximum = 10)

        exerciseTwo.findMaximumNumber(x = 369,y= 963)

        val result = exerciseTwo.transformString(
            text = "Good Morning",
            operation = {
                it.lowercase()
            }
        )
        println(result)

        exerciseTwo.loadDataFromRemote { places ->
            places.map { place ->
                println(place.toString())
            }
        }

        val reversedText = exerciseTwo.reverseString("ROPON")
        println(reversedText)

        val luckyNumber :Int = exerciseTwo.calculateTotalSumNumber(arrayListOf(12,13,14)) // 39
        println(luckyNumber)

        val isSingle: Boolean = exerciseTwo.checkBirthdayIsSingle(12,9,2002)
        if (isSingle) println("You are single.") else println("You’re taken.")

    ////////////// CALL METHOD EXERCISE THREE //////////////
        exerciseThree.main()
}
