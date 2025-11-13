package assignments

import models.ApiResponseModel
import models.PlaceModel
import repositories.PlaceRepository
import kotlin.random.Random

/**
 * 2. Create function and call that function to use in Kotlin follow instruction below
 *  - Function declaration
 *  - Function contain single parameter
 *  - Function contain multiple parameter
 *  - Lambda function (also add parameter)
 *  - Function contain multiple parameter as function (parameter as Unit)
 *  - Function return type (String, Int, Boolean)
 */
class ExerciseTwo {

    /**
     * Let the user guess a random number within the given max range.
     * Gives 5 attempts and drops random roast messages when wrong 😂
    */
    fun guestTheNumber(randomMaximum: Int){
        val green = "\u001B[32m"
        val red = "\u001B[31m"
        val reset = "\u001B[0m"

        val wrongGuessMessages = listOf(
            "Nope! Try again, that’s not it 😅",
            "Close… but not quite. Wanna give it another shot?",
            "Wrong guess! The number’s still hiding 👀",
            "Almost there, but not the right one!",
            "That ain’t it chief 😬",
            "Try again, you’re getting warmer 🔥",
            "No luck this time — think smaller or bigger?",
            "Oops, not correct! You can do better 😎",
            "Missed it! The number’s playing hard to get 🤫",
            "Wrong again, but hey, never give up 💪"
        )

        val random = Random.Default.nextInt(randomMaximum)
        val chances: Int = 5
        var userChances: Int = 0

        while (chances > userChances){
            print(" => (Attempt : ${userChances+1}/$chances) Guest the number : ")

            val userInput = readln().toInt()
            if(random == userInput){
                println("${green}Congratulation your answer ($userInput) are correctly.${reset}")
                break
            }
            val messageRandom = Random.Default.nextInt(wrongGuessMessages.count())
            println("${red}${wrongGuessMessages[messageRandom]}${reset}")

            userChances++
        }
    }

    /**
     * Compare two numbers and print which one is larger, smaller, or equal.
     */
    fun findMaximumNumber(x: Int,y: Int){
        if(x > y){
            println("$x is greater than $y")
        } else if (x == y){
            println("The both value was equals.")
        } else {
            println("$x is less than $y")
        }
    }

    /**
     * Transform a string using a provided lambda operation.
     * Basically lets you plug in any text-modifier function.
     */
    fun transformString(text: String,operation: (text: String) -> String) : String {
        return operation(text)
    }

    /**
     * Fetch remote place data and return it using a callback (lambda).
     * Feels async but still all local.
     */
    fun loadDataFromRemote(onResponse: (places: List<PlaceModel>) -> Unit){
        val placeRepository: PlaceRepository = PlaceRepository()
        val response: ApiResponseModel<List<PlaceModel>> = placeRepository.fetchPlace()
        onResponse(response.data)
    }

    /**
     * Reverse a string manually without fancy functions.
     * Just pure old-school looping.
     */
    fun reverseString(text: String): String {
        val stringBuilder: StringBuilder = StringBuilder()
        for(i in text.length - 1 downTo 0) {
            stringBuilder.append(text[i])
        }
        return stringBuilder.toString()
    }

    /**
     * Sum all numbers in a list and return the total.
     */
    fun calculateTotalSumNumber(numbers: List<Int>) : Int {
        var totalSum: Int = 0
        numbers.map {
            totalSum += it
        }
        return totalSum
    }

    /**
     * Combine birthday digits and pick a random digit to decide “single” or not 😭
     * Returns true if the chosen digit is odd.
     */
    fun checkBirthdayIsSingle(day: Int,month: Int,year: Int) : Boolean {
        val mergeBirthday : Int = day + month + year
        val listNumbers = mergeBirthday.toString().split("").filter { it.isNotBlank() && it.isNotEmpty() }
        val random = Random.Default.nextInt(listNumbers.count())
        return listNumbers[random].toInt() % 2 != 1
    }
}