package dev.gumil.nookbot.telegram.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InlineKeyboardButton(

    @SerialName("text")
    val text: String,

    @SerialName("callback_data")
    val callbackData: String
)
