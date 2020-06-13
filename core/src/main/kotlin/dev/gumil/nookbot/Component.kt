package dev.gumil.nookbot

import dev.gumil.nookbot.repository.InMemoryOrdersRepository
import dev.gumil.nookbot.repository.OrdersRepository
import dev.gumil.nookbot.telegram.CommandRouter
import dev.gumil.nookbot.route.NookBotCommandRouter
import dev.gumil.nookbot.service.OrdersService
import dev.gumil.nookbot.service.TelegramOrdersService
import dev.gumil.nookbot.telegram.TelegramApi
import dev.gumil.nookbot.telegram.TelegramApiImpl
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

    fun provideTelegramApi(
        httpClient: HttpClient = provideHttpClient()
    ): TelegramApi {
        return TelegramApiImpl(httpClient)
    }

    fun provideOrdersRepository(): OrdersRepository {
        return InMemoryOrdersRepository()
    }

    fun provideOrdersService(
        ordersRepository: OrdersRepository = provideOrdersRepository(),
        telegramApi: TelegramApi = provideTelegramApi()
    ): OrdersService {
        return TelegramOrdersService(
            ordersRepository, telegramApi
        )
    }

    fun provideCommandRouter(
        ordersService: OrdersService = provideOrdersService()
    ): CommandRouter {
        return NookBotCommandRouter(ordersService)
    }

    @OptIn(UnstableDefault::class)
    private fun HttpClientConfig<*>.applyConfigurations() {
        install(JsonFeature) {
            serializer = KotlinxSerializer(
                Json(JsonConfiguration(
                    encodeDefaults = false,
                    ignoreUnknownKeys = true
                ))
            )
        }

        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }
}
