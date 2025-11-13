package utils

import models.PlaceModel
import java.io.File

class HtmlConverter {
    private fun String.escapeHtml(): String =
        this.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;")

    private fun buildPlacesHtml(places: List<PlaceModel>): String {
        val rows = places.joinToString("\n") { place ->
            """
        <tr>
            <td>${place.id}</td>
            <td>${place.placeName.escapeHtml()}</td>
            <td>${place.address.escapeHtml()}</td>
            <td>${place.distance} m</td>
            <td>${place.description.escapeHtml()}</td>
        </tr>
        """.trimIndent()
        }

        return """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <title>Place Report</title>
            <style>
                body {
                    font-family: system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif;
                    background: #f3f4f6;
                    margin: 0;
                    padding: 20px;
                }
                .container {
                    max-width: 1200px;
                    margin: 0 auto;
                    background: #ffffff;
                    padding: 24px;
                    border-radius: 12px;
                    box-shadow: 0 10px 25px rgba(0, 0, 0, 0.08);
                }
                h1 {
                    text-align: center;
                    margin-top: 0;
                    margin-bottom: 8px;
                    font-size: 28px;
                    letter-spacing: 0.05em;
                }
                .subtitle {
                    text-align: center;
                    margin-top: 0;
                    margin-bottom: 24px;
                    color: #6b7280;
                    font-size: 14px;
                }
                table {
                    width: 100%;
                    border-collapse: collapse;
                    table-layout: fixed;
                }
                thead {
                    background: linear-gradient(90deg, #2563eb, #4f46e5);
                    color: white;
                }
                th, td {
                    padding: 10px 12px;
                    border-bottom: 1px solid #e5e7eb;
                    vertical-align: top;
                    font-size: 14px;
                    word-wrap: break-word;
                    word-break: break-word;
                }
                th {
                    text-align: left;
                    font-weight: 600;
                }
                tr:nth-child(even) tbody {
                    background-color: #f9fafb;
                }
                tbody tr:nth-child(even) {
                    background-color: #f9fafb;
                }
                tbody tr:hover {
                    background-color: #eef2ff;
                }
                .id-col {
                    width: 60px;
                    text-align: center;
                }
                .name-col {
                    width: 180px;
                }
                .address-col {
                    width: 260px;
                }
                .distance-col {
                    width: 90px;
                    text-align: right;
                    white-space: nowrap;
                }
                .description-col {
                    width: auto;
                }
                .badge-count {
                    display: inline-block;
                    padding: 4px 8px;
                    border-radius: 999px;
                    background: #eef2ff;
                    color: #3730a3;
                    font-size: 11px;
                    font-weight: 600;
                    margin-left: 4px;
                }
                .footer-note {
                    margin-top: 16px;
                    font-size: 11px;
                    color: #9ca3af;
                    text-align: right;
                }
            </style>
        </head>
        <body>
            <div class="container">
                <h1>Place Report</h1>
                <p class="subtitle">
                    Exported ${places.size} place(s)
                    <span class="badge-count">TOTAL: ${places.size}</span>
                </p>
                <table>
                    <thead>
                        <tr>
                            <th class="id-col">ID</th>
                            <th class="name-col">Place Name</th>
                            <th class="address-col">Address</th>
                            <th class="distance-col">Distance</th>
                            <th class="description-col">Description</th>
                        </tr>
                    </thead>
                    <tbody>
                        $rows
                    </tbody>
                </table>
                <div class="footer-note">
                    Developed by Pov Ropon
                </div>
            </div>
        </body>
        </html>
    """.trimIndent()
    }

    private fun getReportsPath(fileName: String = "places.html"): String {
        val folder = File("src/reports")

        if (!folder.exists()) {
            folder.mkdirs()   // create directories if not exist
        }

        return folder.resolve(fileName).absolutePath
    }

    fun exportPlacesToHtml(places: List<PlaceModel>) {
        println("Please enter file name")
        print(" ( Default Name : places.html ) => ")

        val rawInput = readln().trim()

        // 1) Decide base name (without extension)
        val baseName = if (rawInput.isBlank()) {
            "places"
        } else {
            // remove illegal characters for file names
            val invalidChars = charArrayOf('\\', '/', ':', '*', '?', '"', '<', '>', '|')
            val cleaned = rawInput
                .substringBeforeLast('.') // drop any extension user typed
                .filterNot { it in invalidChars }
                .ifBlank { "places" }     // fallback if everything got removed

            cleaned
        }

        // 2) Final file name with .html extension
        val finalFileName = "$baseName.html"

        // 3) Build full path (src/reports/<name>.html)
        val savePath = getReportsPath(finalFileName)

        // 4) Build HTML + write file safely
        val html = buildPlacesHtml(places)
        try {
            File(savePath).writeText(html, Charsets.UTF_8)
            println("✅ Exported ${places.size} places to:")
            println(savePath)
        } catch (e: Exception) {
            println("❌ Failed to export file:")
            println("   ${e.message}")
        }
    }
}