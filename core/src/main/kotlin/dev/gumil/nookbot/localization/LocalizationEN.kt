package dev.gumil.nookbot.localization

internal object LocalizationEN : LocalizationContract {
    override val orderPlaced: String
        get() = "User @%s is requesting %s"
    override val takeOrder: String
        get() = "Take Order"
    override val orderTaken: String
        get() = "%s has taken your order!"
    override val listOrderWithSellerItemBuyer: String
        get() = "@%s is taking care of %s by @%s"
    override val noPendingOrders: String
        get() = "No pending orders"
    override val userHasPendingOrder: String
        get() = "@%s you have a pending order. Make sure to deliver it first before taking a new one."
    override val userSentOrderToBuyer: String
        get() = "@%s have sent the %s order to @%s."
}
