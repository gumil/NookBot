package dev.gumil.nookbot.localization

internal object Localization: LocalizationContract {
    override val orderPlaced: String
        get() = LocalizationEN.orderPlaced
    override val takeOrder: String
        get() = LocalizationEN.takeOrder
    override val orderTaken: String
        get() = LocalizationEN.orderTaken
    override val listOrderWithSellerItemBuyer: String
        get() = LocalizationEN.listOrderWithSellerItemBuyer
    override val noPendingOrders: String
        get() = LocalizationEN.noPendingOrders
    override val userHasPendingOrder: String
        get() = LocalizationEN.userHasPendingOrder
    override val userSentOrderToBuyer: String
        get() = LocalizationEN.userSentOrderToBuyer
}