package dev.gumil.nookbot.telegram.entities

import dev.gumil.nookbot.telegram.entities.serializer.ChatTypeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Chat(

    @SerialName("id")
    val id: Long,

    @SerialName("type")
    val type: Type,

    @SerialName("title")
    val title: String? = null,

    @SerialName("username")
    val username: String? = null,

    @SerialName("first_name")
    val firstName: String? = null
) {

    @Serializable(with = ChatTypeSerializer::class)
    enum class Type {
        PRIVATE,
        GROUP,
        SUPERGROUP,
        CHANNEL
    }
}
