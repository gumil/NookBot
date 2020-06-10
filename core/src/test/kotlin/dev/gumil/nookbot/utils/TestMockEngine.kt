package dev.gumil.nookbot.utils

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.io.ByteArrayOutputStream

internal class TestMockEngine {
    private lateinit var mockResponse: MockResponse

    fun enqueueResponseFromFile(
        file: String,
        statusCode: HttpStatusCode = HttpStatusCode.OK
    ) {
        mockResponse = MockResponse(readFromFile(file), statusCode)
    }

    fun get() = MockEngine {
        respond(
            mockResponse.body,
            mockResponse.statusCode,
            headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
    }

    private fun readFromFile(file: String): String {
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

    private data class MockResponse(
        val body: String,
        val statusCode: HttpStatusCode
    )
}
