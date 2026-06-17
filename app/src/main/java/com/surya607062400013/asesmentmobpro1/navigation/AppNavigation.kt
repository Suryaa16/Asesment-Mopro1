package com.surya607062400013.asesmentmobpro1.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.surya607062400013.asesmentmobpro1.R
import com.surya607062400013.asesmentmobpro1.ui.screens.*
import com.surya607062400013.asesmentmobpro1.viewmodel.AuthViewModel
import com.surya607062400013.asesmentmobpro1.viewmodel.HistoryViewModel
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.Fastfood
import com.surya607062400013.asesmentmobpro1.viewmodel.MealViewModel
import com.surya607062400013.asesmentmobpro1.viewmodel.SettingsViewModel

object Routes {
    const val LOGIN = "login"
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
    const val PROFILE = "profile"
    const val MEAL_DIARY = "meal_diary"
    const val ADD_EDIT_MEAL = "add_edit_meal/{mealId}"
}

//Screen yang tampil di Bottom Navigation
sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val labelRes: Int) {
    object Home : BottomNavItem(Routes.HOME, Icons.Default.Home, R.string.bottom_nav_home)
    object MealDiary : BottomNavItem(Routes.MEAL_DIARY, Icons.Default.Fastfood, R.string.bottom_nav_meal_diary)
    object History : BottomNavItem(Routes.HISTORY, Icons.Default.History, R.string.bottom_nav_history)
    object Profile : BottomNavItem(Routes.PROFILE, Icons.Default.Person, R.string.bottom_nav_profile)
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val historyViewModel: HistoryViewModel = viewModel()
    val settingsViewModel: SettingsViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()
    val mealViewModel: MealViewModel = viewModel()
    val bottomNavItems = listOf(BottomNavItem.Home, BottomNavItem.MealDiary, BottomNavItem.History, BottomNavItem.Profile)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route
    val bottomNavRoutes = listOf(Routes.HOME, Routes.MEAL_DIARY, Routes.HISTORY, Routes.PROFILE)
    val showBottomNav = currentRoute in bottomNavRoutes

    val isLoggedIn by authViewModel.isLoggedIn.collectAsStateWithLifecycle()

    if (isLoggedIn == null) {
        return
    }

    val startDest = remember { if (isLoggedIn == true) Routes.HOME else Routes.LOGIN }

    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = stringResource(item.labelRes)
                                )
                            },
                            label = { 
                                Text(
                                    text = stringResource(item.labelRes),
                                    maxLines = 1,
                                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                ) 
                            },
                            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDest,
            modifier = Modifier
        ) {
            // Login Screen
            composable(Routes.LOGIN) {
                LoginScreen(
                    authViewModel = authViewModel,
                    onLoginSuccess = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.HOME) {
                HomeScreen(
                    onNavigateToBmi = { navController.navigate(Routes.BMI) },
                    onNavigateToCalorie = { navController.navigate(Routes.CALORIE) },
                    onNavigateToProtein = { navController.navigate(Routes.PROTEIN) },
                    onNavigateToAbout = { navController.navigate(Routes.ABOUT) },
                    onNavigateToSettings = { navController.navigate(Routes.SETTINGS) },
                    rootPadding = paddingValues
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
                AboutScreen(onNavigateUp = { navController.navigateUp() })
            }

            composable(Routes.HISTORY) {
                HistoryScreen(
                    rootPadding = paddingValues,
                    onNavigateToDetail = { id ->
                        navController.navigate("history_detail/$id")
                    },
                    onNavigateToRecycleBin = { navController.navigate(Routes.RECYCLE_BIN) },
                    historyViewModel = historyViewModel
                )
            }

            composable(Routes.HISTORY_DETAIL) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
                HistoryDetailScreen(
                    id = id,
                    onNavigateUp = { navController.navigateUp() },
                    onNavigateToEdit = { navController.navigate("history_edit/$id") },
                    historyViewModel = historyViewModel
                )
            }

            composable(Routes.HISTORY_EDIT) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
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

            composable(Routes.MEAL_DIARY) {
                MealDiaryScreen(
                    mealViewModel = mealViewModel,
                    rootPadding = paddingValues,
                    onNavigateToAdd = { navController.navigate("add_edit_meal/-1") },
                    onNavigateToEdit = { id -> navController.navigate("add_edit_meal/$id") }
                )
            }

            composable(Routes.ADD_EDIT_MEAL) { backStackEntry ->
                val mealId = backStackEntry.arguments?.getString("mealId")?.toIntOrNull() ?: -1
                AddEditMealScreen(
                    mealId = mealId,
                    mealViewModel = mealViewModel,
                    authViewModel = authViewModel,
                    onNavigateUp = { navController.navigateUp() }
                )
            }

            composable(Routes.SETTINGS) {
                SettingsScreen(
                    onNavigateUp = { navController.navigateUp() },
                    settingsViewModel = settingsViewModel
                )
            }

            composable(Routes.PROFILE) {
                ProfileScreen(
                    authViewModel = authViewModel,
                    rootPadding = paddingValues,
                    onLogout = {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}