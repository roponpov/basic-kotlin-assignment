package models

import utils.getValueOf

class PlaceModel(
    val id: Int,
    var placeName: String,
    var address: String,
    var distance: Int,
    var description: String,
) {
    private val reset = "\u001B[0m"
    private val green = "\u001B[32m"

    override fun toString(): String {
        val shortDesc = if (description.length > 100) {
            description.take(100) + "..."
        } else {
            description
        }

        return """
            • Place
              ├─ Id: ${green}$id$reset
              ├─ Name: ${green}$placeName$reset
              ├─ Address: ${green}$address$reset
              ├─ Distance: ${green}${distance}m$reset
              └─ Description: ${green}$shortDesc$reset
              
        """.trimIndent()
    }

    companion object {
        fun fromJson(json: String): PlaceModel {
            return PlaceModel(
                id = json.getValueOf("id").toInt(),
                placeName = json.getValueOf("placeName"),
                address = json.getValueOf("address"),
                distance = json.getValueOf("distance").toInt(),
                description = json.getValueOf("description"),
            )
        }
    }
}