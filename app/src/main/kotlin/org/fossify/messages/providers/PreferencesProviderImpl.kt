package org.fossify.messages.providers

import android.content.Context
import android.content.SharedPreferences

/**
 * Implementation of PreferencesProvider that directly accesses SharedPreferences.
 * This maintains backward compatibility while providing a testable interface.
 */
class PreferencesProviderImpl(context: Context) : PreferencesProvider {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "${context.packageName}_preferences", 
        Context.MODE_PRIVATE
    )

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean =
        prefs.getBoolean(key, defaultValue)

    override fun putBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    override fun getInt(key: String, defaultValue: Int): Int =
        prefs.getInt(key, defaultValue)

    override fun putInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    override fun getLong(key: String, defaultValue: Long): Long =
        prefs.getLong(key, defaultValue)

    override fun putLong(key: String, value: Long) {
        prefs.edit().putLong(key, value).apply()
    }

    override fun getString(key: String, defaultValue: String): String =
        prefs.getString(key, defaultValue) ?: defaultValue

    override fun putString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    override fun getStringSet(key: String, defaultValue: Set<String>): Set<String> =
        prefs.getStringSet(key, defaultValue) ?: defaultValue

    override fun putStringSet(key: String, value: Set<String>) {
        prefs.edit().putStringSet(key, value).apply()
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
