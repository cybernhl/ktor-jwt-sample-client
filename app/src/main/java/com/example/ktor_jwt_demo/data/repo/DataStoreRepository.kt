package com.example.ktor_jwt_demo.data.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        val TOKEN = stringPreferencesKey("token")
        val USERNAME = stringPreferencesKey("username")
        val USERID = stringPreferencesKey("userId")
    }

    suspend fun clear() {
        dataStore.edit {
            it.clear()
        }
    }

    suspend fun <T : Any> getValue(key: Preferences.Key<T>): T? {
        return dataStore.data.map { it[key] }.first()
    }

    suspend fun <T : Any> setValue(key: Preferences.Key<T>, value: T) {
        dataStore.edit { it[key] = value }
    }
}