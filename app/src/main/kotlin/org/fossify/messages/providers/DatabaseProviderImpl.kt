package org.fossify.messages.providers

import android.content.Context
import org.fossify.messages.databases.MessagesDatabase

/**
 * Implementation of DatabaseProvider that uses the existing MessagesDatabase.
 * Maintains backward compatibility while providing a testable interface.
 */
class DatabaseProviderImpl(private val context: Context) : DatabaseProvider {
    override fun getDatabase(): MessagesDatabase = MessagesDatabase.getInstance(context)
}
