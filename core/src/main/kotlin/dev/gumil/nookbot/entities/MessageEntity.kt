package dev.gumil.nookbot.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageEntity(

    @SerialName("offset")
    val offset: Int,

    @SerialName("length")
    val length: Int,

    @SerialName("type")
    val type: String
)
