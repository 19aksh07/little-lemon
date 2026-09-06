package com.example.littlelemon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    val database = remember { MenuDatabase.create(context) }
    val httpClient = remember { createMenuHttpClient() }
    var menuLoading by remember { mutableStateOf(true) }
    var menuError by remember { mutableStateOf<String?>(null) }
    var menuRequest by remember { mutableStateOf(0) }
    val startDestination = remember {
        if (preferences.getBoolean("is_logged_in", false)) "home" else "onboarding"
    }

    DisposableEffect(httpClient) {
        onDispose { httpClient.close() }
    }

    LaunchedEffect(database, httpClient, menuRequest) {
        menuLoading = true
        menuError = null
        try {
            if (database.menuDao().count() == 0) {
                val items = MenuNetworkRepository(httpClient)
                    .fetchMenu()
                    .map(MenuItemNetwork::toEntity)
                if (items.isEmpty()) {
                    error("The menu response was empty")
                }
                database.menuDao().insertAll(items)
            }
        } catch (exception: Exception) {
            android.util.Log.e("LittleLemonMenu", "Menu initialization failed", exception)
            menuError = exception.message ?: "Unable to load the menu"
        } finally {
            menuLoading = false
        }
    }

    LittleLemonNavHost(
        navController = navController,
        startDestination = startDestination,
        preferences = preferences,
        database = database,
        menuLoading = menuLoading,
        menuError = menuError,
        onRetryMenu = { menuRequest++ },
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
