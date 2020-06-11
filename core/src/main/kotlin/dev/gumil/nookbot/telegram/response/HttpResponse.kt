package dev.gumil.nookbot.telegram.response

import kotlinx.serialization.Serializable

@Serializable
data class HttpResponse<T>(
    val ok: Boolean,
    val result: T
)
