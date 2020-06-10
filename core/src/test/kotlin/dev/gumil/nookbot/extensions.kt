package dev.gumil.nookbot

import kotlinx.io.ByteArrayOutputStream

internal fun Any.readFromFile(file: String): String {
    val inputStream = this.javaClass.classLoader!!.getResourceAsStream(file)
        ?: throw IllegalStateException()

    val result = ByteArrayOutputStream()
    val buffer = ByteArray(1024)
    var length = inputStream.read(buffer)
    while (length != -1) {
        result.write(buffer, 0, length)
        length = inputStream.read(buffer)
    }
    val resultString = result.toString("UTF-8")
    inputStream.close()
    result.close()
    return resultString
}
