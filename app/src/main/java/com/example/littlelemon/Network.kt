package com.example.littlelemon

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

private const val MENU_URL =
    "https://raw.githubusercontent.com/Meta-Mobile-Developer-PC/Working-With-Data-API/main/menu.json"

private val menuJson = Json { ignoreUnknownKeys = true }

@Serializable
data class MenuNetwork(
    @SerialName("menu")
    val menu: List<MenuItemNetwork>
)

@Serializable
data class MenuItemNetwork(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("price")
    val price: String,
    @SerialName("image")
    val image: String,
    @SerialName("category")
    val category: String = ""
)

fun createMenuHttpClient(): HttpClient = HttpClient {
    install(HttpTimeout) {
        requestTimeoutMillis = 15_000
        connectTimeoutMillis = 10_000
        socketTimeoutMillis = 15_000
    }
    install(ContentNegotiation) {
        json(menuJson)
    }
}

class MenuNetworkRepository(private val client: HttpClient) {
    suspend fun fetchMenu(): List<MenuItemNetwork> {
        val responseText = client.get(MENU_URL).bodyAsText()
        return menuJson.decodeFromString<MenuNetwork>(responseText)
            .menu
    }
}
