package org.fossify.messages.providers

import org.fossify.messages.models.Conversation

/**
 * Interface for managing application preferences.
 * This abstraction allows for easy mocking in tests and decouples from Android SharedPreferences.
 * Uses key-value approach to avoid a large interface with many properties.
 */
interface PreferencesProvider {
    // Generic preference access methods
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean
    fun putBoolean(key: String, value: Boolean)
    
    fun getInt(key: String, defaultValue: Int = 0): Int
    fun putInt(key: String, value: Int)
    
    fun getLong(key: String, defaultValue: Long = 0L): Long
    fun putLong(key: String, value: Long)
    
    fun getString(key: String, defaultValue: String = ""): String
    fun putString(key: String, value: String)
    
    fun getStringSet(key: String, defaultValue: Set<String> = emptySet()): Set<String>
    fun putStringSet(key: String, value: Set<String>)
    
    // Convenience methods for specific features
    fun addToStringSet(key: String, value: String)
    fun removeFromStringSet(key: String, value: String)
}
