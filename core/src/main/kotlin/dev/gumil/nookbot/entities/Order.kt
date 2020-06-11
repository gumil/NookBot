package dev.gumil.nookbot.entities

data class Order(
    val id: Long,
    val name: String,
    val isSent: Boolean = false,
    val buyer: Resident,
    val seller: Resident? = null
)
