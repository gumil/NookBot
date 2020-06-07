package dev.gumil.nookbot.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Chat(

    @SerialName("id")
    val id: Int,

    @SerialName("type")
    val type: Type,

    @SerialName("username")
    val username: String? = null,

    @SerialName("first_name")
    val firstName: String? = null
) {
    enum class Type {
        PRIVATE,
        GROUP,
        SUPERGROUP,
        CHANNEL
    }
}
