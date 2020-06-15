package dev.gumil.nookbot.telegram

import dev.gumil.nookbot.telegram.entities.Update
import dev.gumil.nookbot.telegram.exceptions.CommandParsingError
import org.junit.jupiter.api.Assertions.assertEquals

class FakeCommandRouter: CommandRouter {
    private var parsingError: CommandParsingError? = null
    private var routingUpdate: Triple<Update, String, String>? = null

    override suspend fun route(update: Update, command: String, content: String) {
        routingUpdate = Triple(update, command, content)
    }

    override fun onParsingError(error: CommandParsingError) {
        parsingError = error
    }

    fun <T: CommandParsingError> verifyError(clazz: Class<T>) {
        assertEquals(parsingError!!::class.java, clazz)
    }

    inline fun <reified T: CommandParsingError> verifyError() {
        verifyError(T::class.java)
    }

    fun verifyRouted(update: Update, command: String, content: String) {
        assertEquals(routingUpdate?.first, update)
        assertEquals(routingUpdate?.second, command)
        assertEquals(routingUpdate?.third, content)
    }

    fun tearDown() {
        parsingError = null
        routingUpdate = null
    }
}
