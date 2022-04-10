@file:Suppress("unused")

package com.grand.ezkorone.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.grand.ezkorone.core.extensions.fromJsonOrNull
import com.grand.ezkorone.core.extensions.toJsonOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

abstract class PrefsBase(
    protected val context: Context,
    protected val gson: Gson,
    name: String
) {

    protected val Context.dataStore by preferencesDataStore(name = name)

    protected suspend inline fun <reified T> setValue(key: String, value: T?) {
        context.dataStore.edit { prefs ->
            prefs[stringPreferencesKey(key)] = value.toJsonOrNull(gson) ?: ""
        }
    }

    protected inline fun <reified T> getValue(key: String): Flow<T?> {
        return context.dataStore.data.map { prefs ->
            prefs[stringPreferencesKey(key)].fromJsonOrNull(gson)
        }
    }

    /** Note will be saved as empty string in case of `null` [value] */
    protected suspend fun setStringValue(key: String, value: String?) {
        context.dataStore.edit { prefs ->
            prefs[stringPreferencesKey(key)] = value ?: ""
        }
    }

    protected fun getStringValue(key: String): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[stringPreferencesKey(key)]
        }
    }

    /** Note will be saved as empty string in case of `null` [value] */
    protected suspend fun setBooleanValue(key: String, value: Boolean?) {
        context.dataStore.edit { prefs ->
            prefs[booleanPreferencesKey(key)] = value ?: false
        }
    }

    protected fun getBooleanValue(key: String): Flow<Boolean?> {
        return context.dataStore.data.map { prefs ->
            prefs[booleanPreferencesKey(key)]
        }
    }

    protected suspend fun setIntValue(key: String, value: Int?) {
        context.dataStore.edit { prefs ->
            prefs[intPreferencesKey(key)] = value ?: 0
        }
    }

    protected fun getIntValue(key: String): Flow<Int?> {
        return context.dataStore.data.map { prefs ->
            prefs[intPreferencesKey(key)]
        }
    }

    protected suspend fun setLongValue(key: String, value: Long?) {
        context.dataStore.edit { prefs ->
            prefs[longPreferencesKey(key)] = value ?: 0
        }
    }

    protected fun getLongValue(key: String): Flow<Long?> {
        return context.dataStore.data.map { prefs ->
            prefs[longPreferencesKey(key)]
        }
    }

    /*protected inline fun <reified T> createSaver(): Saver {
        return Saver("", typeOf<T>())
    }

    protected inner class Saver constructor(private val key: String, private val kType: KType) {

    }*/

}
