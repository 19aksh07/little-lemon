package com.example.littlelemon.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LittleLemonColorScheme = lightColorScheme(
    primary = LittleLemonGreen,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    secondary = LittleLemonYellow,
    onSecondary = LittleLemonCharcoal,
    tertiary = LittleLemonOrange,
    background = androidx.compose.ui.graphics.Color.White,
    surface = androidx.compose.ui.graphics.Color.White,
    onBackground = LittleLemonCharcoal,
    onSurface = LittleLemonCharcoal,
    outline = LittleLemonGreen
)

@Composable
fun LittleLemonTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LittleLemonColorScheme,
        typography = Typography,
        content = content
    )
}