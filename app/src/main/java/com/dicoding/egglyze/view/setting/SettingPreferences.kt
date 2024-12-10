package com.example.submissionevent.ui.setting

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val THEME_KEY = booleanPreferencesKey("theme_setting")

    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: false
        }
    }

    suspend fun saveThemeSetting(isDarkModeActivity: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDarkModeActivity
        }
    }

    suspend fun setNotificationSetting(isEnabled: Boolean) {
        dataStore.edit { preferences -> preferences[PreferencesKeys.NOTIFICATION_SETTING] = isEnabled }
    }

    val getNotificationSetting: Flow<Boolean> = dataStore.data
        .map { preferences -> preferences[PreferencesKeys.NOTIFICATION_SETTING] ?: false }


    object PreferencesKeys {
        val NOTIFICATION_SETTING = booleanPreferencesKey("notification_setting")
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}