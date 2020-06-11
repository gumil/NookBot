package dev.gumil.nookbot.entities.serializer

import dev.gumil.nookbot.entities.telegram.Chat
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializer

@Serializer(forClass = Chat.Type::class)
internal object ChatTypeSerializer {
    override val descriptor: SerialDescriptor
        get() = PrimitiveDescriptor(this::class.java.simpleName, PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Chat.Type {
        return Chat.Type.valueOf(decoder.decodeString().toUpperCase())
    }

    override fun serialize(encoder: Encoder, value: Chat.Type) {
        encoder.encodeString(value.name.toLowerCase())
    }
}
