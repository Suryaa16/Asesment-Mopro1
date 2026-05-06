package com.surya607062400013.asesmentmobpro1.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//Extension property untuk datastore
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsDataStore(private val context: Context) {
    companion object{
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val THEME_COLOR = stringPreferencesKey("theme_color")
    }

    //Read dark mode
    val isDarkMode: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[DARK_MODE] ?: false
        }

    //Read theme color
    val themeColor: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[THEME_COLOR] ?: "Purple"
        }

    //save dark mode
    suspend fun saveDarkMode(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_MODE] = isDark
        }
    }

    //save theme color
    suspend fun saveThemeColor(color: String) {
        context.dataStore.edit { preferences ->
            preferences[THEME_COLOR] = color
        }
    }
}