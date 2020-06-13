package dev.gumil.nookbot.localization

internal object LocalizationEN: LocalizationContract {
    override val orderPlaced: String
        get() = "User %s is requesting %s"
    override val takeOrder: String
        get() = "Take Order"
    override val orderTaken: String
        get() = "%s has taken your order!"
    override val listOrderWithSellerItemBuyer: String
        get() = "%s is taking care of the %s by %s"
    override val noPendingOrders: String
        get() = "No pending orders"
}