package services

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URI

enum class HttpMethod {
    GET,
    POST,
    PUT,
    UPDATE,
    DELETE;

    override fun toString(): String {
        return when (this) {
            GET -> "GET"
            POST -> "POST"
            PUT -> "PUT"
            UPDATE -> "UPDATE"
            DELETE -> "DELETE"
        }
    }

}

object ApiService {
    private const val BASE_URL = "https://roponpov.pythonanywhere.com/api/"
    private fun buildConnection(
        endpoint: String,
        httpMethod: String
    ): HttpURLConnection {
        val url = URI.create(BASE_URL + endpoint).toURL()
        val  connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = httpMethod
        connection.connectTimeout = 5000
        connection.readTimeout = 5000
        connection.setRequestProperty("Content-Type", "application/json")
        connection.setRequestProperty("Accept", "application/json")
        return connection
    }

    fun get(endpoint: String): String {
        val connection = buildConnection(
            endpoint = endpoint,
            httpMethod = HttpMethod.GET.toString(),
        )

        return try {
            val responseCode = connection.responseCode
            val reader = if (responseCode == 200)
                BufferedReader(InputStreamReader(connection.inputStream))
            else
                BufferedReader(InputStreamReader(connection.errorStream))

            reader.use { it.readText() }

        } finally {
            connection.disconnect()
        }
    }
}