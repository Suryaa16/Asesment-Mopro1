package com.surya607062400013.asesmentmobpro1.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.surya607062400013.asesmentmobpro1.ui.screens.HomeScreen
import com.surya607062400013.asesmentmobpro1.ui.screens.AboutScreen
import com.surya607062400013.asesmentmobpro1.ui.screens.BmiScreen
import com.surya607062400013.asesmentmobpro1.ui.screens.CalorieScreen
import com.surya607062400013.asesmentmobpro1.ui.screens.HistoryDetailScreen
import com.surya607062400013.asesmentmobpro1.ui.screens.HistoryEditScreen
import com.surya607062400013.asesmentmobpro1.ui.screens.HistoryScreen
import com.surya607062400013.asesmentmobpro1.ui.screens.ProteinScreen
import com.surya607062400013.asesmentmobpro1.ui.screens.RecycleBinScreen
import com.surya607062400013.asesmentmobpro1.ui.screens.SettingsScreen
import com.surya607062400013.asesmentmobpro1.viewmodel.HistoryViewModel
import com.surya607062400013.asesmentmobpro1.viewmodel.SettingsViewModel

object Routes {
    const val HOME = "home"
    const val BMI = "bmi"
    const val CALORIE = "calorie"
    const val PROTEIN = "protein"
    const val ABOUT = "about"
    const val HISTORY = "history"
    const val HISTORY_DETAIL = "history_detail/{id}"
    const val HISTORY_EDIT = "history_edit/{id}"
    const val RECYCLE_BIN = "recycle_bin"
    const val SETTINGS = "settings"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val historyViewModel: HistoryViewModel = viewModel()
    val settingsViewModel: SettingsViewModel = viewModel()

    val isDarkMode by settingsViewModel.isDarkMode.collectAsState(initial = false)
    val themeColor by settingsViewModel.themeColor.collectAsState(initial = "purple")

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                onNavigateToBmi = { navController.navigate(Routes.BMI) },
                onNavigateToCalorie = { navController.navigate(Routes.CALORIE) },
                onNavigateToProtein = { navController.navigate(Routes.PROTEIN) },
                onNavigateToAbout = { navController.navigate(Routes.ABOUT)},
                onNavigateToHistory = { navController.navigate(Routes.HISTORY)},
                onNavigateToSettings = { navController.navigate(Routes.SETTINGS)}
            )
        }

        composable(Routes.BMI) {
            BmiScreen(
                onNavigateUp = { navController.navigateUp() },
                historyViewModel = historyViewModel
            )
        }

        composable(Routes.CALORIE) {
            CalorieScreen(
                onNavigateUp = { navController.navigateUp() },
                historyViewModel = historyViewModel
            )
        }

        composable(Routes.PROTEIN) {
            ProteinScreen(
                onNavigateUp = { navController.navigateUp() },
                historyViewModel = historyViewModel
            )
        }

        composable(Routes.ABOUT) {
            AboutScreen(
                onNavigateUp = { navController.navigateUp()}
            )
        }

        composable (Routes.HISTORY) {
            HistoryScreen(
                onNavigateUp = { navController.navigateUp() },
                onNavigateToDetail = { id ->
                    navController.navigate("history_detail/$id")
                },
                onNavigateToRecycleBin = { navController.navigate(Routes.RECYCLE_BIN) },
                historyViewModel = historyViewModel
            )
        }

        composable(Routes.HISTORY_DETAIL) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?:0
            HistoryDetailScreen(
                id = id,
                onNavigateUp = { navController.navigateUp() },
                onNavigateToEdit = { navController.navigate("history_edit/$id") },
                historyViewModel = historyViewModel
            )
        }

        composable(Routes.HISTORY_EDIT) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?:0
            HistoryEditScreen(
                id = id,
                onNavigateUp = { navController.navigateUp() },
                historyViewModel = historyViewModel
            )
        }

        composable(Routes.RECYCLE_BIN) {
            RecycleBinScreen(
                onNavigateUp = { navController.navigateUp() },
                historyViewModel = historyViewModel
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                onNavigateUp = { navController.navigateUp() },
                settingsViewModel = settingsViewModel
            )
        }
    }
}