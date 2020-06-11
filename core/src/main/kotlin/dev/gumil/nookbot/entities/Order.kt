package dev.gumil.nookbot.entities

data class Order(
    val id: Int,
    val name: String,
    val isSent: Boolean,
    val buyer: Resident,
    val seller: Resident
)