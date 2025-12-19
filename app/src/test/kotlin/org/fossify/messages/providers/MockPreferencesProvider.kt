package org.fossify.messages.providers

/**
 * Mock implementation of PreferencesProvider for testing.
 * This allows tests to run without Android dependencies.
 */
class MockPreferencesProvider : PreferencesProvider {
    private val storage = mutableMapOf<String, Any>()
    
    override fun getBoolean(key: String, defaultValue: Boolean): Boolean =
        storage[key] as? Boolean ?: defaultValue

    override fun putBoolean(key: String, value: Boolean) {
        storage[key] = value
    }

    override fun getInt(key: String, defaultValue: Int): Int =
        storage[key] as? Int ?: defaultValue

    override fun putInt(key: String, value: Int) {
        storage[key] = value
    }

    override fun getLong(key: String, defaultValue: Long): Long =
        storage[key] as? Long ?: defaultValue

    override fun putLong(key: String, value: Long) {
        storage[key] = value
    }

    override fun getString(key: String, defaultValue: String): String =
        storage[key] as? String ?: defaultValue

    override fun putString(key: String, value: String) {
        storage[key] = value
    }

    @Suppress("UNCHECKED_CAST")
    override fun getStringSet(key: String, defaultValue: Set<String>): Set<String> =
        storage[key] as? Set<String> ?: defaultValue

    override fun putStringSet(key: String, value: Set<String>) {
        storage[key] = value
    }

    override fun addToStringSet(key: String, value: String) {
        val current = getStringSet(key)
        putStringSet(key, current.plus(value))
    }

    override fun removeFromStringSet(key: String, value: String) {
        val current = getStringSet(key)
        putStringSet(key, current.minus(value))
    }
}
