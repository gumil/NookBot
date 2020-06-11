package dev.gumil.nookbot.entities.telegram

import kotlinx.serialization.Serializable

@Serializable
data class HttpResponse<T>(
    val ok: Boolean,
    val result: T
)
