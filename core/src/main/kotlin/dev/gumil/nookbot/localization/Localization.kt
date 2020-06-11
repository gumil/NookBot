package dev.gumil.nookbot.localization

internal object Localization: LocalizationContract {
    override val orderPlaced: String
        get() = LocalizationEN.orderPlaced
    override val takeOrder: String
        get() = LocalizationEN.takeOrder
}