package nl.jovmit.lyrics.main.preferences

import android.content.SharedPreferences

internal class MockSharedPreferences : SharedPreferences {

    private val editor = MockEditor()
    private val values = mutableMapOf<String?, String?>()

    override fun getAll(): MutableMap<String, *> = TODO("not implemented")

    override fun getString(key: String?, fallbackValue: String?) = values[key]

    fun putString(key: String, value: String) {
        values[key] = value
    }

    override fun getStringSet(
        key: String?,
        fallbackValue: MutableSet<String>?
    ): MutableSet<String> = TODO("not implemented")

    override fun getInt(key: String?, fallbackValue: Int): Int = TODO("not implemented")

    override fun getLong(key: String?, fallbackValue: Long): Long = TODO("not implemented")

    override fun getFloat(key: String?, fallbackValue: Float): Float = TODO("not implemented")

    override fun getBoolean(key: String?, fallbackValue: Boolean): Boolean = TODO("not implemented")

    override fun contains(key: String?): Boolean = values.contains(key)

    override fun edit(): SharedPreferences.Editor = editor

    override fun registerOnSharedPreferenceChangeListener(
        onSharedPreferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener?
    ) { TODO("not implemented") }

    override fun unregisterOnSharedPreferenceChangeListener(
        onSharedPreferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener?
    ) { TODO("not implemented") }

    inner class MockEditor : SharedPreferences.Editor {

        override fun putString(key: String?, value: String?): SharedPreferences.Editor {
            values[key] = value
            return this
        }

        override fun putStringSet(
            key: String?,
            value: MutableSet<String>?
        ): SharedPreferences.Editor = TODO("not implemented")


        override fun putInt(key: String?, value: Int): SharedPreferences.Editor {
            TODO("not implemented")
        }

        override fun putLong(key: String?, value: Long): SharedPreferences.Editor {
            TODO("not implemented")
        }

        override fun putFloat(key: String?, value: Float): SharedPreferences.Editor {
            TODO("not implemented")
        }

        override fun putBoolean(key: String?, value: Boolean): SharedPreferences.Editor {
            TODO("not implemented")
        }

        override fun remove(key: String?): SharedPreferences.Editor {
            values.remove(key)
            return this
        }

        override fun clear(): SharedPreferences.Editor {
            values.clear()
            return this
        }

        override fun commit(): Boolean = true

        override fun apply() {}
    }
}
