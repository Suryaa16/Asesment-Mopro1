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

        //User Auth Data
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_PHOTO_URL = stringPreferencesKey("user_photo_url")
        val GOOGLE_ID = stringPreferencesKey("google_id")
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

    // Read user data
    val userName: Flow<String> = context.dataStore.data
        .map { it[USER_NAME] ?: "" }

    val userEmail: Flow<String> = context.dataStore.data
        .map { it[USER_EMAIL] ?: "" }

    val userPhotoUrl: Flow<String> = context.dataStore.data
        .map { it[USER_PHOTO_URL] ?: "" }

    val googleId: Flow<String> = context.dataStore.data
        .map { it[GOOGLE_ID] ?: "" }

    //Ngecek apakah user udah login
    val isLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            !preferences[GOOGLE_ID].isNullOrEmpty()
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

    //Simpan data user setelah Google Sign-In
    suspend fun saveUserData(
        name: String,
        email: String,
        photoUrl: String,
        googleId: String
    ) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME] = name
            preferences[USER_EMAIL] = email
            preferences[USER_PHOTO_URL] = photoUrl
            preferences[GOOGLE_ID] = googleId
        }
    }

    //Hapus data user saat logout
    suspend fun clearUserData() {
        context.dataStore.edit { preferences ->
            preferences.remove(USER_NAME)
            preferences.remove(USER_EMAIL)
            preferences.remove(USER_PHOTO_URL)
            preferences.remove(GOOGLE_ID)
        }
    }
}