package dev.gumil.nookbot.route

import dev.gumil.nookbot.telegram.entities.Update

interface CommandRouter {
    suspend fun route(update: Update)
}
