package dev.gumil.nookbot.entities.telegram

import dev.gumil.nookbot.entities.telegram.Message
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Update(

    @SerialName("update_id")
    val updateId: Int,

    @SerialName("message")
    val message: Message? = null
)
