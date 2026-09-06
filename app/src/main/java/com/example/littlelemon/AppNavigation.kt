package com.example.littlelemon

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

data class RegistrationData(
    val firstName: String,
    val lastName: String,
    val email: String
)

private object Routes {
    const val ONBOARDING = "onboarding"
    const val HOME = "home"
    const val PROFILE = "profile"
}

@Composable
fun LittleLemonNavHost(
    navController: NavHostController,
    startDestination: String,
    onRegister: (RegistrationData) -> Unit,
    preferences: android.content.SharedPreferences,
    onLogout: () -> Unit
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Routes.ONBOARDING) {
            Onboarding(onRegister = { firstName, lastName, email ->
                onRegister(RegistrationData(firstName, lastName, email))
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.ONBOARDING) { inclusive = true }
                    launchSingleTop = true
                }
            })
        }
        composable(Routes.HOME) {
            HomeScreen(onProfileClick = { navController.navigate(Routes.PROFILE) })
        }
        composable(Routes.PROFILE) {
            ProfileScreen(
                preferences = preferences,
                onBack = { navController.popBackStack() },
                onLogout = {
                    onLogout()
                    navController.navigate(Routes.ONBOARDING) {
                        popUpTo(Routes.HOME) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}