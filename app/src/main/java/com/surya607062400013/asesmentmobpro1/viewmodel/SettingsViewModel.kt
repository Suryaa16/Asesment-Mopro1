package com.surya607062400013.asesmentmobpro1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.surya607062400013.asesmentmobpro1.FitcallApplication
import com.surya607062400013.asesmentmobpro1.data.datastore.SettingsDataStore
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val dataStore = SettingsDataStore(application)
    //Read dark mode
    val isDarkMode = dataStore.isDarkMode
    //Read theme color
    val themeColor = dataStore.themeColor
    //Save dark mode
    fun setDarkMode(isDark: Boolean) {
        viewModelScope.launch {
            dataStore.saveDarkMode(isDark)
        }
    }
    //Save theme color
    fun setThemeColor(color: String) {
        viewModelScope.launch {
            dataStore.saveThemeColor(color)
        }
    }
}