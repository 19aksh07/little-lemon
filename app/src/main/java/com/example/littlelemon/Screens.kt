package com.example.littlelemon

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.littlelemon.ui.theme.LittleLemonGreen
import com.example.littlelemon.ui.theme.LittleLemonYellow

@Composable
fun HomeScreen(onProfileClick: () -> Unit) {
    SimpleAppScreen(title = "Home", actionLabel = "Profile", onAction = onProfileClick) {
        Text(
            text = "Welcome to Little Lemon",
            style = MaterialTheme.typography.headlineSmall,
            color = LittleLemonGreen,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Fresh Mediterranean flavors, made for sharing.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ProfileScreen(onBack: () -> Unit, onLogout: () -> Unit) {
    SimpleAppScreen(title = "Profile", actionLabel = "Back", onAction = onBack) {
        Text(
            text = "Your profile",
            style = MaterialTheme.typography.headlineSmall,
            color = LittleLemonGreen
        )
        Button(
            onClick = onLogout,
            colors = ButtonDefaults.buttonColors(
                containerColor = LittleLemonYellow,
                contentColor = Color.Black
            )
        ) {
            Text("Logout")
        }
    }
}

@Composable
private fun SimpleAppScreen(
    title: String,
    actionLabel: String,
    onAction: () -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(LittleLemonGreen)
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Little Lemon logo",
                modifier = Modifier.widthIn(max = 150.dp)
            )
            Button(
                onClick = onAction,
                colors = ButtonDefaults.buttonColors(
                    containerColor = LittleLemonYellow,
                    contentColor = Color.Black
                )
            ) {
                Text(actionLabel)
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 520.dp)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(title, style = MaterialTheme.typography.titleLarge)
            content()
        }
    }
}