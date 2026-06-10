package io.android.discovery.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun VintyBottomBar(
    onHomeClick: () -> Unit = {},
    onWatchlistClick: () -> Unit = {}, // Novo callback
    onProfileClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    NavigationBar(
        containerColor = Color.Black,
        contentColor = Color.White
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, null) },
            label = { Text("Home") },
            selected = false, // Ajuste conforme a rota ativa
            onClick = onHomeClick,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                indicatorColor = Color(0xFF2A2A2A),
                unselectedIconColor = Color.Gray
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Star, null) },
            label = { Text("Lista") },
            selected = false, // Ajuste conforme a rota ativa
            onClick = onWatchlistClick, // Conectado
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                indicatorColor = Color(0xFF2A2A2A),
                unselectedIconColor = Color.Gray
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, null) },
            label = { Text("Perfil") },
            selected = false,
            onClick = onProfileClick,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                indicatorColor = Color(0xFF2A2A2A),
                unselectedIconColor = Color.Gray
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, null) },
            label = { Text("Ajustes") },
            selected = false,
            onClick = onSettingsClick,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                indicatorColor = Color(0xFF2A2A2A),
                unselectedIconColor = Color.Gray
            )
        )
    }
}