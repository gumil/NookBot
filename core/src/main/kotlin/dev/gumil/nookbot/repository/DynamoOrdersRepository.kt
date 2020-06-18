package dev.gumil.nookbot.repository

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import dev.gumil.nookbot.entities.Order
import dev.gumil.nookbot.entities.Resident

internal class DynamoOrdersRepository(
    client: AmazonDynamoDB
) : OrdersRepository {

    private val mapper = DynamoDBMapper(client)

    override fun save(id: Long, order: Order) {
        val nookBotTable = mapper.load(NookBotTable::class.java, id)
        val orderDocument = order.toDocument()

        if (nookBotTable == null) {
            mapper.save(NookBotTable(id, listOf(orderDocument)))
            return
        }

        val orders = nookBotTable.orders.toMutableList()

        val indexOrder = orders.indexOfFirst { it.id == orderDocument.id }

        if (indexOrder > -1) {
            orders[indexOrder] = orderDocument
            mapper.save(NookBotTable(id, orders))
            return
        }


        orders.add(orderDocument)

        mapper.save(NookBotTable(id, orders))
    }

    override fun getOrders(id: Long): List<Order>? {
        return mapper.load(NookBotTable::class.java, id)?.orders?.map { it.toEntity() }
    }

    override fun getOrder(chatId: Long, orderId: OrderId): Order? {
        return getOrders(chatId)?.find { it.id == orderId.id }
    }

    override fun getOrder(chatId: Long, seller: SellerId): Order? {
        return getOrders(chatId)?.find { it.seller?.id == seller.id }
    }

    override fun hasPendingOrder(chatId: Long, seller: Resident): Boolean {
        return getOrders(chatId)?.any { order ->
            seller == order.seller
        } ?: false
    }

    override fun deleteOrder(id: Long, order: Order): Boolean {
        val nookBotTable = mapper.load(NookBotTable::class.java, id) ?: return false
        val orderDocument = order.toDocument()

        val orders = nookBotTable.orders.toMutableList()

        val isRemoved = orders.remove(orderDocument)

        mapper.save(NookBotTable(id, orders))

        return isRemoved
    }
}
