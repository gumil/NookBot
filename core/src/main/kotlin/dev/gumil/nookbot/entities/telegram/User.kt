package dev.gumil.nookbot.entities.telegram

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(

    @SerialName("id")
    val id: Long,

    @SerialName("is_bot")
    val isBot: Boolean,

    @SerialName("first_name")
    val firstName: String,

    @SerialName("username")
    val username: String? = null,

    @SerialName("language_code")
    val languageCode: String? = null
)
