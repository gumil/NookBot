package dev.gumil.nookbot.utils

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue

internal class TestInMemoryStore<T> {
    private val itemsMap = mutableMapOf<Long, MutableList<T>>()

    fun save(id: Long, item: T) {
        itemsMap[id]?.add(item) ?: itemsMap.put(id, mutableListOf(item))
    }

    fun getItems(id: Long): List<T>? {
        return itemsMap[id]
    }

    fun deleteItem(id: Long, item: T): Boolean {
        return itemsMap[id]?.remove(item) ?: false
    }

    fun verifySavedOrder(id: Long, item: T) {
        val items = itemsMap[id]
        assertNotNull(items)
        assertTrue(items?.contains(item) ?: false)
    }

    fun verifyEmptyStore() {
        assertTrue(itemsMap.isEmpty())
    }

    fun tearDown() {
        itemsMap.clear()
    }
}