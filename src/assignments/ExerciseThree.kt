package assignments

import models.ApiResponseModel
import models.PlaceModel
import repositories.PlaceRepository
import utils.ConsoleTool
import utils.HtmlConverter
import kotlin.concurrent.thread
import kotlin.system.exitProcess

/**
 * 3.	Create class in Kotlin
 * -	Any class (at least 5 class)
 * -	Class contain 5 data member and 5 methods
 * -	Class contain constructor method
 * -	Class contain initialize block
 * -	Instance class (at least 15)
 * -	Extend class from another class (at least 5)
 * -	Add multi line comment on the top of any method
 */
class ExerciseThree {
    private var places: List<PlaceModel> = emptyList()

    init {
        places = loadData()
    }

    /**
     * Shows the main menu of the Places console app and handles user navigation.
     *
     * Flow:
     * - Clears the console for a clean UI.
     * - Displays the main menu options (view, search, update, delete, export, exit).
     * - Reads the user's choice and routes to the matching function.
     * - If the input is invalid, it recalls `main()` to re-show the menu.
     * - After handling an action, it calls `exitOrBackToMenu()` to let users continue or exit.
     */
    fun main(){
        ConsoleTool.clearScreenFake()

        val listMenu = """
        ╔═════════════════════════════╗
        ║          MAIN MENU          ║
        ╚═════════════════════════════╝
         [1]  View All Places
         [2]  Search Place By Name
         [3]  Update Place By ID
         [4]  Delete Place By ID
         [5]  Export Places To HTML
         [6]  Exit
     
        """.trimIndent()
        println(listMenu)
        print(" => Choose an option: ")
        val userInput: Int? = readln().toIntOrNull()

        when(userInput) {
            1 -> {
                viewAllPlaces()
            }
            2 -> {
                searchPlaceByName()
            }
            3 -> {
                updatePlaceById()
            }
            4 -> {
                deletePlaceById()
            }
            5 -> {
                exportPlacesToHTML()
            }
            6 -> {
                exitProcess(0)
            }

            else -> {
                main()
            }
        }
        exitOrBackToMenu()
    }

    /**
     * Loads all places from the repository on startup.
     *
     * Behavior:
     * - Starts a loading spinner in a background thread while fetching data.
     * - Calls `PlaceRepository.fetchPlace()` to get data from the API.
     * - Stops the loading spinner once the response is received.
     * - If the response status indicates an error:
     *      • Asks the user whether to retry (Y) or exit (N).
     *      • Retries recursively on Y.
     *      • Exits the application on N.
     *
     * @return List of [PlaceModel] loaded from the API.
     */
    private fun loadData(): List<PlaceModel> {
        val placeRepository = PlaceRepository()

        val loadingThread = thread(start = true, isDaemon = true) {
            ConsoleTool.showLoading("Fetching data")
        }

        val response: ApiResponseModel<List<PlaceModel>> = try {
            placeRepository.fetchPlace()
        } finally {
            loadingThread.interrupt()
        }

        println("Fetching data... done.")

        if (response.status.contains("error", ignoreCase = true)) {
            while (true) {
                println("Do you want to retry fetching data? (Y/N)")
                print(" => ")

                val userInput = readln().trim()

                when {
                    userInput.equals("Y", ignoreCase = true) ->
                        return loadData()

                    userInput.equals("N", ignoreCase = true) -> {
                        println("Exiting application...")
                        exitProcess(0)
                    }

                    else -> {
                        ConsoleTool.clearScreenFake()
                        println("Invalid input. Please enter Y or N.\n")
                    }
                }
            }
        }

        return response.data
    }

    /**
     * Prints all places currently loaded into memory.
     *
     * Behavior:
     * - Iterates through the `places` list.
     * - Prints each place using its `toString()` representation.
     *
     * This is used by the "View All Places" option in the main menu.
     */
    private fun viewAllPlaces() {
        places.map {
            println(it.toString())
        }
    }

    /**
     * Searches for a place by its name and displays the first match.
     *
     * Flow:
     * - Clears the console.
     * - Prompts the user to enter a place name.
     * - If input is blank/empty, it recalls itself (forces user to input again).
     * - Scans `places` and prints the first result where `placeName` contains the search text (case-insensitive).
     * - If nothing is found, shows a "PLACE NOT FOUND" message.
     */
    private fun searchPlaceByName() {
        ConsoleTool.clearScreenFake()
        print(" => Search here (Place Name) : ")
        val searchText: String = readln().trim()

        if(searchText.isNotBlank() && searchText.isNotEmpty()) {
            places.map {
                if(it.placeName.contains(searchText, ignoreCase = true)){
                    println(it.toString())
                    return
                }
            }
        } else {
            searchPlaceByName()
            return
        }
        println("""
            ╔═══════════════════════╗
            ║    PLACE NOT FOUND    ║
            ╚═══════════════════════╝
        """.trimIndent())

    }

