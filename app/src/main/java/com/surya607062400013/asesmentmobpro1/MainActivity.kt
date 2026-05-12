package com.surya607062400013.asesmentmobpro1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.surya607062400013.asesmentmobpro1.navigation.AppNavigation
import com.surya607062400013.asesmentmobpro1.ui.theme.FitCallTheme
import com.surya607062400013.asesmentmobpro1.viewmodel.SettingsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsViewModel: SettingsViewModel = viewModel()
            val isDarkMode by  settingsViewModel.isDarkMode.collectAsStateWithLifecycle(false)
            val themeColor by settingsViewModel.themeColor.collectAsStateWithLifecycle("Purple")

            FitCallTheme(
                darkTheme = isDarkMode,
                themeColor = themeColor
            ) {
                AppNavigation()
            }
        }
    }
}
