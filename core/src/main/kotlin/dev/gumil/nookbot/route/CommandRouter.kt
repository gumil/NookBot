package dev.gumil.nookbot.route

import dev.gumil.nookbot.entities.telegram.Update

interface CommandRouter {
    suspend fun route(update: Update)
}
