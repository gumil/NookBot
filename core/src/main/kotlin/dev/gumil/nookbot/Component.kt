package dev.gumil.nookbot

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.util.KtorExperimentalAPI
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

internal object Component {

    @OptIn(KtorExperimentalAPI::class)
    fun provideHttpClient(engine: HttpClientEngine? = null): HttpClient {
        return if (engine != null) {
            HttpClient(engine) {
                applyConfigurations()
            }
        } else {
            HttpClient(CIO) {
                applyConfigurations()
            }
        }
    }

    @OptIn(UnstableDefault::class)
    private fun HttpClientConfig<*>.applyConfigurations() {
        install(JsonFeature) {
            serializer = KotlinxSerializer(
                Json(JsonConfiguration(ignoreUnknownKeys = true))
            )
        }

        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.HEADERS
        }
    }
}
