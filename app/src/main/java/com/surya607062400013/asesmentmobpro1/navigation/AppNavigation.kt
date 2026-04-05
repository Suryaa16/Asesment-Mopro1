package com.surya607062400013.asesmentmobpro1.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.surya607062400013.asesmentmobpro1.ui.screens.HomeScreen
import com.surya607062400013.asesmentmobpro1.ui.screens.AboutScreen
import com.surya607062400013.asesmentmobpro1.ui.screens.BmiScreen
import com.surya607062400013.asesmentmobpro1.ui.screens.CalorieScreen
import com.surya607062400013.asesmentmobpro1.ui.screens.ProteinScreen

object Routes {
    const val HOME = "home"
    const val BMI = "bmi"
    const val CALORIE = "calorie"
    const val PROTEIN = "protein"
    const val ABOUT = "about"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                onNavigateToBmi = { navController.navigate(Routes.BMI) },
                onNavigateToCalorie = { navController.navigate(Routes.CALORIE) },
                onNavigateToProtein = { navController.navigate(Routes.PROTEIN) },
                onNavigateToAbout = { navController.navigate(Routes.ABOUT)}
            )
        }

        composable(Routes.BMI) {
            BmiScreen(
                onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(Routes.CALORIE) {
            CalorieScreen(
                onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(Routes.PROTEIN) {
            ProteinScreen(
                onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(Routes.ABOUT) {
            AboutScreen(
                onNavigateUp = { navController.navigateUp()}
            )
        }
    }
}