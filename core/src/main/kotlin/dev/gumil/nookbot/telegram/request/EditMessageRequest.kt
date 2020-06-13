package dev.gumil.nookbot.telegram.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EditMessageRequest(

    @SerialName("chat_id")
    val chatId: String,

    @SerialName("message_id")
    val messageId: Long,

    @SerialName("text")
    val text: String? = null
)
