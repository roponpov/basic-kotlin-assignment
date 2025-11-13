package utils

class ConsoleTool {
    companion object {
        fun showLoading(message: String = "Loading") {
            val frames = listOf("|", "/", "-", "\\")
            var index = 0

            try {
                while (!Thread.currentThread().isInterrupted) {
                    val frame = frames[index % frames.size]
                    print("\r$message $frame")
                    Thread.sleep(120)
                    index++
                }
            } catch (_: InterruptedException) {
                // Spinner was interrupted while sleeping -> just exit quietly
            } finally {
                // Optional: clear the line when stopping
                print("\r${" ".repeat(message.length + 4)}\r")
            }
        }
        fun clearScreenFake() {
            repeat(60) { println() }  // push everything up out of view
        }
    }

}