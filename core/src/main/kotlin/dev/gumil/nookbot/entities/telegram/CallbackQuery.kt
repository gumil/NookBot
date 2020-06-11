package dev.gumil.nookbot.entities.telegram

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CallbackQuery(

    @SerialName("id")
    val id: String,

    @SerialName("from")
    val from: User,

    @SerialName("message")
    val message: Message? = null,

    @SerialName("data")
    val data: String? = null
)
