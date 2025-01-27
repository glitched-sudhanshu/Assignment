package com.myjar.jarassignment.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.myjar.jarassignment.data.utils.AppConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.dataStore by lazy { preferencesDataStore(AppConstants.DATA_STORE_KEY) }.value

object DataStoreHelper {

    suspend fun save(context: Context, value: String) {
        val prefKey = stringPreferencesKey(AppConstants.COMPUTER_ITEMS)
        context.dataStore.edit {
            it[prefKey] = value
        }
    }


    fun read(context: Context): Flow<String?> {
        val prefKey = stringPreferencesKey(AppConstants.COMPUTER_ITEMS)
        return context.dataStore.data.map {
            it[prefKey]
        }
    }
}