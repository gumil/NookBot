package dev.gumil.nookbot.telegram.request

import dev.gumil.nookbot.telegram.entities.InlineKeyboardMarkup
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SendMessageRequest(

    @SerialName("chat_id")
    val chatId: String,

    @SerialName("text")
    val text: String,

    @SerialName("reply_markup")
    var replyMarkup: InlineKeyboardMarkup? = null
)
