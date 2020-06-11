package dev.gumil.nookbot.localization

internal object LocalizationEN: LocalizationContract {
    override val orderPlaced: String
        get() = "User %s is requesting %s item"
    override val takeOrder: String
        get() = "Take Order"
}