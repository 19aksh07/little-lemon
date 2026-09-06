package com.example.littlelemon

import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.littlelemon.ui.theme.LittleLemonCharcoal
import com.example.littlelemon.ui.theme.LittleLemonGreen
import com.example.littlelemon.ui.theme.LittleLemonLightGray
import com.example.littlelemon.ui.theme.LittleLemonTheme
import com.example.littlelemon.ui.theme.LittleLemonYellow

private val PageBackground = Color(0xFFF7F8F6)
private val CardBorder = Color(0xFFD9DEDC)
private val MutedText = Color(0xFF66736E)

@Composable
fun HomeScreen(
    menuItems: List<MenuItemEntity> = emptyList(),
    menuLoading: Boolean = false,
    menuError: String? = null,
    onRetryMenu: () -> Unit = {},
    onProfileClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBackground)
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppHeader(onProfileClick = onProfileClick)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 560.dp)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 28.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            HeroSection()
            MenuItems(
                items = menuItems,
                isLoading = menuLoading,
                errorMessage = menuError,
                onRetry = onRetryMenu
            )
        }
    }
}

@Composable
private fun HeroSection() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = LittleLemonGreen
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Little Lemon", style = MaterialTheme.typography.headlineSmall, color = LittleLemonYellow)
                Text("Chicago", style = MaterialTheme.typography.titleMedium, color = Color.White)
                Text(
                    "We are a family-owned Mediterranean restaurant, focused on traditional recipes served with a modern twist",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
            }
            Image(
                painter = painterResource(R.drawable.hero_image),
                contentDescription = "Little Lemon dishes",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(132.dp)
                    .clip(RoundedCornerShape(14.dp))
            )
        }
    }
}

@Composable
fun MenuItems(
    items: List<MenuItemEntity>,
    isLoading: Boolean,
    errorMessage: String?,
    onRetry: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text(
            "Menu",
            style = MaterialTheme.typography.titleLarge,
            color = LittleLemonCharcoal
        )
        if (isLoading && items.isEmpty()) {
            Text(
                "Loading today's menu...",
                style = MaterialTheme.typography.bodyLarge,
                color = MutedText
            )
        } else if (errorMessage != null && items.isEmpty()) {
            Text(
                "We couldn't load the menu right now.",
                style = MaterialTheme.typography.bodyLarge,
                color = MutedText
            )
            Button(
                onClick = onRetry,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LittleLemonYellow,
                    contentColor = LittleLemonCharcoal
                )
            ) {
                Text("Try again")
            }
        } else {
            items.forEach { item -> MenuItemCard(item) }
        }
    }
}

@Composable
@OptIn(ExperimentalGlideComposeApi::class)
private fun MenuItemCard(item: MenuItemEntity) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlideImage(
                model = item.localImageRes ?: item.image,
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(104.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(item.title, style = MaterialTheme.typography.titleMedium, color = LittleLemonCharcoal)
                    Text("$${item.price}", style = MaterialTheme.typography.titleMedium, color = LittleLemonGreen)
                }
                Text(
                    item.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MutedText,
                    maxLines = 3
                )
            }
        }
    }
}

private val MenuItemEntity.localImageRes: Int?
    get() = when (title.trim().lowercase()) {
        "grilled fish" -> R.drawable.grilled_fish
        "lemon desert", "lemon dessert" -> R.drawable.lemon_dessert
        else -> null
    }

@Composable
fun ProfileScreen(
    preferences: SharedPreferences,
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    val firstName = preferences.getString("first_name", "") ?: ""
    val lastName = preferences.getString("last_name", "") ?: ""
    val email = preferences.getString("email", "") ?: ""

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBackground)
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileHeader(onBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 560.dp)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.profile_vector),
                        contentDescription = "Profile photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(128.dp)
                            .clip(CircleShape)
                    )
                    Text(
                        "Profile information",
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.titleMedium,
                        color = LittleLemonGreen
                    )
                    ProfileValue("First name", firstName)
                    ProfileValue("Last name", lastName)
                    ProfileValue("Email", email)
                    Spacer(Modifier.height(4.dp))
                    Button(
                        onClick = onLogout,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LittleLemonYellow,
                            contentColor = LittleLemonCharcoal
                        )
                    ) {
                        Text("Log out", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileHeader(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(LittleLemonLightGray)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(48.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back to home",
                tint = LittleLemonGreen
            )
        }
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "Little Lemon logo",
            modifier = Modifier.widthIn(max = 175.dp)
        )
    }
}

@Composable
private fun ProfileValue(label: String, value: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = LittleLemonGreen)
        Text(
            value.ifBlank { "Not provided" },
            style = MaterialTheme.typography.bodyLarge,
            color = LittleLemonCharcoal
        )
    }
}

@Composable
private fun AppHeader(onProfileClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(LittleLemonLightGray)
            .padding(horizontal = 20.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "Little Lemon logo",
            modifier = Modifier.widthIn(max = 175.dp)
        )
        Image(
            painter = painterResource(R.drawable.profile_vector),
            contentDescription = "Open profile",
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(44.dp)
                .clip(CircleShape)
                .clickable(onClick = onProfileClick)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomePreview() {
    LittleLemonTheme { HomeScreen(onProfileClick = {}) }
}