    /**
     * Asks the user whether they want to exit the app or return to the main menu.
     *
     * Flow:
     * - Prints options: (0) Exit, (1) Back to menu.
     * - Reads user input using `toIntOrNull()` to avoid crashes.
     * - On:
     *      • 0 → exits the process.
     *      • 1 → calls `main()` to show the menu again.
     *      • else → clears screen, shows error, and asks again recursively.
     */
    private fun exitOrBackToMenu() {
        println("\n (0) Exit, (1) Back to menu")
        print(" => Enter your choice : ")

        val userInput = readln().toIntOrNull()

        when (userInput) {
            0 -> exitProcess(0)
            1 -> main()
            else -> {
                ConsoleTool.clearScreenFake()
                println("Wrong input, please input again !!!")
                exitOrBackToMenu()
            }
        }
    }

    /**
     * Updates an existing place based on its ID.
     *
     * Flow:
     * - Prompts the user to enter the Place ID to update.
     * - Validates the ID (must be a number).
     * - Searches the `places` list for a matching ID.
     * - If not found:
     *      • Clears screen.
     *      • Shows "No Place Found" message.
     *      • Recursively calls itself to let user try again.
     * - If found:
     *      • Shows current place info.
     *      • Asks for new values field by field (name, address, distance, description).
     *      • If user input is blank, keeps the old value.
     *      • Prints a confirmation banner once updated.
     */
    private fun updatePlaceById() {
        print(" => Enter the id to update (Place ID): ")
        val userInput = readln().toIntOrNull()

        if (userInput == null) {
            ConsoleTool.clearScreenFake()
            println("Invalid ID, please enter a number.")
            updatePlaceById()
            return
        }

        val place = places.find { it.id == userInput }

        if (place == null) {
            ConsoleTool.clearScreenFake()
            println("""
                ╔══════════════════════╗
                ║    No Place Found    ║
                ╚══════════════════════╝
            """.trimIndent())

            updatePlaceById()
            return
        }

        ConsoleTool.clearScreenFake()
        println("Please update your information one by one (Blank = keep old data)")
        println("\n$place")

        print(" => Place Name (New) : ")
        readln().takeIf { it.isNotBlank() }?.let { place.placeName = it }

        print(" => Address (New) : ")
        readln().takeIf { it.isNotBlank() }?.let { place.address = it }

        print(" => Distance (New) : ")
        readln().toIntOrNull()?.let { place.distance = it }

        print(" => Description (New) : ")
        readln().takeIf { it.isNotBlank() }?.let { place.description = it }

        println("""
            ╔═══════════════════════════════════╗
            ║    Place Updated Successfully.    ║
            ╚═══════════════════════════════════╝
        """.trimIndent())
    }

    /**
     * Deletes a place by its ID after user confirmation.
     *
     * Flow:
     * - Clears the screen and asks for the Place ID to delete.
     * - Validates that the ID is numeric.
     * - Searches the `places` list for the matching place.
     * - If not found, prints a "PLACE NOT FOUND" banner.
     * - If found:
     *      • Prints the place details.
     *      • Asks the user to confirm deletion (Y/N).
     *      • On Y → removes the place from the list and shows success banner.
     *      • On N → prints "Cancelled" and returns.
     *      • On invalid input → keeps asking until valid.
     */
    private fun deletePlaceById() {
        ConsoleTool.clearScreenFake()
        print(" => Enter the ID : ")
        val userInput = readln().toIntOrNull()

        if (userInput == null) {
            println("Invalid ID, please enter a number.\n")
            deletePlaceById()
            return
        }

        val place = places.find { it.id == userInput }

        if (place == null) {
            println("""
            ╔═══════════════════════╗
            ║    PLACE NOT FOUND    ║
            ╚═══════════════════════╝
        """.trimIndent())
            return
        }

        println(place.toString())

        while (true) {
            print("Are you sure you want to delete? (Y/N) : ")
            val userDecide = readln().trim()

            when {
                userDecide.equals("Y", ignoreCase = true) -> {
                    places = places.filter { it.id != place.id }
                    println("""
                        ╔═════════════════════════════╗
                        ║    Deleted Successfully!    ║
                        ╚═════════════════════════════╝
                    """.trimIndent())
                    return
                }

                userDecide.equals("N", ignoreCase = true) -> {
                    println("""
                        ╔═════════════════╗
                        ║    Cancelled    ║
                        ╚═════════════════╝
                    """.trimIndent())
                    return
                }

                else -> {
                    println("❌ Invalid input. Please enter Y or N.")
                }
            }
        }
    }

    /**
     * Exports the current list of places to an HTML file.
     *
     * Behavior:
     * - Creates an instance of [HtmlConverter].
     * - Passes the current `places` list to `exportPlacesToHtml()`.
     *
     * The actual HTML building and file writing logic is handled inside `HtmlConverter`.
     */
    private fun exportPlacesToHTML() {
        val htmlConverter: HtmlConverter = HtmlConverter()
        htmlConverter.exportPlacesToHtml(places = places)
    }

}