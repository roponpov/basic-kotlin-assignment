package utils

fun String.getValueOf(key: String): String {
    val pattern = """"$key"\s*:\s*"(.*?)"""".toRegex()
    val match = pattern.find(this)
    if (match != null) return match.groupValues[1]

    val patternNum = """"$key"\s*:\s*([^,\}\s]+)""".toRegex()
    val matchNum = patternNum.find(this)
    if (matchNum != null) return matchNum.groupValues[1]

    return ""
}

fun String.rawObjectOf(key: String): String {
    val startKey = this.indexOf(""""$key"""")
    if (startKey == -1) return ""

    val startBrace = this.indexOf('{', startKey)
    var braceCount = 0
    for (i in startBrace..<this.length) {
        if (this[i] == '{') braceCount++
        if (this[i] == '}') braceCount--
        if (braceCount == 0) {
            return this.substring(startBrace, i + 1)
        }
    }
    return ""
}

inline fun <T> String.getObjectOf(key: String, mapper: (String) -> T): T {
    val raw = this.rawObjectOf(key)
    return mapper(raw)
}

fun String.splitTopLevelObjects(): List<String> {
    val result = mutableListOf<String>()
    var brace = 0
    var start = -1

    for (i in indices) {
        val c = this[i]
        if (c == '{') {
            if (brace == 0) start = i
            brace++
        } else if (c == '}') {
            brace--
            if (brace == 0 && start != -1) {
                result.add(this.substring(start, i + 1))
                start = -1
            }
        }
    }
    return result
}

fun String.rawArrayOf(key: String): String {
    val startKey = this.indexOf(""""$key"""")
    if (startKey == -1) return ""

    val startBracket = this.indexOf('[', startKey)
    var bracketCount = 0
    for (i in startBracket..<this.length) {
        if (this[i] == '[') bracketCount++
        if (this[i] == ']') bracketCount--
        if (bracketCount == 0) {
            return this.substring(startBracket, i + 1)
        }
    }
    return ""
}
