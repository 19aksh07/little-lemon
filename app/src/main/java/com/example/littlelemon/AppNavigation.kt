package com.example.littlelemon

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

private object Routes {
    const val ONBOARDING = "onboarding"
    const val HOME = "home"
    const val PROFILE = "profile"
}

@Composable
fun LittleLemonNavHost(
    navController: NavHostController,
    startDestination: String,
    onRegister: (String) -> Unit,
    onLogout: () -> Unit
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Routes.ONBOARDING) {
            Onboarding(onRegister = { firstName ->
                onRegister(firstName)
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