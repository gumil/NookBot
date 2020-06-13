package dev.gumil.nookbot.telegram.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Update(

    @SerialName("update_id")
    val updateId: Long,

    @SerialName("message")
    val message: Message? = null,

    @SerialName("callback_query")
    val callbackQuery: CallbackQuery? = null
)
