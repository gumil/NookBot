package dev.gumil.nookbot.route

import dev.gumil.nookbot.entities.Order
import dev.gumil.nookbot.entities.Resident
import dev.gumil.nookbot.telegram.entities.Update
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class CommandRouterImplTest {
    private val ordersService = FakeOrdersService()

    private val commandRouter = CommandRouterImpl(ordersService)

    @Test
    fun `null message should ignore routing`() = runBlocking {
        commandRouter.route(Update(Random.nextLong()))

        ordersService.verifyEmptyOrder()
    }

    @Test
    fun `null user should ignore routing`() = runBlocking {
        commandRouter.route(CommandRouterFactory.getUpdateNoUser())

        ordersService.verifyEmptyOrder()
    }

    @Test
    fun `private chat should ignore routing`() = runBlocking {
        commandRouter.route(CommandRouterFactory.getUpdatePrivateChat())

        ordersService.verifyEmptyOrder()
    }

    @Test
    fun `command order saves order`() = runBlocking {
        val name = Random.nextDouble().toString()
        val update = CommandRouterFactory.getUpdateForOrder(name)
        val order = Order(
            id = update.updateId,
            name = name,
            buyer = Resident(
                update.message!!.from!!.id,
                update.message!!.from!!.firstName
            )
        )

        commandRouter.route(update)

        ordersService.verifySavedOrder(update.message!!.chat.id, order)
    }

    @AfterEach
    fun tearDown() {
        ordersService.tearDown()
    }
}