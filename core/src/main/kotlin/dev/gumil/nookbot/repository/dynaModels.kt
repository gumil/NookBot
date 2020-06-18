package dev.gumil.nookbot.repository

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable
import dev.gumil.nookbot.entities.Order
import dev.gumil.nookbot.entities.Resident

const val TABLE_NAME = "NookBot"

@DynamoDBTable(tableName = TABLE_NAME)
data class NookBotTable(

    @get:DynamoDBHashKey(attributeName = "id")
    var id: Long = 0,

    @get:DynamoDBAttribute(attributeName = "orders")
    var orders: List<OrderDocument> = emptyList()
)

@DynamoDBDocument
data class OrderDocument(

    @get:DynamoDBAttribute(attributeName = "id")
    var id: Long = 0,

    @get:DynamoDBAttribute(attributeName = "name")
    var name: String? = null,

    @get:DynamoDBAttribute(attributeName = "buyer")
    var buyer: ResidentDocument? = null,

    @get:DynamoDBAttribute(attributeName = "seller")
    var seller: ResidentDocument? = null
)

@DynamoDBDocument
data class ResidentDocument(

    @get:DynamoDBAttribute(attributeName = "id")
    var id: Long = 0,

    @get:DynamoDBAttribute(attributeName = "name")
    var name: String? = null
)

internal fun Order.toDocument(): OrderDocument {
    return OrderDocument(
        id,
        name,
        buyer.toDocument(),
        seller?.toDocument()
    )
}

internal fun Resident.toDocument(): ResidentDocument {
    return ResidentDocument(
        id,
        name
    )
}

internal fun OrderDocument.toEntity(): Order {
    return Order(
        id,
        name ?: throw IllegalArgumentException("name should not be null"),
        buyer?.toEntity() ?: throw IllegalArgumentException("buyer should not be null"),
        seller?.toEntity()
    )
}

internal fun ResidentDocument.toEntity(): Resident {
    return Resident(
        id,
        name ?: throw IllegalArgumentException("name should not be null")
    )
}
