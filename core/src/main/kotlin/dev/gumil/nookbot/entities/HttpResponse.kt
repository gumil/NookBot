package dev.gumil.nookbot.entities

import kotlinx.serialization.Serializable

@Serializable
data class HttpResponse<T>(
    val ok: Boolean,
    val result: T
)
