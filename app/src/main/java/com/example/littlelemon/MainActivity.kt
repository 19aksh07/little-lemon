package com.example.littlelemon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.littlelemon.ui.theme.LittleLemonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LittleLemonTheme {
                LittleLemonApp()
            }
        }
    }
}

@Composable
private fun LittleLemonApp() {
    val context = androidx.compose.ui.platform.LocalContext.current
    val preferences = remember {
        context.getSharedPreferences(
            "little_lemon_session",
            android.content.Context.MODE_PRIVATE
        )
    }
    val navController = rememberNavController()
    val startDestination = remember {
        if (preferences.getBoolean("is_logged_in", false)) "home" else "onboarding"
    }

    LittleLemonNavHost(
        navController = navController,
        startDestination = startDestination,
        preferences = preferences,
        onRegister = { registration ->
            preferences.edit()
                .putBoolean("is_logged_in", true)
            .putString("first_name", registration.firstName)
            .putString("last_name", registration.lastName)
            .putString("email", registration.email)
                .apply()
        },
        onLogout = {
            preferences.edit().clear().apply()
        }
    )
}
