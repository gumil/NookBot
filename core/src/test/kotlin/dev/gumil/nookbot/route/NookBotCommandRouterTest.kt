package dev.gumil.nookbot.route

import dev.gumil.nookbot.UpdateEntityFactory
import dev.gumil.nookbot.entities.Order
import dev.gumil.nookbot.entities.Resident
import dev.gumil.nookbot.telegram.entities.Update
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class NookBotCommandRouterTest {
    private val ordersService = FakeOrdersService()

    private val commandRouter = NookBotCommandRouter(ordersService)

    @Test
    fun `null message should ignore routing`() = runBlocking {
        commandRouter.route(
            Update(Random.nextLong()),
            Random.nextDouble().toString(),
            Random.nextDouble().toString()
        )

        ordersService.verifyEmptyOrder()
    }

    @Test
    fun `null user should ignore routing`() = runBlocking {
        commandRouter.route(
            UpdateEntityFactory.getUpdateNoUser(),
            Random.nextDouble().toString(),
            Random.nextDouble().toString()
        )

        ordersService.verifyEmptyOrder()
    }

    @Test
    fun `private chat should ignore routing`() = runBlocking {
        commandRouter.route(
            UpdateEntityFactory.getUpdatePrivateChat(),
            Random.nextDouble().toString(),
            Random.nextDouble().toString()
        )

        ordersService.verifyEmptyOrder()
    }

    @Test
    fun `unsupported command should ignore routing`() = runBlocking {
        commandRouter.route(
            UpdateEntityFactory.getUpdatePrivateChat(),
            Random.nextDouble().toString(),
            Random.nextDouble().toString()
        )

        ordersService.verifyEmptyOrder()
    }

    @Test
    fun `command order saves order`() = runBlocking {
        val name = Random.nextDouble().toString()
        val command = "order"
        val update = UpdateEntityFactory.getUpdate(command, name)
        val order = Order(
            id = update.updateId,
            name = name,
            buyer = Resident(
                update.message!!.from!!.id,
                update.message!!.from!!.firstName
            )
        )

        commandRouter.route(
            update,
            command,
            name
        )

        ordersService.verifySavedOrder(update.message!!.chat.id, order)
    }

    @Test
    fun `command take takes order`() = runBlocking {
        val name = Random.nextDouble().toString()
        val command = "take"
        val update = UpdateEntityFactory.getUpdate(command, name)

        commandRouter.route(
            update,
            command,
            name
        )

        ordersService.verifyOrderTaken(
            update.message!!.chat.id,
            update.message!!.messageId,
            update.updateId,
            Resident(
                update.message!!.from!!.id,
                update.message!!.from!!.firstName
            )
        )
    }

    @AfterEach
    fun tearDown() {
        ordersService.tearDown()
    }
}
