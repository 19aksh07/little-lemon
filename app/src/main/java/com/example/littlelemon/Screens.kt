package com.example.littlelemon

import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Search
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.littlelemon.ui.theme.LittleLemonCharcoal
import com.example.littlelemon.ui.theme.LittleLemonGreen
import com.example.littlelemon.ui.theme.LittleLemonLightGray
import com.example.littlelemon.ui.theme.LittleLemonTheme
import com.example.littlelemon.ui.theme.LittleLemonYellow

private val MutedText = Color(0xFF66736E)

@Composable
fun HomeScreen(
    menuItems: List<MenuItemEntity> = emptyList(),
    menuLoading: Boolean = false,
    menuError: String? = null,
    onRetryMenu: () -> Unit = {},
    onProfileClick: () -> Unit
) {
    var searchPhrase by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("all") }
    val categories = remember(menuItems) {
        listOf("all") + menuItems
            .map { it.category.trim().lowercase() }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()
    }
    val filteredItems = menuItems.filter { item ->
        val categoryMatches = selectedCategory == "all" ||
            item.category.equals(selectedCategory, ignoreCase = true)
        val query = searchPhrase.trim()
        val searchMatches = query.isBlank() ||
            item.title.contains(query, ignoreCase = true) ||
            item.description.contains(query, ignoreCase = true)
        categoryMatches && searchMatches
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppHeader(onProfileClick = onProfileClick)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top
        ) {
            HeroSection(
                searchPhrase = searchPhrase,
                onSearchPhraseChange = { searchPhrase = it }
            )
            CategoryBreakdown(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it }
            )
            MenuItems(
                items = filteredItems,
                isLoading = menuLoading,
                errorMessage = menuError,
                onRetry = onRetryMenu
            )
        }
    }
}

@Composable
private fun HeroSection(
    searchPhrase: String,
    onSearchPhraseChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(LittleLemonGreen)
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        Text(
            "Little Lemon",
            style = MaterialTheme.typography.displayLarge,
            color = LittleLemonYellow,
            modifier = Modifier.padding(bottom = 0.dp)
        )
        Text(
            "Chicago",
            style = MaterialTheme.typography.displayMedium,
            color = Color.White,
            modifier = Modifier.padding(top = 0.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "We are a family-owned Mediterranean restaurant, focused on traditional recipes served with a modern twist",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                modifier = Modifier.weight(1f).padding(end = 16.dp)
            )
            Image(
                painter = painterResource(R.drawable.hero_image),
                contentDescription = "Little Lemon dishes",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(140.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = searchPhrase,
            onValueChange = onSearchPhraseChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter search phrase") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search menu")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            shape = RoundedCornerShape(8.dp),
            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFEDEFEE),
                unfocusedContainerColor = Color(0xFFEDEFEE),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            )
        )
    }
}

@Composable
private fun CategoryBreakdown(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "ORDER FOR DELIVERY!",
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 18.sp),
            color = LittleLemonCharcoal
        )
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            categories.forEach { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { onCategorySelected(category) },
                    label = {
                        Text(
                            text = if (category == "all") "All" else category.replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = LittleLemonGreen,
                        selectedLabelColor = Color.White,
                        containerColor = LittleLemonLightGray,
                        labelColor = LittleLemonGreen
                    ),
                    border = null,
                    shape = RoundedCornerShape(16.dp)
                )
            }
        }
        androidx.compose.material3.HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp),
            thickness = 1.dp,
            color = LittleLemonLightGray
        )
    }
}

@Composable
fun MenuItems(
    items: List<MenuItemEntity>,
    isLoading: Boolean,
    errorMessage: String?,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (isLoading && items.isEmpty()) {
            Text(
                "Loading today's menu...",
                style = MaterialTheme.typography.bodyLarge,
                color = MutedText,
                modifier = Modifier.padding(vertical = 20.dp)
            )
        } else if (errorMessage != null && items.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "We couldn't load the menu right now.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MutedText
                )
                Button(
                    onClick = onRetry,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LittleLemonYellow,
                        contentColor = LittleLemonCharcoal
                    )
                ) {
                    Text("Try again")
                }
            }
        } else if (items.isEmpty()) {
            Text(
                "No menu items match your filters.",
                style = MaterialTheme.typography.bodyLarge,
                color = MutedText,
                modifier = Modifier.padding(vertical = 20.dp)
            )
        } else {
            items.forEachIndexed { index, item ->
                MenuItemCard(item)
                if (index < items.size - 1) {
                    androidx.compose.material3.HorizontalDivider(
                        thickness = 1.dp,
                        color = LittleLemonLightGray
                    )
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalGlideComposeApi::class)
private fun MenuItemCard(item: MenuItemEntity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                color = LittleLemonCharcoal
            )
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MutedText,
                maxLines = 2,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
            Text(
                text = "$${item.price}",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Medium),
                color = MutedText
            )
        }
        GlideImage(
            model = item.localImageRes ?: item.image,
            contentDescription = item.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp))
        )
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
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppHeader(onBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                "Personal information",
                style = MaterialTheme.typography.titleLarge,
                color = LittleLemonCharcoal
            )

            // Profile Image in content - improvised styling and alignment
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.profile_vector),
                    contentDescription = "Profile photo",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .border(2.dp, LittleLemonGreen, CircleShape)
                        .background(LittleLemonLightGray)
                )
            }

            ProfileField(label = "First name", value = firstName)
            ProfileField(label = "Last name", value = lastName)
            ProfileField(label = "Email", value = email)

            Spacer(Modifier.height(40.dp))

            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                contentPadding = PaddingValues(vertical = 12.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LittleLemonYellow,
                    contentColor = LittleLemonCharcoal
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF48E1C))
            ) {
                Text(
                    "Log out",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun ProfileField(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = LittleLemonCharcoal
        )
        OutlinedTextField(
            value = value,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            shape = RoundedCornerShape(8.dp)
        )
    }
}

@Composable
private fun AppHeader(
    onProfileClick: (() -> Unit)? = null,
    onBack: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        if (onBack != null) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = LittleLemonGreen
                )
            }
        }

        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "Little Lemon logo",
            modifier = Modifier.height(60.dp)
        )

        if (onProfileClick != null) {
            Image(
                painter = painterResource(R.drawable.profile_vector),
                contentDescription = "Open profile",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(50.dp)
                    .clickable(onClick = onProfileClick)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomePreview() {
    LittleLemonTheme { HomeScreen(onProfileClick = {}) }
}
