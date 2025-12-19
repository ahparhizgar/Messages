package org.fossify.messages.providers

import org.fossify.messages.databases.MessagesDatabase

/**
 * Interface for providing access to the Messages database.
 * This abstraction enables easy testing by allowing replacement with in-memory database.
 */
interface DatabaseProvider {
    fun getDatabase(): MessagesDatabase
}
